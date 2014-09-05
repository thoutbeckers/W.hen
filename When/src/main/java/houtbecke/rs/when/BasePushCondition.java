package houtbecke.rs.when;

import java.util.IdentityHashMap;
import java.util.LinkedHashMap;

public class BasePushCondition implements PushCondition {

    final LinkedHashMap<Object, IdentityHashMap<PushConditionListener, Void>> listeners = new LinkedHashMap<Object, IdentityHashMap<PushConditionListener, Void>>(1);

    @Override
    public void removeListener(PushConditionListener listener, Object thing) {
        synchronized (listeners) {

            IdentityHashMap<PushConditionListener, Void> pcls = listeners.get(thing);
            if (pcls == null)
                return;
            if (pcls.size() <= 1)
                listeners.remove(thing);
            pcls.remove(listener);
        }
    }

    @Override
    public void addListener(PushConditionListener listener, Object thing) {

        synchronized (listeners) {
            IdentityHashMap<PushConditionListener, Void> pcls = listeners.get(thing);
            if (pcls != null && !pcls.keySet().contains(listener)) {
                pcls.put(listener, null);
            } else {
                pcls = new IdentityHashMap<PushConditionListener, Void>(1);
                pcls.put(listener, null);
                listeners.put(thing, pcls);
            }
        }
    }

    public void push(Object thing, boolean isConditionMet, boolean isSticky, Object... results ) {

        synchronized(listeners) {

            if (thing != null) {
                IdentityHashMap<PushConditionListener, Void> pcls = listeners.get(thing);
                if (pcls == null)
                    return;
                for (PushConditionListener pcl : pcls.keySet())
                    pcl.push(this, isConditionMet, isSticky, results);
                return;
            }


            for (IdentityHashMap<PushConditionListener, Void> pcls : listeners.values()) {
                for (PushConditionListener pcl : pcls.keySet())
                    pcl.push(this, isConditionMet, isSticky, results);
            }
        }
    }

    public void eventForThing(Object thing, Object... results) {
        if (thing == null)
            return;
        push(thing, true, false, results);
    }
    public void event(Object... results) {
        push(null, true, false, results);
    }

    public void stickForThing(Object thing, boolean isConditionMet, Object... results) {
        if (thing == null)
            return;
        push(thing, isConditionMet, true, results);
    }
    public void stick(boolean isConditionMet, Object... results) {
        push(null, isConditionMet, true, results);
    }
}
