package ua.com.alevel.config;

import ua.com.alevel.util.ClassLoaderUtil;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Application {

    public static void start(Class mainClass) {
        Package mainPackage = mainClass.getPackage();
        String packageName = mainPackage.getName();
        try {
            List<Class> classes = ClassLoaderUtil.getClasses(packageName);
            Set<Class> interfaces = classes.stream().filter(Class::isInterface).collect(Collectors.toSet());
            ApplicationContext context = new ApplicationContext(interfaces, packageName);
            ObjectFactory objectFactory = new ObjectFactory(context);
            context.setObjectFactory(objectFactory);
            context.applicationRun();
            ApplicationStarter applicationStarter = new ApplicationStarter(context.getControllerMap());
            applicationStarter.start();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }
}
