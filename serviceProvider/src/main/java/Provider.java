import roles.SelfDesc;
import roles.ServiceInitializer;
import roles.ServiceMonitor;
import utils.RedisUtil;
import zn.ioc.SpringContext;

/**
 * @author zhangnan
 */
public class Provider {

    public static void main(String[] args) throws Exception{

        SpringContext sc = new SpringContext("service");

        SelfDesc selfDesc = new SelfDesc();
        ServiceInitializer.init(new SelfDesc());

        //启动服务，监听调用
        ServiceMonitor serviceMonitor = new ServiceMonitor(sc);
        serviceMonitor.start(selfDesc.getPort());

    }

}
