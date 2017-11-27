package commexercise.pubsub;

public interface PubSubSubscriberListener {
    void subscriberJoined(String topic, String uniqueId);
    void subscriberLeft(String topic, String uniqueId);
}
