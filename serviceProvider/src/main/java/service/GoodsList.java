package service;

import zn.ioc.IocService;

@IocService
public interface GoodsList {
    String[] list();
}
