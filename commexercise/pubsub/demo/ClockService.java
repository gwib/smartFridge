package commexercise.pubsub.demo;

import commexercise.pubsub.PubSubServer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockService implements Runnable {
    private PubSubServer broker;
    private Boolean running;
    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private Thread thread;

    public ClockService(PubSubServer server) {
        this.broker = server;
        this.thread = new Thread(this);
    }

    public void run() {
    	running = true;
        while(running) {
            String time = sdf.format(new Date());
            broker.send("clock", new String[]{time});
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) { 
            	running = false;
            }
        }
    }

    public ClockService start() {
        this.thread.start();
        return this;
    }
    
    public void stop() {
    	running = false;
        this.thread.interrupt();
    }
}
