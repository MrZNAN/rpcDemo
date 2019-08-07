package roles;

import utils.RedisUtil;

public class ServiceInitializer  {

    public static void init(SelfDesc selfDesc) throws Exception{

        RedisUtil.addKeyValue(selfDesc.getId(),selfDesc.getHost()+":"+selfDesc.getPort()+"-"+selfDesc.getServiceName());

    }

}
