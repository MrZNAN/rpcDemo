package zn.ioc;

import java.lang.annotation.*;

/**
 * @author zhangnan
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IocService {
    String name() default "";
}