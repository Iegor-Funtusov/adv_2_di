package ua.com.alevel.config;

import ua.com.alevel.util.ClassLoaderUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Application {

    public static void start(Class mainClass) {
        Package mainPackage = mainClass.getPackage();
        String basePackage = mainPackage.getName();
        try {
            List<Class> allClasses = ClassLoaderUtil.getClasses(basePackage);
            Set<Class> interfaces = allClasses.stream().filter(Class::isInterface).collect(Collectors.toSet());
            ApplicationContext context = new ApplicationContext(interfaces, basePackage);
            ObjectFactory objectFactory = new ObjectFactory(context);
            context.setObjectFactory(objectFactory);
            context.applicationRun();
            Map<Class, Object> mapInterfaceAndImplementation = context.getMapInterfaceAndImplementation();
            Collection<Object> controllers = mapInterfaceAndImplementation.values();
            ApplicationStarter applicationStarter = new ApplicationStarter(controllers);
            applicationStarter.start();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
