package ua.com.alevel.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ClassLoaderUtil {

    private static final String CLASS_TYPE = ".class";
    private static final String DOT_STRING = ".";
    private static final char DOT_CHAR = '.';
    private static final char SLASH_CHAR = '/';

    public static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace(DOT_CHAR, SLASH_CHAR);
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(DOT_STRING);
                classes.addAll(findClasses(file, packageName + DOT_STRING + file.getName()));
            } else if (file.getName().endsWith(CLASS_TYPE)) {
                classes.add(Class.forName(packageName + DOT_STRING + file.getName().substring(0, file.getName().length() - CLASS_TYPE.length())));
            }
        }
        return classes;
    }
}
