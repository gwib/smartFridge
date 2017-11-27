package commexercise.pubsub;

public interface PubSubCallbackListener {
    void messageReceived(String[] message);
}
