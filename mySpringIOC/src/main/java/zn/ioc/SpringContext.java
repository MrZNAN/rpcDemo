package zn.ioc;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringContext {

    private Socket socket;

    private ConcurrentHashMap<String ,Object> initBean = new ConcurrentHashMap<String, Object>();
    private ConcurrentHashMap<Class ,Object> initBeanClass = new ConcurrentHashMap<Class, Object>();

    private String scanPackage;

    public SpringContext(String scanPackage,Socket socket) {
        this.scanPackage = scanPackage;
        this.socket = socket;
        initBeanFactory(scanPackage);
    }

    public SpringContext(String scanPackage) {
        this.scanPackage = scanPackage;
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
                            try {
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
                initAttribute( value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * 初始化依赖的属性
     * @param object
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private void initAttribute(Object object)throws Exception{
        //获取object的所有类型
        Class<? extends Object> classinfo = object.getClass();
        //获取所有的属性字段
        Field[] fields = classinfo.getDeclaredFields();
        //遍历所有字段
        for(Field field : fields){
            //查找字段上有依赖的注解
            boolean falg = field.isAnnotationPresent(IocResource.class);
            if (falg){
                IocResource iocResource = field.getAnnotation(IocResource.class);
                if (iocResource!=null){
                    //获取属性的beanid
                    String beanId = field.getName();
                    //获取对应的object
                    Object attrObject = getBean(beanId);
                    if (attrObject!=null){
                        //访问私有字段
                        field.setAccessible(true);
                        //赋值
                        field.set(object,attrObject);
                        continue;
                    }
                }
            }
        }
    }


    public Object getBean(String beanName) throws Exception{

        Object bean = initBean.get(beanName);
        if (bean == null ) {
            throw new Exception("no found anything bean is useding initial..");
        }
        return bean;

    }

    public Object getBean(Class beanClass) throws Exception{
        Object bean = initBeanClass.get(beanClass);
        if (bean == null ) {
            throw new Exception("no found anything bean is useding initial..");
        }
        return bean;
    }


    /**
     * 首字母转换为小写
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))){
            return s;
        }
        else{
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }


}
