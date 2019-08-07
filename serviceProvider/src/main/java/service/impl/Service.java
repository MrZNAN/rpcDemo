package service.impl;

import service.ServiceInterface;

/**
 * @author zhangnan
 */
public class Service implements ServiceInterface {
    public String eat(String foodName) {
        return "i like "+foodName;
    }
}
