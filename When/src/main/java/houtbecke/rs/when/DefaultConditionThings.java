package houtbecke.rs.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultConditionThings<T> implements ConditionThings<T> {




    Set<T> things = new HashSet<T>(0);

    class PushConditionWithListener {
        PushConditionListener listener;
        PushCondition condition;

        PushConditionWithListener(PushCondition condition, PushConditionListener listener) {
            this.condition = condition;
            this.listener = listener;
        }
    }
    List<PushConditionWithListener> conditionsWithListener = new ArrayList<PushConditionWithListener>(0);

    @Override
    public void addThing(T thing) {
        for (PushConditionWithListener pwl: conditionsWithListener)
            pwl.condition.addListener(pwl.listener, thing);
        things.add(thing);
        isNotEmptyCondition.event();

    }

    @Override
    public void removeThing(T thing) {
        for (PushConditionWithListener pwl: conditionsWithListener)
            pwl.condition.removeListener(pwl.listener, thing);
        things.remove(thing);
        isEmptyCondition.event();

    }

    @Override
    public void addPushCondition(PushCondition condition, PushConditionListener listener) {
        for (Object thing: things)
            condition.addListener(listener, thing);

        conditionsWithListener.add(new PushConditionWithListener(condition, listener));
    }

    @Override
    public Set<T> getThings() {
        return things;
    }

    public PullCondition notOneOf(final Class filterClass) {
        return new PullCondition() {
            @Override
            public boolean isMet(Object thing) {
                if (filterClass.isInstance(thing) && !things.contains(thing))
                    return true;
                return false;
            }
        };
    }

    final BasePushCondition isEmptyCondition = new BasePushCondition() {
        @Override
        public void event(Object... results){
            if (things.isEmpty())
                super.event();
        }

    };


    public BasePushCondition IsEmpty() {
        return isEmptyCondition;
    }

   final BasePushCondition isNotEmptyCondition =
            new BasePushCondition() {

                @Override
                public void event(Object... results){
                    if (!things.isEmpty())
                        super.event();
                }
            };

    public BasePushCondition IsNotEmpty() {
        return isNotEmptyCondition;
    }
}
