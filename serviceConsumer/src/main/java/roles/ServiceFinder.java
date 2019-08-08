package roles;

import utils.RedisUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class ServiceFinder {

    public static String RPC_DEMO = "rpcDemo";

    public Socket findService() {
        int reTryTimes = 0;
        Socket socket = SocketPoll.getSocketPoll().getSocket();
        try {
            socket = tryConnect(socket);
        } catch (IOException e) {
            reTryTimes++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            if(reTryTimes <= 5){
                socket = findService();
            }else{
                throw new RuntimeException("经过多次重试，仍未与服务器链接！");
            }
        }
        return socket;

    }

    private Socket tryConnect(Socket socket) throws IOException {

        List<String> allKeys = RedisUtil.getAllKeys(ServiceFinder.RPC_DEMO + "*");
        RemoteService desciber = RemoteService.getDesciber(UUID.randomUUID().toString(), allKeys.get(new Random().nextInt(allKeys.size())));
        socket.connect(new InetSocketAddress(desciber.getHost(),desciber.getPort()));
        return socket;
    }
}
