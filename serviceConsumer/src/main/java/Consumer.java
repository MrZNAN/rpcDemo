import roles.SpringContentRemote;
import service.Check;
import zn.ioc.SpringContext;

/**
 * @author zhangnan
 */
public class Consumer {

    public static void main(String[] args) throws Exception{

        SpringContext sc = new SpringContentRemote("service");

        for(int i = 0 ; i< 200 ; i++){
            Thread.sleep(1000);
            Check check = (Check)sc.getBean(Check.class);
            check.check();
        }
    }

}
