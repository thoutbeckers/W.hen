package houtbecke.rs.when;

public interface ConditionThings<T> extends Things<T> {
    void addThing(T thing);

    void removeThing(T thing);

    void addPushCondition(PushCondition condition, PushConditionListener listener);
}
