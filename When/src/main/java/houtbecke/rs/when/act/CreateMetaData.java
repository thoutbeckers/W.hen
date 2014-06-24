package houtbecke.rs.when.act;

import houtbecke.rs.when.Act;
import houtbecke.rs.when.MetaData;
import houtbecke.rs.when.MetaDataStore;
import houtbecke.rs.when.TypedAct;

public class CreateMetaData<T> implements Act {

    MetaDataStore<T> store;
    Class clazz;
    T field;
    String value;

    public CreateMetaData(MetaDataStore<T> store, Class clazz, T field, String value) {
        this.store = store;
        this.clazz = clazz;
        this.field = field;
        this.value = value;
    }

    @Override
    public void act(Object... things) {
        for (Object thing: things)
            if (clazz.isInstance(thing))
                store.storeMetaData(new MetaData<T>(thing, field, value));
    }
}
