package roles;

import utils.RedisUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;

public class ServiceFinder {

    private static String RPC_DEMO = "rpcDemo";

    public Socket findService() {
        Socket socket;
        try {
            socket = tryConnect();
        } catch (IOException e) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            socket = findService();
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
