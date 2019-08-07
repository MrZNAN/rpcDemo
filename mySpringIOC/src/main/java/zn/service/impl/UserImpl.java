package zn.service.impl;

import zn.ioc.IocResource;
import zn.ioc.IocService;
import zn.service.User;
import zn.service.UserMapper;

public class UserImpl implements User {

    @IocResource
    private UserMapper userMapper;

    @Override
    public void eat(String food) {
        System.out.println("i like eat" + userMapper.findUser(food));
    }
}
