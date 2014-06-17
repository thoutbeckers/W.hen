package houtbecke.rs.when;

public interface ObservableThings<T> extends Things<T> {

    void addThing(T thing);

    void removeThing(T thing);

    void observe(ThingsListener<T> listener);


}
