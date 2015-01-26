package houtbecke.rs.when;

public interface FilterConditionThings<T> extends ConditionThings<T> {
    PullCondition notOneOf(Class<? extends T> filterClass);

    PullCondition oneOf(Class<? extends T> filterClass);

    BasePushCondition isEmpty();

    BasePushCondition isNotEmpty();
}
