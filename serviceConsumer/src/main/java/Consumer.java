import roles.RemoteService;
import roles.ServiceFinder;
import service.Check;
import zn.ioc.SpringContext;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zhangnan
 */
public class Consumer {

    public static void main(String[] args) throws Exception{

        Socket socket = ServiceFinder.findService();
        SpringContext sc = new SpringContext("service",socket);

        Check check = (Check)sc.getBean(Check.class);
        check.check();
    }

}
