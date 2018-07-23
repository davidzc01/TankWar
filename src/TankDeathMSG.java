

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * Created by David on 8/14/17.
 */
public class TankDeathMSG implements GameMessage {
    int Type = TANK_DEAD_MSG;
    Tank tank;
    TankClient tankClient;

    public TankDeathMSG(Tank tank){
        this.tank = tank;
    }

    public TankDeathMSG(TankClient tc){
        this.tankClient = tc;
    }

    @Override
    public void sendMsg(DatagramSocket ds, String IP, int udpPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(Type);
            dos.writeInt(tank.id);
        }catch (IOException e){
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, udpPort));
            ds.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void parseMsg(DataInputStream dis) {
        try {
            int id = dis.readInt();
            if(tankClient.myPlayerTank.id == id) {
                return;
            }
//System.out.println("id:" + id + "-x:" + x + "-y:" + y + "-dir:" + dir + "-good:" + good);
            boolean exist = false;
            for(int i=0; i<tankClient.tanks.size(); i++) {
                Tank t = tankClient.tanks.get(i);
                if(t.id == id) {
                    t.setLive(false);
                    tankClient.tanks.remove(t);
                    exist = true;
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
