package commexercise.pubsub;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PubSubClientImpl implements PubSubClient {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private XmlRpcClientConfigImpl config;
    private XmlRpcClient client;
    private Map<PubSubCallbackListener, SubscriberThreadThreadPair> listeners;
    private String uniqueId;
    private Boolean clientActive; // ID cannot be changed if the client is active

    public PubSubClientImpl(String hostname, int port, String name) throws MalformedURLException {
        this("http://"+hostname + ":" + port,name);
    }

    public PubSubClientImpl(String address, String name) throws MalformedURLException {
        this(new URL(address),name);
    }

    public PubSubClientImpl(URL url, String name) {
        uniqueId = name;
        listeners = new HashMap<PubSubCallbackListener, SubscriberThreadThreadPair>();

        config = new XmlRpcClientConfigImpl();
        config.setServerURL(url);
        config.setEnabledForExceptions(true);
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(10);
        client = new XmlRpcClient();
        client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
        client.setConfig(config);
    }

    private Object send(String service, Object[] parms) throws XmlRpcException {
        return client.execute(service, parms);
    }

    /**
     * Set a unique ID for this client, will be used when the client subscribes to a PubSub server<br/>
     * A unique ID can be generated like this: String id = UUID.randomUUID().toString();<br/>
     * The ID cannot be changed after calling 'subscribe'
     * @param id A unique ID in the form of a String
     */
    public void setID(String id) {
        if (clientActive) {
            log.error("ID cannot be changed after the client has been subscribed!");
            return;
        }
        this.uniqueId = id;
    }

    /**
     * Create a subscription for a specific topic
     * @param topic The topic of interest
     * @param observer An instance of a callback that will get invoked when a message arrives with the topic
     */
    public void subscribe(String topic, PubSubCallbackListener observer) {
        clientActive = true;
        SubscriberThread st = new SubscriberThread(topic, uniqueId, observer, this);
        Thread t = new Thread(st);
        listeners.put(observer, new SubscriberThreadThreadPair(st, t));
        t.start();
    }

    /**
     * Unsubscribe from the topic
     */
    public void unsubscribe(PubSubCallbackListener observer) {
        Thread t = listeners.get(observer).thread;
        SubscriberThread st = listeners.get(observer).subscriberThread;
        listeners.remove(observer);
        st.stop();
        t.interrupt();
        try {
            send("PubSubImplementation.unsubscribe", new Object[]{st.topic, uniqueId});
        } catch (XmlRpcException e) {
            log.error("Unable to unsubscribe from server, unexpected behavior may follow...", e);
        }
    }

    /**
     * Unsubscribe from all topics for this client
     */
    public void stop() {
        for (SubscriberThreadThreadPair kv : listeners.values()) {
            kv.subscriberThread.stop();
            kv.thread.interrupt();
            try {
                send("PubSubImplementation.unsubscribe", new Object[]{kv.subscriberThread.topic, uniqueId});
            } catch (XmlRpcException e) {
                log.error("Unable to unsubscribe from server, unexpected behavior may follow...", e);
            }
        }
        listeners.clear();
    }


    private class SubscriberThread implements Runnable {
        private final Logger log = LoggerFactory.getLogger(this.getClass());

        public String topic;
        private String uniqueId;
        private PubSubClientImpl client;
        private PubSubCallbackListener observer;
        private Boolean running;

        public SubscriberThread(String topic, String uniqueId, PubSubCallbackListener observer, PubSubClientImpl client) {
            this.topic = topic;
            this.client = client;
            this.uniqueId = uniqueId;
            this.observer = observer;
        }

        public void run() {
            running = true;
            while (running) {
                try {
                    Object obj = client.send("PubSubImplementation.listen", new Object[]{topic, uniqueId});
                    if (!running) break;
                    Object[] result = (Object[])obj;
                    String[] strings = new String[result.length];
                    for (int i=0; i < result.length; i++) {
                        strings[i] = (String)result[i];
                    }
                    observer.messageReceived(strings);
                } catch (XmlRpcException e) {
                    if (!e.linkedException.getClass().equals(SocketTimeoutException.class)) {
                        if (!running) break;
                        log.error("Error while waiting for response from pubsub server", e);
                    }
                }
            }
        }

        public void stop() {
            running = false;
        }
    }

    public class SubscriberThreadThreadPair { //Changed from private to public
        public SubscriberThread subscriberThread;
        public Thread thread;

        public SubscriberThreadThreadPair(SubscriberThread st, Thread t) {
            this.subscriberThread = st;
            this.thread = t;
        }
    }

}
