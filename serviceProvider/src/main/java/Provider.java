import roles.SelfDesc;
import roles.ServiceInitializer;
import roles.ServiceMonitor;
import utils.RedisUtil;

/**
 * @author zhangnan
 */
public class Provider {

    public static void main(String[] args) throws Exception{

        SelfDesc selfDesc = new SelfDesc();
        ServiceInitializer.init(selfDesc);

        //启动服务，监听调用
        ServiceMonitor serviceMonitor = new ServiceMonitor();
        serviceMonitor.start(selfDesc.getPort());

    }

}
