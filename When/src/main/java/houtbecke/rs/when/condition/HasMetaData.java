package houtbecke.rs.when.condition;

import houtbecke.rs.when.MetaDataStore;
import houtbecke.rs.when.PullCondition;
import houtbecke.rs.when.TypedPullCondition;

public class HasMetaData<C, T> implements PullCondition {


    Class<? extends C> objectClass;
    MetaDataStore<T> store;
    T field;
    String value;

    public HasMetaData(Class<? extends C> objectClass, MetaDataStore<T> store, T field) {
        this(objectClass, store, field, null);
    }

    public HasMetaData(Class<? extends C> objectClass, MetaDataStore<T> store, T field, String value) {
        this.objectClass = objectClass;
        this.store = store;
        this.field = field;
        this.value = value;
    }

    @Override
    public boolean isMet(Object thing) {
        if (objectClass.isInstance(thing)) {
            String valueFromStore = store.getMetaData(thing, field);
            if (value == null)
                return valueFromStore != null;
            return value.equals(valueFromStore);

        }
        return false;
    }
}
