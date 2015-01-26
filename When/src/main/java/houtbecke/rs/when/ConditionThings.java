package houtbecke.rs.when;

public interface ConditionThings<T> extends ObservableThings<T> {

    void addPushCondition(PushCondition condition, PushConditionListener listener);
}
