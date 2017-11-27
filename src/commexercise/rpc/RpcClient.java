package commexercise.rpc;

public interface RpcClient {
    void setTimeout(int seconds);
    String[] callSync(String functionName, String[] args) throws Exception;
    void callAsync(String functionName, String[] args, final CallbackListener callbackListener) throws Exception;
    void callAsync(String functionName, String[] args, final long callID, final CallbackListener callbackListener) throws Exception;
}
