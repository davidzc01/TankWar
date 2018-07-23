import java.io.DataInputStream;
import java.net.DatagramSocket;

/**
 * Created by David on 8/4/17.
 */
public interface GameMessage {
    int TANK_NEW_MSG = 1;
    int TANK_MOVE_MSG = 2;
    int TANK_FIRE_MSG = 3;
    int TANK_DEAD_MSG = 4;
    int MISSILE_DEAD_MSG = 5;


    void sendMsg(DatagramSocket ds, String IP, int udpPort);
    void parseMsg(DataInputStream dis);
}
