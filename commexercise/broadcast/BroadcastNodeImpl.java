package commexercise.broadcast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import com.sun.org.apache.bcel.internal.generic.IADD;
import java.util.ArrayList;

public class BroadcastNodeImpl implements BroadcastNode,
                                          Runnable {
  
  public static final int BEGIN_PORT=16332;
  public static final int END_PORT=BEGIN_PORT+19;
  public static final int MAXBUFSIZE=1200;
  public static final int BC_PKTID=0xb32e;
  
  private Vector<MessageListener> listeners;
  private Thread receiveThread;
  private static byte[] rbuf;
  private static byte[] sbuf;
  private String nodename;
  private byte[] myIP;
  private DatagramSocket sendsock;
  private InetAddress broadcast;

  public BroadcastNodeImpl(String nodename) {
    listeners=new Vector<MessageListener>();
    rbuf=new byte[MAXBUFSIZE];
    sbuf=new byte[MAXBUFSIZE];
    this.nodename=(nodename.length()>32)?nodename.substring(0,32):nodename;
    
    try {
      Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
      ArrayList<InetAddress> inlist=new ArrayList<InetAddress>();
      for (NetworkInterface netint : Collections.list(nets)) {
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        inlist.addAll(Collections.list(inetAddresses));
      }
      InetAddress linklocal=null;
      InetAddress local=null;
      InetAddress loopback=null;
      for (InetAddress inetAddress : inlist) {
        if (!(inetAddress instanceof Inet4Address)) continue;
        if (inetAddress.isLoopbackAddress()) {
          loopback=inetAddress;
        }
        else if (inetAddress.isLinkLocalAddress()) {
          linklocal=inetAddress;
        }
        else if (inetAddress.isSiteLocalAddress()) {
          local=inetAddress;
        }
      }
      InetAddress sockaddr=null;
      if (loopback!=null) {
        sockaddr=loopback;
      }
      if (linklocal!=null) {
        sockaddr=linklocal;
      }
      if (local!=null) {
        sockaddr=local;
      }
      if (sockaddr==null) {
        System.out.println("No suitable IP address found to bind sender socket. Exiting.");
        System.exit(1);
      }
      InetAddress bcast=null;
      nets = NetworkInterface.getNetworkInterfaces();
      for (NetworkInterface netint : Collections.list(nets)) {
        List<InterfaceAddress> list = netint.getInterfaceAddresses();
        Iterator<InterfaceAddress> it = list.iterator();
        while (it.hasNext()) {
          InterfaceAddress ia = it.next();
          if (ia.getAddress().equals(sockaddr)) {
            bcast=ia.getBroadcast();
          }
        }
      }
      if (bcast==null) {
        System.out.println("No broadcast address found for sender socket. Exiting.");
        System.exit(1);
      }
      broadcast=bcast;
      byte[] inb=sockaddr.getAddress();
      myIP=inb;
      sendsock=new DatagramSocket();
      System.out.println("Binding sender to "+(inb[0]&0xff)+"."
                             +(inb[1]&0xff)+"."+(inb[2]&0xff)+"."+(inb[3]&0xff));
      inb=bcast.getAddress();
      System.out.println("Broadcast address: "+(inb[0]&0xff)+"."
                             +(inb[1]&0xff)+"."+(inb[2]&0xff)+"."+(inb[3]&0xff));
    }
    catch (Exception e) {
      e.printStackTrace();
      System.out.println("Network identification failed. Shutting down.");
      System.exit(1);
    }
    
    receiveThread=new Thread(this);
    receiveThread.start();
  }
  
  @Override
  public void sendMessage(String message) {
    byte[] ploadbytes=message.substring(0,Math.min(1024, message.length())).getBytes();
    byte[] origbytes=nodename.getBytes();
    int pktlen=2+1+origbytes.length+4+2+ploadbytes.length;
    sbuf[0]=(byte)(BC_PKTID>>8);
    sbuf[1]=(byte)(BC_PKTID&0xff);
    sbuf[2]=(byte)origbytes.length;
    System.arraycopy(origbytes, 0, sbuf, 3, origbytes.length);
    int writePtr=3+origbytes.length;
    System.arraycopy(myIP, 0, sbuf, writePtr, 4);
    writePtr+=4;
    sbuf[writePtr++]=(byte)((ploadbytes.length>>8)&0xff);
    sbuf[writePtr++]=(byte)(ploadbytes.length&0xff);
    System.arraycopy(ploadbytes,0,sbuf,writePtr,ploadbytes.length);
    try {
      for (int i=BEGIN_PORT;i<=END_PORT;i++) {
        DatagramPacket pack=new DatagramPacket(sbuf,pktlen,broadcast,i);
        sendsock.send(pack);
      }
    } catch (UnknownHostException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // TODO Auto-generated method stub
    
  }

  @Override
  public void addMessageListener(MessageListener l) {
    if (!listeners.contains(l)) {
      listeners.add(l);
    }
  }

  @Override
  public void removeMessageListener(MessageListener l) {
    try {
      listeners.remove(l);
    }
    catch (Exception e) {};
  }

  private DatagramSocket findFreePort() {
    DatagramSocket rv=null;
    int probe=BEGIN_PORT;
    while ((probe<=END_PORT) && (rv==null)) {
      try {
        DatagramSocket ds=new DatagramSocket(probe);
        rv=ds;
        System.out.println("Binding receiver to port "+probe);
      }
      catch (SocketException e) {} //silently ignore, try next port
      probe++;
    }
    return rv;
  }
  
  private void decode(byte[] buf) {
    //discard packets without the correct packet identifier
    if (buf[0]!=(byte)(BC_PKTID>>8))
      return;
    if (buf[1]!=(byte)(BC_PKTID&0xff))
      return;
    int readptr=2;
    byte origlen=buf[readptr++];
    if ((origlen<0) || (origlen>32)) return;
    String origin=new String(buf,readptr,origlen);
    readptr+=origlen;
    byte[] origip=new byte[4];
    System.arraycopy(buf,readptr,origip,0,4);
    readptr+=4;
    int payloadlen=((int)buf[readptr++]<<8)+(int)buf[readptr++];
    String payload=new String(buf,readptr,payloadlen);
    readptr+=payloadlen;
    
    for (MessageListener li:listeners) {
      li.messageReceived(payload,origin);
    }
  }
  
  @Override
  public void run() {
    DatagramSocket sock=findFreePort();
    if (sock==null) {
      System.out.println("Could not find unbound port within range. Shutting down.");
      System.exit(1);
    }
    while (!Thread.interrupted()) {
      try {
        DatagramPacket pack=new DatagramPacket(rbuf,rbuf.length);
        sock.receive(pack);
        decode(rbuf);
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
  
  public static void main(String args[]) {
    if (args.length<1) {
      System.out.println("Usage: BroadcastNodeImpl <nodename>");
      System.exit(1);
    }
    new BroadcastNodeImpl(args[0]);
  }

}
