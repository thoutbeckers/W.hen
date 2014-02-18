package houtbecke.rs.when;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ClassMethodCache {

    String methodName;
    Object object;
    Method[] methods;

    Map<Class, Method> cachedMethods = new HashMap<Class, Method>(1);

    public ClassMethodCache(Object object, String methodName) {
        this.object = object;
        this.methodName = methodName;
    }

    public Method getMethodForObject(Object thing) {
        Class thingClass = thing.getClass();

        if (cachedMethods.keySet().contains(thingClass))
            return cachedMethods.get(thingClass);

        Method method = cachedMethods.get(thingClass);

        if (method == null) {
            if (methods == null)
                methods = object.getClass().getMethods();
            for(Method m: methods) {
                if (!m.getName().equals(methodName))
                    continue;
                Class[] parameters = m.getParameterTypes();
                if (parameters.length > 1 || parameters.length < 1 || parameters[0] == Object.class)
                    continue;
                if (parameters[0].isInstance(thing)) {
                    method = m;
                    break;
                }
            }
        }
        cachedMethods.put(thingClass, method);

        return method;
    }
}
