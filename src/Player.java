
import java.io.*;
import java.net.*;

/**
 * Created by David on 8/2/17.
 */
// 坦克大战client实体类
public class Player {
    TankClient tankClient;
    private int udpPort;
    String IP; //Server IP
    DatagramSocket ds = null;

    public Player(TankClient tc){
      tankClient = tc;
    }

    public void connect(String IP, int port){
        this.IP = IP;
        //this.udpPort = port;
        try{
            ds = new DatagramSocket(udpPort);
        }catch (IOException e){
            e.printStackTrace();
        }
        Socket s = null;
        try {
            s = new Socket(IP, port);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.writeInt(udpPort);
            DataInputStream dis = new DataInputStream(s.getInputStream());
            int id = dis.readInt();
            tankClient.myPlayerTank.id = id;
            System.out.println("Connected to server! and server give me a ID:" + id);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(s != null) {
                try {
                    s.close();
                    s = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        TankNewMSG msg = new TankNewMSG(tankClient.myPlayerTank);
        if (msg == null){
            System.out.println("msg null");

            return;
        }
        send(msg);
        new Thread(new UDPRecvThread()).start();
    }

    public void send(GameMessage msg) {
        msg.sendMsg(ds, IP, GameServer.UDP_PORT);
    }

    private class UDPRecvThread implements Runnable {

        byte[] buf = new byte[1024];

        public void run() {

            while(ds != null){
                DatagramPacket dp = new DatagramPacket(buf, buf.length);
                try {
                    ds.receive(dp);
                    parse(dp);
                    System.out.println("a packet received from server!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void parse(DatagramPacket dp) {
            ByteArrayInputStream bais = new ByteArrayInputStream(buf, 0, dp.getLength());
            DataInputStream dis = new DataInputStream(bais);
            int msgType = 0;
            try {
                msgType = dis.readInt();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GameMessage msg = null;
            switch (msgType) {
                case GameMessage.TANK_NEW_MSG:
                    msg = new TankNewMSG(Player.this.tankClient);
                    msg.parseMsg(dis);
                    break;
                case GameMessage.TANK_MOVE_MSG:
                    msg = new TankMoveMSG(Player.this.tankClient);
                    msg.parseMsg(dis);
                    break;
                case GameMessage.TANK_DEAD_MSG:
                    msg = new TankDeathMSG(Player.this.tankClient);
                    msg.parseMsg(dis);
                    break;
                case GameMessage.TANK_FIRE_MSG:
                    msg = new TankFireMSG(Player.this.tankClient);
                    msg.parseMsg(dis);
                    break;
            }

        }

    }

    public int getUdpPort() {
        return udpPort;
    }

    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }
}



