package service.impl;

import service.Check;
import service.GoodsList;
import zn.ioc.IocResource;

public class CheckImpl implements Check {

    @IocResource
    private GoodsList goodsList;

    public void check() {
        String[] list = goodsList.list();
        for (String s : list) {
            System.out.println("----------------");
            System.out.println(s);
            System.out.println("----------------");
        }
    }
}
