package roles;

import utils.RedisUtil;

public class ServiceFinder {

    public static RemoteService findService() throws Exception {
        String value = RedisUtil.getValueBykey("1");
        return RemoteService.getDesciber("1",value);
    }
}
