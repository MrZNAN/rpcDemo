package roles;

import utils.RedisUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class ServiceInitializer  {

    private static Map<String,Object> map = new HashMap<String, Object>(16);

    public static void init(SelfDesc selfDesc) throws Exception{

        List<String> classFullName = new ArrayList<String>();

        Properties properties = new Properties();
        properties.load(new FileInputStream("D:\\project\\rpcDemo\\serviceProvider\\src\\main\\resources\\conf.property"));
        String services = properties.getProperty("service");
        for (String serviceName : services.split(",")) {
            classFullName.add(serviceName);
        }

        for (String fullName : classFullName) {
            String interfaceName = Class.forName(fullName).getInterfaces()[0].getName();
            map.put(interfaceName.substring(interfaceName.lastIndexOf(".")+1),Class.forName(fullName).newInstance());

        }
        //去注册中心注册服务
        RedisUtil.addKeyValue(selfDesc.getId(),selfDesc.getHost()+":"+selfDesc.getPort()+"-"+selfDesc.getServiceName());

    }

    public static Object getServiceImpl(String service){
        return map.get(service);
    }


}
