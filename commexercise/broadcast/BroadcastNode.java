package commexercise.broadcast;

public interface BroadcastNode {

  void sendMessage(String message);
  void addMessageListener(MessageListener l);
  void removeMessageListener(MessageListener l);
  
}
