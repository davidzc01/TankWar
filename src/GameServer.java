import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 8/2/17.
 */
// 坦克大战server实体类
public class GameServer {
    private  static int clientID = 100;
    public static final int TCP_PORT = 7777;
    public static final int UDP_PORT = 8888;
    List<Client> clients = new ArrayList<>();

    public void start(){
        new Thread(new UDPThreads()).start();
        ServerSocket sSocket = null;
        try{
            sSocket = new ServerSocket(TCP_PORT);
        }catch (IOException e){
            e.printStackTrace();
        }
        while(true){
            Socket socket = null;
            try {
                socket = sSocket.accept();
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String ip = socket.getInetAddress().getHostAddress();
                int uport = dis.readInt();
                Client client = new Client(ip,uport);
                clients.add(client);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeInt(clientID);
                clientID++;
                System.out.println("A Client Connect! Addr- " + socket.getInetAddress() + ":" + socket.getPort() + "----UDP Port:" + uport);

            }catch (IOException e){
                e.printStackTrace();
            }finally {
                if (socket != null) {
                    try {
                        socket.close();
                    socket = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args){
        new GameServer().start();
    }

    class UDPThreads implements Runnable{
        byte[] buff = new byte[1024];
        @Override
        public void run() {
            DatagramSocket datagramSocket = null;
            try{
                datagramSocket = new DatagramSocket(UDP_PORT);
            }catch (SocketException e){
                e.printStackTrace();
            }
            while (datagramSocket != null){
                DatagramPacket dPacket = new DatagramPacket(buff,buff.length);
                try{
                    datagramSocket.receive(dPacket);
                    for(int i=0; i<clients.size(); i++) {
                        Client c = clients.get(i);
                        dPacket.setSocketAddress(new InetSocketAddress(c.IP, c.uport));
                        datagramSocket.send(dPacket);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }


    class Client{
        String IP;
        int uport;
        public Client(String IP, int uPort){
            this.IP = IP;
            this.uport = uPort;
        }
    }
}
