package service.impl;

import service.GoodsList;

public class GoodListImpl implements GoodsList {
    public String[] list() {
        return new  String[]{"apple","banana"};
    }
}
