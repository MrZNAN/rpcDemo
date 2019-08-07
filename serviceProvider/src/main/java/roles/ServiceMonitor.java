package roles;

import zn.ioc.ServiceDescriber;
import zn.ioc.SpringContext;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangnan
 */
public class ServiceMonitor  {

    private SpringContext sc;

    public ServiceMonitor(SpringContext sc){
        this.sc =sc;
    }

    public ServiceMonitor(){
        this.sc = new SpringContext("service");
    }

    public void start(int port) throws Exception {
        ServerSocket ss = new ServerSocket(port);
        while (true){

            final Socket accept = ss.accept();
            final InputStream inputStream = accept.getInputStream();
            ObjectInputStream obs = new ObjectInputStream(inputStream);
            final ServiceDescriber serviceDescriber = (ServiceDescriber) obs.readObject();

            final  Object obj = sc.getBean(serviceDescriber.getServiceClass());
            Object resoutObj = Proxy.newProxyInstance(
                    obj.getClass().getClassLoader(),
                    obj.getClass().getInterfaces(),
                    new InvocationHandler() {
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            if(method.getName().toLowerCase().equals(serviceDescriber.getMethodName().toLowerCase())){
                                System.out.println("--"+accept.getInetAddress()+accept.getPort()+"-调用了我的【"+method.getName()+"】方法--");
                                return method.invoke(obj, args);
                            }else{
                                return null;
                            }
                        }
                    });

            Method[] methods = resoutObj.getClass().getMethods();
            for (Method method : methods) {
                if(method.getName().equals(serviceDescriber.getMethodName())){
                    Object invoke = method.invoke(resoutObj, serviceDescriber.getArgs());
                    serviceDescriber.setResult(invoke);
                }
            }
            OutputStream outputStream = accept.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(serviceDescriber);
            objectOutputStream.close();
        }
    }

}
