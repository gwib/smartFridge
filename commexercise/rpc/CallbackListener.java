package commexercise.rpc;

public interface CallbackListener {
    void functionExecuted(long callID, String[] response);
    void functionFailed(long callID, Exception e);
}
