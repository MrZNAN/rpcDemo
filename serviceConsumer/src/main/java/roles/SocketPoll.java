package roles;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SocketPoll {

    private static List<Socket> socketsPoll = new ArrayList<>();
    private int POOL_SIZE = 1;
    private int tyrTime = 0;

    private static SocketPoll socketPoll;

    public static SocketPoll getSocketPoll() {
        if (socketPoll == null) {
            return new SocketPoll();
        } else {
            return socketPoll;
        }
    }

    private SocketPoll() {
        initPool();
    }

    public Socket getSocket() {
        if(size() > 0){
            int id = new Random().nextInt(size());
            Socket socket = socketsPoll.get(id);
            socketsPoll.remove(socket);
            return socket;
        }else{
            tyrTime ++;
            if(tyrTime > 5){
                throw new RuntimeException("socket池未有释放足够socket");
            }
            System.out.println("----------迟内剩余socket数量不足，请等待。。。-----------");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return getSocket();
        }

    }

    public void relaseSocket(Socket socket) {
        socketsPoll.add(socket);
    }

    private void initPool() {
        for (int i = 0 ; i< POOL_SIZE ; i++) {
            Socket socket = new Socket();
            socketsPoll.add(socket);
        }
    }

    private int size() {
        return socketsPoll.size();
    }

}
