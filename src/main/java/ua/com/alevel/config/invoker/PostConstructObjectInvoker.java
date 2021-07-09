package ua.com.alevel.config.invoker;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PostConstructObjectInvoker implements ObjectInvoker {

    @Override
    public <I> void invoke(Class<I> type, I i) {
        for (Method method : type.getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.setAccessible(true);
                try {
                    method.invoke(i);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
