package ua.com.alevel.config.configurator;

import ua.com.alevel.config.ApplicationContext;
import ua.com.alevel.config.annotation.InjectBean;

import java.lang.reflect.Field;

public class BeanClassAnnotationObjectConfigurator implements ObjectConfigurator {

    @Override
    public void configure(Object o, ApplicationContext context) {
        Field[] declaredFields = o.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(InjectBean.class)) {
                declaredField.setAccessible(true);
                Object impl = context.getObjectImpl(declaredField.getType());
                try {
                    declaredField.set(o, impl);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("problem from initial field");
                }
            }
        }
    }
}
