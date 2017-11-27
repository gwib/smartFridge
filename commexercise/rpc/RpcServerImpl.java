package commexercise.rpc;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RpcServerImpl implements RpcServer {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private WebServer webServer;
    private XmlRpcServer xmlRpcServer;
    private PropertyHandlerMapping phm;
    private Thread shutdownThread;
    private int port;
    private ListenerCarrier carrier;
    private RpcHandler handler;

    public RpcServerImpl(int port) {
        this.port = port;
        this.carrier = new ListenerCarrier();
        shutdownThread = new Thread() {
            @Override
            public void run() {
                webServer.shutdown();
            }
        };
    }

    @Override
    public void setCallListener(CallListener listener) {
        if (carrier.listener != null) log.debug("Remember only one call listener can be active at any time, the new listener will replace the old.");
        carrier.listener = listener;
    }

    @Override
    public void removeCallListener(CallListener listener) {
        carrier.listener = null;
    }

    /**
     * Starts the server.<br>
     * Can be chained: RpcServer server = new RpcServer(8080).start();
     */
    public RpcServerImpl start() throws Exception {
        Runtime.getRuntime().addShutdownHook(shutdownThread);

        webServer = new WebServer(port);
        xmlRpcServer = webServer.getXmlRpcServer();

        XmlRpcServerConfigImpl config = (XmlRpcServerConfigImpl)xmlRpcServer.getConfig();
        config.setEnabledForExceptions(true);
        config.setEnabledForExtensions(true);
        config.setContentLengthOptional(false);

        phm = new PropertyHandlerMapping();
        handler = new RpcHandler(carrier);
        phm.setRequestProcessorFactoryFactory(new RpcHandlerRequestProcessorFactoryFactory(handler));
        phm.addHandler("RPCImplementation", RpcHandler.class);
        xmlRpcServer.setHandlerMapping(phm);

        webServer.start();
        return this;
    }

    /**
     * Shutdown the RPC server.
     */
    public void stop() {
        Runtime.getRuntime().removeShutdownHook(shutdownThread);
        webServer.shutdown();
    }

    private class ListenerCarrier {
        public CallListener listener;
    }

    private class RpcHandlerRequestProcessorFactoryFactory implements RequestProcessorFactoryFactory {
        private final RequestProcessorFactory factory = new RpcHandlerRequestProcessorFactory();
        private final RpcHandler handler;

        public RpcHandlerRequestProcessorFactoryFactory(RpcHandler handler) {
            this.handler = handler;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public RequestProcessorFactory getRequestProcessorFactory(Class aClass) throws XmlRpcException {
            return factory;
        }

        private class RpcHandlerRequestProcessorFactory implements RequestProcessorFactory {
            @Override
            public Object getRequestProcessor(XmlRpcRequest xmlRpcRequest) throws XmlRpcException {
                return handler;
            }
        }
    }

    protected class RpcHandler {
        private ListenerCarrier carrier;

        public RpcHandler(ListenerCarrier carrier) {
            this.carrier = carrier;
        }

        private String[] callSync(String function, String[] args) throws Exception {
            if (carrier.listener == null) throw new Exception("No CallListener has been set on the RPC server!");
            return carrier.listener.receivedSyncCall(function, args);
        }

        private String[] callAsync(String function, String[] args, long callID) throws Exception {
            if (carrier.listener == null) throw new Exception("No CallListener has been set on the RPC server!");
            return carrier.listener.receivedAsyncCall(function, args, callID);
        }

        // stupid work around methods
        public String[] callSync(String function) throws Exception {
            List<String> list = new ArrayList<>();
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            return callSync(function, (String[])list.toArray());
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15, String arg16) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            list.add(arg16);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15, String arg16, String arg17) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            list.add(arg16);
            list.add(arg17);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15, String arg16, String arg17, String arg18) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            list.add(arg16);
            list.add(arg17);
            list.add(arg18);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15, String arg16, String arg17, String arg18, String arg19) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            list.add(arg16);
            list.add(arg17);
            list.add(arg18);
            list.add(arg19);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }
        public String[] callSync(String function, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15, String arg16, String arg17, String arg18, String arg19, String arg20) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            list.add(arg16);
            list.add(arg17);
            list.add(arg18);
            list.add(arg19);
            list.add(arg20);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            return callSync(function, arr);
        }











        public String[] callAsync(String function, String callID) throws Exception {
            List<String> list = new ArrayList<>();
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15, String arg16) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            list.add(arg16);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15, String arg16, String arg17) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            list.add(arg16);
            list.add(arg17);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15, String arg16, String arg17, String arg18) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            list.add(arg16);
            list.add(arg17);
            list.add(arg18);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15, String arg16, String arg17, String arg18, String arg19) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            list.add(arg16);
            list.add(arg17);
            list.add(arg18);
            list.add(arg19);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }
        public String[] callAsync(String function, String callID, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6, String arg7, String arg8, String arg9, String arg10, String arg11, String arg12, String arg13, String arg14, String arg15, String arg16, String arg17, String arg18, String arg19, String arg20) throws Exception {
            List<String> list = new ArrayList<>();
            list.add(arg1);
            list.add(arg2);
            list.add(arg3);
            list.add(arg4);
            list.add(arg5);
            list.add(arg6);
            list.add(arg7);
            list.add(arg8);
            list.add(arg9);
            list.add(arg10);
            list.add(arg11);
            list.add(arg12);
            list.add(arg13);
            list.add(arg14);
            list.add(arg15);
            list.add(arg16);
            list.add(arg17);
            list.add(arg18);
            list.add(arg19);
            list.add(arg20);
            String[] arr = new String[list.size()];
            arr = list.toArray(arr);
            long call = Long.parseLong(callID);
            return callAsync(function, arr, call);
        }



    }
}
