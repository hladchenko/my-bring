package com.hladchenko.bring.context;

import com.hladchenko.bring.demo.BringComponent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

public class BringContext {

  private static final Map<Class<?>, Object> registry = new ConcurrentHashMap<>();

  public static BringContext scan(Class<?> clazz) {
    String packageName = clazz.getPackageName();
    Set<Class<?>> typesAnnotatedWith = new Reflections(packageName,
        Scanners.TypesAnnotated).getTypesAnnotatedWith(
        BringComponent.class);
    for (Class<?> type : typesAnnotatedWith) {
      addTypeToRegistry(type);
    }

    return new BringContext();
  }

  private static void addTypeToRegistry(Class<?> type) {
    try {
      Constructor<?> declaredConstructor = type.getDeclaredConstructors()[0];
      Class<?>[] parameterTypes = declaredConstructor.getParameterTypes();
      Object[] parameters = new Object[parameterTypes.length];
      int index = 0;
      for (Class<?> clazz : parameterTypes) {
        if (!registry.containsKey(clazz)) {
          addTypeToRegistry(clazz);
        }
        parameters[index++] = registry.get(clazz);
      }

      registry.put(type, declaredConstructor.newInstance(parameters));
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  public <T> T getBean(Class<T> clazz) {
    return clazz.cast(registry.get(clazz));
  }
}
