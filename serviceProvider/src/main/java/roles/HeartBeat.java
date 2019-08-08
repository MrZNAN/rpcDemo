package roles;

import utils.RedisUtil;

public class HeartBeat implements Runnable {

    public static String RPC_DEMO = "rpcDemo_";

    private SelfDesc selfDesc;

    public HeartBeat(SelfDesc selfDesc){
        this.selfDesc = selfDesc;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                RedisUtil.addKeyValue(HeartBeat.RPC_DEMO + selfDesc.getId(),selfDesc.getHost()+":"+selfDesc.getPort());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
