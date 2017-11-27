package commexercise.rpc;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.client.AsyncCallback;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RpcClientImpl implements RpcClient {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private XmlRpcClientConfigImpl config;
    private XmlRpcClient client;
    private static final String RPC_SERVICENAME_SYNC = "RPCImplementation.callSync";
    private static final String RPC_SERVICENAME_ASYNC = "RPCImplementation.callAsync";

    public RpcClientImpl(String hostname, int port) throws MalformedURLException {
        this(new URL("http://"+hostname + ":" + port));
    }

    public RpcClientImpl(String address) throws MalformedURLException {
        this(new URL(address));
    }

    public RpcClientImpl(URL url) {
        config = new XmlRpcClientConfigImpl();
        config.setServerURL(url);
        config.setEnabledForExceptions(true);
        config.setEnabledForExtensions(true);
        config.setConnectionTimeout(3000);
        client = new XmlRpcClient();
        client.setTransportFactory(new XmlRpcCommonsTransportFactory(client));
        client.setConfig(config);
    }

    public void setTimeout(int seconds) {
        if (config != null) {
            config.setReplyTimeout(seconds * 1000);
        }
    }

    /**
     * Perform a synchronous call to the RPC server
     * @param functionName Name of the function you want to call on the server
     * @param args A String array of arguments
     * @return Returns a String array
     * @throws Exception Throws an exception if an errror happended, the original exception from the server is linked in this.
     */
    public String[] callSync(String functionName, String[] args) throws Exception {
        try {
            if (args == null) args = new String[]{};
            String[] req = new String[args.length + 1];
            System.arraycopy(args, 0, req, 1, args.length);
            req[0] = functionName;
            Object resp = client.execute(RPC_SERVICENAME_SYNC, req);
            Object[] result = (Object[])resp;
            String[] strings = new String[result.length];
            for (int i=0; i < result.length; i++) {
                strings[i] = (String)result[i];
            }
            return strings;
        } catch (XmlRpcException e) {
            log.error("Exception raised during synchronous call", e);
            throw new Exception(e.getMessage(), e);
        }
    }

    /**
     * Perform an asynchronous call to the RPC server
     * @param functionName Name of the function you want to call on the server
     * @param args An object array of arguments, can be any type ie. new Object[]{int, string, byte[], long}
     * @param callbackListener An instance of CallbackListener used to return the result from the server
     */
    public void callAsync(String functionName, String[] args, final CallbackListener callbackListener) throws Exception {
        callAsync(RPC_SERVICENAME_ASYNC, args, 0L, callbackListener);
    }

    /**
     * Perform an asynchronous call to the RPC server
     * @param functionName Name of the function you want to call on the server
     * @param args A String array of arguments
     * @param callID Provide an ID to identify the response from the server on reply
     * @param callbackListener An instance of CallbackListener used to return the result from the server
     */
    public void callAsync(String functionName, String[] args, final long callID, final CallbackListener callbackListener) throws Exception {
        try {
            String[] req = new String[args.length + 2];
            System.arraycopy(args, 0, req, 2, args.length);
            req[0] = functionName;
            req[1] = String.valueOf(callID);
            client.executeAsync(RPC_SERVICENAME_ASYNC, req, new AsyncCallback() {
                public void handleResult(XmlRpcRequest pRequest, Object pResult) {
                    Object[] resp = (Object[])pResult;
                    String[] strings = new String[resp.length];
                    for (int i=0; i < resp.length; i++) {
                        strings[i] = (String)resp[i];
                    }
                    callbackListener.functionExecuted(callID, strings);
                }

                public void handleError(XmlRpcRequest pRequest, Throwable pError) {
                    callbackListener.functionFailed(callID, new Exception(pError.getMessage(), pError));
                }
            });
        } catch (XmlRpcException e) {
            log.error("Exception raised during asynchronous call", e);
            throw new Exception(e.getMessage(), e);
        }
    }

    // HELPER FUNCTIONS
    public static List<String> stringArrayToList(String[] str) {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, str);
        return list;
    }
    public static String[] listToStringArray(List<String> list) {
        String[] str = new String[list.size()];
        str = list.toArray(str);
        return str;
    }
}
