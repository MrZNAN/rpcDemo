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

        for(int i = 0 ; i< 200 ; i++){

            Thread.sleep(1000);

            Socket socket = new ServiceFinder().findService();
            SpringContext sc = new SpringContext("service",socket);

            Check check = (Check)sc.getBean(Check.class);
            check.check();
        }


    }

}
