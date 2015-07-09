package houtbecke.rs.when;

public interface Thing<T> extends FilterConditionThings<T> {

    void setOrReplaceThing(T t);

    T get();

}
