package roles;

import utils.RedisUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class ServiceFinder {

    private static String RPC_DEMO = "rpcDemo";

    public Socket findService() {
        int reTryTimes = 0;
        Socket socket;
        try {
            socket = tryConnect();
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

    private Socket tryConnect() throws IOException {
        List<String> values = RedisUtil.getAllKeys(ServiceFinder.RPC_DEMO + "*");

        int id = new Random().nextInt(values.size());
        String hostAndPort = values.get(id);
        RemoteService desciber = RemoteService.getDesciber(UUID.randomUUID().toString(), hostAndPort);
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(desciber.getHost(),desciber.getPort()));
        return socket;
    }

}
