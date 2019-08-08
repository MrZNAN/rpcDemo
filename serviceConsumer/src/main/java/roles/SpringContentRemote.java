package roles;

import zn.ioc.*;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhangnan
 */
public class SpringContentRemote extends SpringContext {
    public SpringContentRemote(String scanPackage) {
        super();
        initBeanFactory(scanPackage);
    }


    private void initBeanFactory(String scanPackage)  {

        List<Class> interfaces = new ArrayList<>();
        List<Class> clazzes = new ArrayList<>();
        List<Class> rpcClazzes = new ArrayList<>();

        List<Class<?>> classes = ClassUtil.getClasses(scanPackage);
        for (Class<?> aClass : classes) {
            if(aClass.isInterface()){
                if(aClass.getAnnotation(myRpc.class) != null){
                    rpcClazzes.add(aClass);
                }else{
                    interfaces.add(aClass);
                }
            }else{
                clazzes.add(aClass);
            }
        }

        for (Class clazz : clazzes) {
            Class[] myinterfaces = clazz.getInterfaces();
            for (Class myinterface : myinterfaces) {
                for (Class anInterface : interfaces) {
                    if(myinterface.getName().equals(anInterface.getName())){
                        Object bean = null;
                        try {
                            bean = clazz.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Annotation annotation = anInterface.getAnnotation(IocService.class);
                        if(annotation != null){
                            initBeanClass.put(myinterface,bean);
                            initBean.put(SpringContext.toLowerCaseFirstOne(myinterface.getName().substring(myinterface.getName().lastIndexOf(".")+1)),bean);
                        }
                    }
                }
            }
        }

        if(rpcClazzes.size() == 0){
            return;
        }

        for (Class rpcClazz : rpcClazzes) {
            Object bean =  Proxy.newProxyInstance(rpcClazz.getClassLoader(), new Class[]{rpcClazz}
                    , new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            Object obj = new Object();
                            OutputStream outputStream = null;
                            InputStream inputStream = null;
                            ObjectOutputStream obs = null;
                            ObjectInputStream ois = null;
                            Socket socket = null;
                            try {
                                socket = new ServiceFinder().findService();
                                ServiceDescriber classDesc = new ServiceDescriber();
                                classDesc.setServiceName(SpringContext.toLowerCaseFirstOne(rpcClazz.getName().substring(rpcClazz.getName().lastIndexOf(".") + 1)));
                                classDesc.setServiceClass(rpcClazz);
                                classDesc.setMethodName(method.getName());
                                classDesc.setArgs(args);
                                outputStream = socket.getOutputStream();
                                obs = new ObjectOutputStream(outputStream);
                                obs.writeObject(classDesc);
                                inputStream = socket.getInputStream();
                                ois = new ObjectInputStream(inputStream);
                                obj = ois.readObject();
                            }catch (Exception e){
                                e.printStackTrace();
                            }finally {
                                outputStream.close();
                                inputStream.close();
                                obs.close();
                                ois.close();
                                SocketPoll.getSocketPoll().relaseSocket(socket);
                            }
                            return ((ServiceDescriber) obj).getResult();
                        }
                    });

            initBeanClass.put(rpcClazz,bean);
            initBean.put(SpringContext.toLowerCaseFirstOne(rpcClazz.getName().substring(rpcClazz.getName().lastIndexOf(".")+1)),bean);

        }

        for(Map.Entry entry: initBean.entrySet()){
            Object value = entry.getValue();
            try {
                super.initAttribute( value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
