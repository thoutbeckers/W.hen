package houtbecke.rs.when;

import java.lang.reflect.Method;

public class TypedPushCondition extends BasePushCondition {

    ClassMethodCache methodCache = new ClassMethodCache(this, "thing");

    @Override
    public void addListener(PushConditionListener listener, Object thing) {
        super.addListener(listener, thing);
        Method m = methodCache.getMethodForObject(thing);
        if (m!=null)
            try {
                m.invoke(this, thing);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    }
}
