package houtbecke.rs.when;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultConditionThings<T> implements ConditionThings<T> {

    Set<T> things = new HashSet<>(0);

    class PushConditionWithListener {
        PushConditionListener listener;
        PushCondition condition;

        PushConditionWithListener(PushCondition condition, PushConditionListener listener) {
            this.condition = condition;
            this.listener = listener;
        }
    }
    List<PushConditionWithListener> conditionsWithListener = new ArrayList<>(0);

    @Override
    public void addThing(T thing) {
        if (things.contains(thing))
            return;

        for (PushConditionWithListener pwl: conditionsWithListener)
            pwl.condition.addListener(pwl.listener, thing);

        things.add(thing);

        for (ThingsListener<T> listener: thingsListeners)
            listener.thingAdded(this, thing);

        isNotEmptyCondition.event();

    }

    @Override
    public void removeThing(T thing) {
        if (!things.contains(thing))
            return;
        for (PushConditionWithListener pwl: conditionsWithListener)
            pwl.condition.removeListener(pwl.listener, thing);
        things.remove(thing);

        for (ThingsListener<T> listener: thingsListeners)
            listener.thingRemoved(this, thing);

        isEmptyCondition.event();

    }

    Set<ThingsListener<T>> thingsListeners = new HashSet<>(0);

    @Override
    public void observe(ThingsListener<T> listener) {
        thingsListeners.add(listener);
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


    @Override
    public String toString() {
        return "DefaultConditionThings{" +
                "things=" + things +
                '}';
    }

    /**
     * If a thing that is an instance of filterClass is not one of the things in this DefaultConditionThings
     * the return PullCondition will evaluate as met.
     *
     * @param filterClass the class to which a thing would have to be an instance of
     * @return The condition with which to test
     */
    public PullCondition notOneOf(final Class<? extends T> filterClass) {
        return new PullCondition() {
            @Override
            public boolean isMet(Object thing) {
                if (filterClass.isInstance(thing)) {
                    if (!things.contains(thing))
                        return true;
                    return false;
                }
                return false;
            }

            @Override
            public String toString() {
                return "notOneOf "+DefaultConditionThings.this.toString();
            }

        };
    }

    /**
     * If a thing is an instance of filterClass and is one of the things in this DefaultConditionThing
     * then the return PullCondition will evaluate as met
     * @param filterClass the class to which a thing would have to be an instance of
     * @return The condition with which to test
     */
    public PullCondition oneOf(final Class<? extends T> filterClass) {
        return new PullCondition() {
            @Override
            public boolean isMet(Object thing) {
                if (filterClass.isInstance(thing)) {
                    if (things.contains(thing))
                        return true;
                    return false;
                }
                return false;
            }


            @Override
            public String toString() {
                return "oneOf "+DefaultConditionThings.this.toString();
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


    public BasePushCondition isEmpty() {
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

    public BasePushCondition isNotEmpty() {
        return isNotEmptyCondition;
    }

}
