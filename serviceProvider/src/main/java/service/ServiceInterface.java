package service;

import zn.ioc.IocService;

/**
 * @author zhangnan
 */
@IocService
public interface ServiceInterface {
    /**
     *
     * @param foodName
     * @return
     */
    String eat(String foodName);
}
