package zn.service;

import zn.ioc.IocService;

@IocService
public interface UserMapper {
    String findUser(String id);

}
