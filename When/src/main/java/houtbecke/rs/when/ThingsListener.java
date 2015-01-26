package houtbecke.rs.when;

public interface ThingsListener<T> {
    void thingAdded(Things<T> things, T thing);
    void thingRemoved(Things<T> things, T thing);
}
