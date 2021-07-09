package ua.com.alevel.config.configurator;

import ua.com.alevel.config.ApplicationContext;

public interface ObjectConfigurator {

    void configure(Object o, ApplicationContext context);
}
