package service;


import zn.ioc.myRpc;

@myRpc
public interface GoodsList {
    String[] list();
}
