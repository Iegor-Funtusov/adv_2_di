package ua.com.alevel.config;

import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ApplicationStarter {

    private final Map<Class, Object> controllerMap;

    public ApplicationStarter(Map<Class, Object> controllerMap) {
        this.controllerMap = controllerMap;
    }

    public void start() {
        controllerMap.forEach((key, value) -> {
            for (Method method : value.getClass().getMethods()) {
                if (method.getName().equals("start")) {
                    try {
                        method.invoke(value);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
