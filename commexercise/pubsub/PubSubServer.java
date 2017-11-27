package commexercise.pubsub;

public interface PubSubServer {
    PubSubServer start() throws Exception;
    void stop();
    void send(String topic, String[] message);
    void addSubscriberListener(PubSubSubscriberListener listener);
    void removeSubscriberListener(PubSubSubscriberListener listener);
    String[] getSubscriberListForTopic(String topic);
}
