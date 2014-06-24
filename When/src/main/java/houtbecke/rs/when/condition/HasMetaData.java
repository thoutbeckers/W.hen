package houtbecke.rs.when.condition;

import houtbecke.rs.when.MetaDataStore;
import houtbecke.rs.when.PullCondition;
import houtbecke.rs.when.TypedPullCondition;

public class HasMetaData<C, T> implements PullCondition {


    Class<? extends C> objectClass;
    MetaDataStore<T> store;
    T field;
    String value;

    public HasMetaData(Class<? extends C> objectClass, MetaDataStore<T> store, T field, String value) {
        this.objectClass = objectClass;
        this.store = store;
        this.field = field;
        this.value = value;
    }

    @Override
    public boolean isMet(Object thing) {
        return
            objectClass.isInstance(thing)
            && value.equals(store.getMetaData(thing, field));
    }
}
