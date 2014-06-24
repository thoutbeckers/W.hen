package houtbecke.rs.when;

import houtbecke.rs.when.ObservableThings;
import houtbecke.rs.when.Things;
import houtbecke.rs.when.ThingsListener;

public interface ObservableThing<T> extends ObservableThings<T> {

    void setOrReplaceThing(T t);

    void clearThing();
}
