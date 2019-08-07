package roles;

public class RemoteService {

    private String id;
    private String host;
    private int post;
    private String serviceName;

    public static RemoteService getDesciber( String id,String desc ){
        RemoteService remoteService = new RemoteService();
        remoteService.setId(id);
        String[] split = desc.split("-");
        remoteService.setServiceName(split[1]);
        String[] hostAndPort = split[0].split(":");
        remoteService.setHost(hostAndPort[0]);
        remoteService.setPost(Integer.parseInt(hostAndPort[1]));
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

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
