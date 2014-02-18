package houtbecke.rs.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TypedPullCondition implements PullCondition {

    ClassMethodCache methodCache = new ClassMethodCache(this, "isMet");


    @Override
    public boolean isMet(Object thing) {

        if (thing == null)
            return isMet();

        Method method = methodCache.getMethodForObject(thing);
        if (method == null)
            return isMet();

        try {
            return (Boolean) method.invoke(this, thing);
        } catch (InvocationTargetException e) {
            // perhaps add warning
        } catch (IllegalAccessException e) {
            // perhaps add warning
        }
        return isMet();
    }

    public boolean isMet() {return false;}
}
