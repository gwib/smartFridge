package commexercise.broadcast;

public interface MessageListener {

  void messageReceived(String message, String origin);
  
}
