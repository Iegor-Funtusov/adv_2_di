package ua.com.alevel.config;

import ua.com.alevel.config.annotation.BeanClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationContext {

    private final Set<Class> daoClasses;
    private final Set<Class> serviceClasses;
    private final Set<Class> controllerClasses;
    private final Map<Class, Object> daoMap = new HashMap<>();
    private final Map<Class, Object> serviceMap = new HashMap<>();
    private final Map<Class, Object> controllerMap = new HashMap<>();
    private final ApplicationSearcher applicationSearcher;
    private ObjectFactory objectFactory;

    public ApplicationContext(Set<Class> interfaces, String packageToScan) {
        this.daoClasses = interfaces.stream().filter(i -> i.getSimpleName().endsWith("Dao")).collect(Collectors.toSet());
        this.serviceClasses = interfaces.stream().filter(i -> i.getSimpleName().endsWith("Service")).collect(Collectors.toSet());
        this.controllerClasses = interfaces.stream().filter(i -> i.getSimpleName().endsWith("Controller")).collect(Collectors.toSet());
        this.applicationSearcher = new ApplicationSearcher(packageToScan);
    }

    public void applicationRun() {
        initDaoClasses();
        initServiceClasses();
        initControllerClasses();
    }

    public ApplicationSearcher getApplicationSearcher() {
        return applicationSearcher;
    }

    public Map<Class, Object> getControllerMap() {
        return controllerMap;
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    private void initDaoClasses() {
        for (Class daoClass : this.daoClasses) {
            getObjectImpl(daoClass);
        }
    }

    private void initServiceClasses() {
        for (Class serviceClass : this.serviceClasses) {
            getObjectImpl(serviceClass);
        }
    }

    private void initControllerClasses() {
        for (Class controllerClass : this.controllerClasses) {
            getObjectImpl(controllerClass);
        }
    }

    public <I> I getObjectImpl(Class<I> type) {
        if (type.getSimpleName().contains("Dao")) {
            return getObjectImpl(type, daoMap);
        }
        if (type.getSimpleName().contains("Service")) {
            return getObjectImpl(type, serviceMap);
        }
        if (type.getSimpleName().contains("Controller")) {
            return getObjectImpl(type, controllerMap);
        }
        return null;
    }

    private <I> I getObjectImpl(Class<I> type, Map<Class, Object> implClasses) {
        Class<? extends I> impl = type;
        if (implClasses.containsKey(type)) {
            return (I) implClasses.get(type);
        }
        if (type.isInterface()) {
            impl = this.applicationSearcher.getImplementation(type);
        }
        I i = objectFactory.createObject(impl);
        if (impl.isAnnotationPresent(BeanClass.class)) {
            implClasses.put(type, i);
        }
        return i;
    }
}
