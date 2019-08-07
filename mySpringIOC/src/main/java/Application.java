import zn.ioc.ComponetScanner;
import zn.ioc.SpringContext;
import zn.service.User;

public class Application {
    public static void main(String[] args) throws Exception {
        SpringContext context = new SpringContext("zn.service");
        User userService = (User) context.getBean(User.class);
        userService.eat("aaa");


    }
}
