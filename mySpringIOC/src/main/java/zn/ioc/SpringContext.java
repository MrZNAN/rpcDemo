package zn.ioc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SpringContext {

    protected ConcurrentHashMap<String, Object> initBean = new ConcurrentHashMap<String, Object>();
    protected ConcurrentHashMap<Class, Object> initBeanClass = new ConcurrentHashMap<Class, Object>();
    protected String scanPackage;

    public SpringContext() {
    }

    public SpringContext(String scanPackage) {
        this.scanPackage = scanPackage;
        initBeanFactory(scanPackage);
    }

    private void initBeanFactory(String scanPackage) {

        List<Class> interfaces = new ArrayList<>();
        List<Class> clazzes = new ArrayList<>();

        List<Class<?>> classes = ClassUtil.getClasses(scanPackage);
        for (Class<?> aClass : classes) {
            if (aClass.isInterface()) {
                interfaces.add(aClass);
            } else {
                clazzes.add(aClass);
            }
        }


        for (Class clazz : clazzes) {
            Class[] myinterfaces = clazz.getInterfaces();
            for (Class myinterface : myinterfaces) {
                for (Class anInterface : interfaces) {
                    if (myinterface.getName().equals(anInterface.getName())) {
                        Object bean = null;
                        try {
                            bean = clazz.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Annotation annotation = anInterface.getAnnotation(IocService.class);
                        if (annotation != null) {
                            initBeanClass.put(myinterface, bean);
                            initBean.put(SpringContext.toLowerCaseFirstOne(myinterface.getName().substring(myinterface.getName().lastIndexOf(".") + 1)), bean);
                        }
                    }
                }
            }
        }


        for (Map.Entry entry : initBean.entrySet()) {
            Object value = entry.getValue();
            try {
                initAttribute(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 初始化依赖的属性
     *
     * @param object
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    protected void initAttribute(Object object) throws Exception {
        //获取object的所有类型
        Class<? extends Object> classinfo = object.getClass();
        //获取所有的属性字段
        Field[] fields = classinfo.getDeclaredFields();
        //遍历所有字段
        for (Field field : fields) {
            //查找字段上有依赖的注解
            boolean falg = field.isAnnotationPresent(IocResource.class);
            if (falg) {
                IocResource iocResource = field.getAnnotation(IocResource.class);
                if (iocResource != null) {
                    //获取属性的beanid
                    String beanId = field.getName();
                    //获取对应的object
                    Object attrObject = getBean(beanId);
                    if (attrObject != null) {
                        //访问私有字段
                        field.setAccessible(true);
                        //赋值
                        field.set(object, attrObject);
                        continue;
                    }
                }
            }
        }
    }

    public Object getBean(String beanName) throws Exception {

        Object bean = initBean.get(beanName);
        if (bean == null) {
            throw new Exception("no found anything bean is useding initial..");
        }
        return bean;

    }

    public Object getBean(Class beanClass) throws Exception {
        Object bean = initBeanClass.get(beanClass);
        if (bean == null) {
            throw new Exception("no found anything bean is useding initial..");
        }
        return bean;
    }

    /**
     * 首字母转换为小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
