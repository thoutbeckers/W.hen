package houtbecke.rs.when;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class magically tries to put the things passed to the act method to an act method you define.
 *
 * You can only declare one act method. Your method will only be called if a thing is found for each
 * of the parameters you defined. This can include a vararg or single dimensional array as the last argument.
 *
 */
public class TypedAct implements Act {

    private static class ParameterClasses {

        Class[] classes;
        private ParameterClasses(Object[] things) {
            this.classes = new Class[things.length];
            for (int i = 0; i < things.length; i++)
                classes[i] = things[i] == null ? null: things[i].getClass();
        }

        public boolean equals(Object o) {
            return Arrays.equals(((ParameterClasses)o).classes, classes);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(classes);
        }
    }

    private void nullMethod() {}

    final private Method nullMethod;

    public TypedAct() {
        try {
            nullMethod = TypedAct.class.getDeclaredMethod("nullMethod");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("nullMethod on TypedAct not found. Perhaps you are using proguard renaming on Act classes?", e);
        }
    }


    Map<ParameterClasses, Method> actCache = new HashMap<ParameterClasses, Method>();

    private Class getArrayType(Class array) {
        if (!array.isArray()) return null;
        try {
            String lastTypeClassName = array.getName();
            String name = lastTypeClassName.substring(2, lastTypeClassName.length() - 1);
            return Class.forName(name);
        } catch (Exception ignored) {
        }
        return null;
    }

    @Override
    public void act(Object... things) {
        if (things == null || things.length == 0) {
            defaultAct(things);
            return;
        }

        ParameterClasses parameterClasses = new ParameterClasses(things);

        Method cachedMethod = actCache.get(parameterClasses);
        Object[] parameters = null;

        if (cachedMethod == null) {
            for (Method method : getClass().getMethods()) {
                if (method.getName().equals("act")) {
                    Class[] types = method.getParameterTypes();
                    // check to make sure it's not this method
                    if (types.length == 1 && getArrayType(types[0]) == Object.class)
                        continue;

                   Object[] possibleParameters = tryMethod(method, things);
                    if (parameters == null && possibleParameters != null || possibleParameters != null && possibleParameters.length > parameters.length) {
                        cachedMethod = method;
                        parameters = possibleParameters;
                    }
                }
            }
            if (cachedMethod == null)
                cachedMethod = nullMethod;

            actCache.put(parameterClasses, cachedMethod);
        } else if (cachedMethod != nullMethod)
            parameters = tryMethod(cachedMethod, things);

        if (parameters == null || cachedMethod == nullMethod){
            defaultAct(things);
            return;
        }

        invokeMethod(cachedMethod, parameters);

    }

    public void invokeMethod(Method cachedMethod, Object... parameters) {
        try {
            cachedMethod.invoke(this, parameters);
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
            throw new RuntimeException("error executing act", e);
        }
    }

    public Object[] tryMethod(Method method, Object[] objects) {

        Object[] things = Arrays.copyOf(objects, objects.length);
        Class[] paramTypes = method.getParameterTypes();
        Object[] paramValues = new Object[paramTypes.length];
        List<Object> varargValues = null;
        boolean hasVararg = false;
        Class varargClass = null;

        Class lastType = paramTypes[paramTypes.length-1];
        if (lastType.isArray()) {
            hasVararg = true;
            varargClass = getArrayType(lastType);
            varargValues = new ArrayList<Object>();
        }

        for (int i = 0; i<paramTypes.length; i++) {
            Class paramType = paramTypes[i];
            for (int k=0; k < things.length; k++) {
                Object o = things[k];
                if (o == null)
                    continue;
                else {
                    if (paramType.isInstance(o)) {
                        paramValues[i] = o;
                        things[k] = null;
                        break;
                    }
                    else if (i == paramTypes.length -1 && hasVararg && varargClass.isInstance(o)) {
                        things[k] = null;
                        varargValues.add(o);
                    }
                }
            }
            if (paramValues[i] != null)
                continue;
            if (varargValues != null) {
                Object o = Array.newInstance(varargClass, varargValues.size());
                for (int k=0; k<varargValues.size(); k++)
                    Array.set(o, k, varargValues.get(k));
                paramValues[i] = o;
            }
            else // a value will be null, which we don't accept
                return null;
        }

        return paramValues;


    }

    public void defaultAct(Object... things) {
    }
}