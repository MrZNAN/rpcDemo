package roles;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class SelfDesc {
    private String id;
    private String host ;
    private int port ;
    private String serviceName ;


    public SelfDesc() throws Exception {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("conf.property"));
        this.id = properties.getProperty("id");
        this.host = properties.getProperty("host");
        this.port = Integer.parseInt( properties.getProperty("port"));
    }

    public SelfDesc(String id) throws Exception {
        Properties properties = new Properties();
        properties.load(this.getClass().getClassLoader().getResourceAsStream("conf.property"));
        this.id = id;
        this.host = properties.getProperty("host");
        this.port = Integer.parseInt( properties.getProperty("port"));
    }

    public SelfDesc(String id,String host,int port) throws Exception {
        this.id = id;
        this.host = host;
        this.port = port;

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

    @Override
    public String toString() {
        return "SelfDesc{" +
                "id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
