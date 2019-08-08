import roles.HeartBeat;
import roles.SelfDesc;
import roles.ServiceMonitor;
import zn.ioc.SpringContext;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangnan
 */
public class Provider {

    public static void main(String[] args) throws Exception{

        ExecutorService service = Executors.newFixedThreadPool(2);

        SpringContext sc = new SpringContext("service");
        int port = 8888;

        if(args.length == 1){
            port = Integer.parseInt(args[0]);
        }

        SelfDesc selfDesc = new SelfDesc(UUID.randomUUID().toString(),"127.0.0.1",port);
        service.submit(new HeartBeat(selfDesc));

        service.submit(()->{
            //启动服务，监听调用
            ServiceMonitor serviceMonitor = new ServiceMonitor(sc);
            try {
                serviceMonitor.start(selfDesc.getPort());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println(selfDesc+"----已启动");

        while (true){

        }

    }

}
