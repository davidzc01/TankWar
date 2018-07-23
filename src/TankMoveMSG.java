

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by David on 8/11/17.
 */
public class TankMoveMSG implements GameMessage {
    int Type = TANK_MOVE_MSG;
    TankClient tankClient;
    int id;
    int x;
    int y;
    Direction dir;
    Direction ptDir;

    public TankMoveMSG(int id,int x, int y,  Direction dir, Direction ptDir) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.ptDir = ptDir;
    }

    public TankMoveMSG(TankClient tc){
        this.tankClient = tc;
    }
    @Override
    public void sendMsg(DatagramSocket ds, String IP, int udpPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(Type);
            dos.writeInt(id);
            dos.writeInt(x);
            dos.writeInt(y);
            dos.writeInt(dir.ordinal());
            dos.writeInt(ptDir.ordinal());
        }catch (IOException e){
            e.printStackTrace();
        }
        byte[] buf = baos.toByteArray();
        try{
            DatagramPacket dp = new DatagramPacket(buf, buf.length, new InetSocketAddress(IP, udpPort));
            ds.send(dp);
        }catch (SocketException e) {
            e.printStackTrace();
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
            int x = dis.readInt();
            int y = dis.readInt();
            Direction dir = Direction.values()[dis.readInt()];
            Direction ptDir = Direction.values()[dis.readInt()];
            boolean exist = false;
            for(int i=0; i<tankClient.tanks.size(); i++) {
                EnemyTank t = tankClient.tanks.get(i);
                if(t.id == id) {
                    t.x = x;
                    t.y = y;
                    t.dir = dir;
                    t.ptDir = ptDir;
                    exist = true;
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
