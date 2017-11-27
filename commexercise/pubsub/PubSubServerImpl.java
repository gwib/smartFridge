package commexercise.pubsub;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("rawtypes")
public class PubSubServerImpl implements PubSubServer {
    private WebServer webServer;
    private XmlRpcServer xmlRpcServer;
    private PropertyHandlerMapping phm;
    private Thread shutdownThread;
    private int port;
    private Map<String, Map<String, BlockingQueue>> topics = new HashMap<String, Map<String, BlockingQueue>>();
    private Semaphore lock = new Semaphore(1, true);
    private List<PubSubSubscriberListener> listeners = new ArrayList<PubSubSubscriberListener>();
    private PubSubHandler handler;

    public PubSubServerImpl(int port) {
        this.port = port;
        shutdownThread = new Thread() {
            @Override
            public void run() {
                webServer.shutdown();
            }
        };
    }


    /**
     * Start accepting subscribes for the PubSub<br/>
     * Can be chained: RpcServer server = new RpcServer(9090).start();
     */
    public PubSubServerImpl start() throws Exception {
        Runtime.getRuntime().addShutdownHook(shutdownThread);

        webServer = new WebServer(port);
        xmlRpcServer = webServer.getXmlRpcServer();

        XmlRpcServerConfigImpl config = (XmlRpcServerConfigImpl)xmlRpcServer.getConfig();
        config.setEnabledForExceptions(true);
        config.setEnabledForExtensions(true);
        config.setContentLengthOptional(false);

        phm = new PropertyHandlerMapping();
        handler = new PubSubHandler(this);
        phm.setRequestProcessorFactoryFactory(new PubSubHandlerRequestProcessorFactoryFactory(handler));
        phm.setVoidMethodEnabled(true);
        phm.addHandler("PubSubImplementation", PubSubHandler.class);
        xmlRpcServer.setHandlerMapping(phm);

        webServer.start();
        return this;
    }

    /**
     * Shutdown PubSub server, stops accepting subscribers, no new listeners will be accepted on the socket, the currently connected listeners will be connected until they time out
     */
    public void stop() {
        handler.stop();
        Runtime.getRuntime().removeShutdownHook(shutdownThread);
        webServer.shutdown();
    }

    /**
     * Send a message to all subscribed listeners for the given topic
     * @param topic The topic to send the message to
     * @param message The message
     */
    @SuppressWarnings("unchecked")
    public void send(String topic, String[] message) {
        try {
            lock.acquire();
            if (topics.containsKey(topic)) {
                for (BlockingQueue queue : topics.get(topic).values()) {
                    queue.add(message);
                }
            }
            lock.release();
        } catch (InterruptedException e) {
            // ignored
        }
    }

    public void addSubscriberListener(PubSubSubscriberListener listener) {
        listeners.add(listener);
    }

    public void removeSubscriberListener(PubSubSubscriberListener listener) {
        listeners.add(listener);
    }

    @Override
    public String[] getSubscriberListForTopic(String topic) {
        if (topics.containsKey(topic)) {
            Map<String, BlockingQueue> clients = topics.get(topic);
            String[] clientsStr = new String[clients.size()];
            int i = 0;
            for (String client : clients.keySet()) {
                clientsStr[i] = client;
                i++;
            }
            return clientsStr;
        } else {
            return new String[0];
        }
    }


    private BlockingQueue getQueue(String topic, String uniqueId) {
        try {
            Boolean newsub = false;
            lock.acquire();
            if (!topics.containsKey(topic)) {
                topics.put(topic, new HashMap<String, BlockingQueue>());
            }
            if (!topics.get(topic).containsKey(uniqueId)) {
                topics.get(topic).put(uniqueId, new ArrayBlockingQueue(100));
                newsub = true;
            }
            BlockingQueue queue = topics.get(topic).get(uniqueId);
            lock.release();

            if (newsub) {
                for (PubSubSubscriberListener listener : listeners) {
                    listener.subscriberJoined(topic, uniqueId); // don't do long computation on this thread as it will result in lost communication
                }
            }

            return queue;
        } catch (InterruptedException e) {
            // ignored
            return null;
        }
    }

    private void unsubscribe(String topic, String uniqueId) {
        try {
            Boolean subleft = false;
            lock.acquire();
            if (topics.containsKey(topic)) {
                if (topics.get(topic).containsKey(uniqueId)) {
                    topics.get(topic).remove(uniqueId);
                    subleft = true;
                }
                if (topics.get(topic).size() == 0) {
                    topics.remove(topic);
                }
            }
            lock.release();

            if (subleft) {
                for (PubSubSubscriberListener listener : listeners) {
                    listener.subscriberLeft(topic, uniqueId);
                }
            }
        } catch (InterruptedException e) {
            // ignored
        }
    }


    private class PubSubHandlerRequestProcessorFactoryFactory implements RequestProcessorFactoryFactory {
        private final RequestProcessorFactory factory = new PubSubHandlerRequestProcessorFactory();
        private final PubSubHandler handler;

        public PubSubHandlerRequestProcessorFactoryFactory(PubSubHandler handler) {
            this.handler = handler;
        }

        @Override
        public RequestProcessorFactory getRequestProcessorFactory(Class aClass) throws XmlRpcException {
            return factory;
        }

        private class PubSubHandlerRequestProcessorFactory implements RequestProcessorFactory {
            @Override
            public Object getRequestProcessor(XmlRpcRequest xmlRpcRequest) throws XmlRpcException {
                return handler;
            }
        }
    }

    protected class PubSubHandler {
        private long pollTimeout = 1000;
        private Boolean running;
        private PubSubServerImpl broker;

        public PubSubHandler(PubSubServerImpl broker) {
            running = true;
            this.broker = broker;
        }

        public void stop() {
            running = false;
        }

        public Object listen(String topic, String uniqueId) throws Exception {
            BlockingQueue queue = broker.getQueue(topic, uniqueId);
            Object result = null;
            while (result == null && running) {
                result = queue.poll(pollTimeout, TimeUnit.MILLISECONDS);
            }
            return result;
        }

        public void unsubscribe(String topic, String uniqueId) throws Exception {
            broker.unsubscribe(topic, uniqueId);
        }
    }
}
