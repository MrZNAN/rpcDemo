package roles;

import utils.RedisUtil;

import java.net.InetSocketAddress;
import java.net.Socket;

public class ServiceFinder {

    public static Socket findService() throws Exception {
        String value = RedisUtil.getValueBykey("1");
        RemoteService desciber = RemoteService.getDesciber("1", value);
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(desciber.getHost(),desciber.getPort()));
        return socket;

    }
}
