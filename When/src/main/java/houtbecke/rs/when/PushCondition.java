package houtbecke.rs.when;

public interface PushCondition extends Condition {
    void addListener(PushConditionListener listener, Object thing);
    void removeListener(PushConditionListener listener, Object thing);
}
