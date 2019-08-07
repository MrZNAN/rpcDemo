import roles.ServiceProxy;
import service.GoodsList;

/**
 * @author zhangnan
 */
public class Consumer {

    public static void main(String[] args) throws Exception{

        GoodsList person = ServiceProxy.getService(GoodsList.class);
        String[] list = person.list();
        for (String s : list) {
            System.out.println(s);
        }
    }

}
