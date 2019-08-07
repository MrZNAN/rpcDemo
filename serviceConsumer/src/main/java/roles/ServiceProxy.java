package roles;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ServiceProxy {

    @SuppressWarnings("unchecked")
    public static <T> T getService(final Class clazz) throws Exception {
        //寻找服务
        final RemoteService service = ServiceFinder.findService();

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}
                , new InvocationHandler() {
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(service.getHost(), service.getPost()));
                        ServiceDescriber classDesc = new ServiceDescriber();
                        classDesc.setServiceName(clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1));
                        classDesc.setMethodName(method.getName());
                        classDesc.setArgs(args);
                        OutputStream outputStream = socket.getOutputStream();
                        ObjectOutputStream obs = new ObjectOutputStream(outputStream);
                        obs.writeObject(classDesc);
                        InputStream inputStream = socket.getInputStream();
                        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                        Object obj = objectInputStream.readObject();
                        return ((ServiceDescriber) obj).getResult();
                    }
                });
    }

}
