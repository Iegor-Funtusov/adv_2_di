package ua.com.alevel.config;

import org.reflections.Reflections;

import java.util.Set;

public class ApplicationSearcher {

    private final Reflections scanner;

    public ApplicationSearcher(String packageToScan) {
        this.scanner = new Reflections(packageToScan);
    }

    public <I> Class<? extends I> getImplementation(Class<I> type) {
        Set<Class<? extends I>> children = scanner.getSubTypesOf(type);
        for (Class<? extends I> child : children) {
            if (!child.isAnnotationPresent(Deprecated.class)) {
                return child;
            }
        }
        throw new RuntimeException("impl not found");
    }

    public Reflections getScanner() {
        return scanner;
    }
}
