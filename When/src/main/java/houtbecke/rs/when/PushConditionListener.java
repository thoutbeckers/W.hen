package houtbecke.rs.when;

public interface PushConditionListener {

    public void push(Condition c, boolean conditionMet, boolean isSticky, Object... results);

}
