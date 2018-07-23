

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * Created by David on 8/4/17.
 */
public class TankNewMSG implements GameMessage {
    int Type = TANK_NEW_MSG;
    PlayerTank tank;
    TankClient tankClient;

    public TankNewMSG(PlayerTank tank){
        this.tank = tank;
    }

    public TankNewMSG(TankClient tc){
        this.tankClient = tc;
    }

    @Override
    public void sendMsg(DatagramSocket ds, String IP, int udpPort) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try{
            dos.writeInt(Type);
            dos.writeInt(tank.id);
            dos.writeInt(tank.x);
            dos.writeInt(tank.y);
            dos.writeInt(tank.dir.ordinal());
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
//System.out.println("id:" + id + "-x:" + x + "-y:" + y + "-dir:" + dir + "-good:" + good);
            boolean exist = false;
            for(int i=0; i<tankClient.tanks.size(); i++) {
                Tank t = tankClient.tanks.get(i);
                if(t.id == id) {
                    exist = true;
                    break;
                }
            }

            if(!exist) {
                TankNewMSG tnMsg = new TankNewMSG(tankClient.myPlayerTank);
                tankClient.player.send(tnMsg);

                EnemyPlayer t = new EnemyPlayer(x, y, dir, tankClient);
                t.id = id;
                tankClient.tanks.add(t);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

