package commexercise.rpc;

public interface CallListener {
    String[] receivedSyncCall(String function, String[] args) throws Exception;
    String[] receivedAsyncCall(String function, String[] args, long callID) throws Exception;
}
