package ua.com.alevel.config.invoker;

public interface ObjectInvoker {

    <I> void invoke(Class<I> type, I i);
}
