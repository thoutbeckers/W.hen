package houtbecke.rs.when;

public interface ThingsListener<T> {
    void thingAdded(T t);
    void thingRemoved(T t);
}
