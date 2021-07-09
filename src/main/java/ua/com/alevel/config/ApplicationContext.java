package ua.com.alevel.config;

import ua.com.alevel.config.annotation.BeanClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ApplicationContext {

    private final Set<Class> interfaces;
    private final Map<Class, Object> mapInterfaceAndImplementation = new HashMap<>();
    private final ApplicationSearcher applicationSearcher;
    private ObjectFactory objectFactory;

    public ApplicationContext(Set<Class> interfaces, String packageToScan) {
        this.interfaces = initOnlyInterfacesClasses(interfaces);
        this.applicationSearcher = new ApplicationSearcher(packageToScan);
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public void applicationRun() {
        initMapInterfaceAndImplementation();
    }

    public ApplicationSearcher getApplicationSearcher() {
        return applicationSearcher;
    }

    public  <I> I getObjectImpl(Class<I> type) {
        Class<? extends I> impl = type;
        if (mapInterfaceAndImplementation.containsKey(type)) {
            return (I) mapInterfaceAndImplementation.get(type);
        }
        if (type.isInterface()) {
            impl = this.applicationSearcher.getImplementation(type);
        }
        I i = objectFactory.createObject(impl);
        if (impl.isAnnotationPresent(BeanClass.class)) {
            mapInterfaceAndImplementation.put(type, i);
        }
        return i;
    }

    public Map<Class, Object> getMapInterfaceAndImplementation() {
        return mapInterfaceAndImplementation;
    }

    private void initMapInterfaceAndImplementation() {
        for (Class serviceInterface : this.interfaces) {
            getObjectImpl(serviceInterface);
        }
    }

    private Set<Class> initOnlyInterfacesClasses(Set<Class> interfaces) {
        return interfaces
                .stream()
                .filter(this::isServiceClass)
                .collect(Collectors.toSet());
    }

    private boolean isServiceClass(Class currentInterface) {
        return currentInterface.getSimpleName().endsWith("Dao") ||
                currentInterface.getSimpleName().endsWith("Service") ||
                currentInterface.getSimpleName().endsWith("Controller");
    }
}
