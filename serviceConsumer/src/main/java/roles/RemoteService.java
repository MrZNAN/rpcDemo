package roles;

public class RemoteService {

    private String id;
    private String host;
    private int port;
    private String serviceName;

    public static RemoteService getDesciber( String id,String desc ){
        RemoteService remoteService = new RemoteService();
        remoteService.setId(id);
        String[] hostAndPort = desc.split(":");
        remoteService.setHost(hostAndPort[0]);
        remoteService.setPort(Integer.parseInt(hostAndPort[1]));
        return remoteService;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
