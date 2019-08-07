package roles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class SelfDesc {
    private String id = "1";
    private String host = "127.0.0.1";
    private int port = 8888 ;
    private String serviceName = "ServiceInterface";


    public SelfDesc() throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream("D:\\project\\rpcDemo\\serviceProvider\\src\\main\\resources\\conf.property"));
        this.id = properties.getProperty("id");
        this.host = properties.getProperty("host");
        this.port = Integer.parseInt( properties.getProperty("port"));
        this.serviceName =  properties.getProperty("serviceName");
    }

    public String getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }


    public String getServiceName() {
        return serviceName;
    }


}
