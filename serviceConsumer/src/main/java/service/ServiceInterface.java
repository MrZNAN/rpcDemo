package service;

import zn.ioc.myRpc;

/**
 * @author zhangnan
 */
@myRpc
public interface ServiceInterface {
    /**
     *
     * @param foodName
     * @return
     */
    String eat(String foodName);
}
