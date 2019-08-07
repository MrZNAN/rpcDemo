package zn.service.impl;

import zn.service.UserMapper;

public class UserMapperImpl implements UserMapper {
    @Override
    public String findUser(String id) {
        return "{ id :"+id+" - name: zhangsan  }";
    }
}
