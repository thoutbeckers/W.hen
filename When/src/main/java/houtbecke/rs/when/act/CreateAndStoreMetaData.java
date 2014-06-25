package houtbecke.rs.when.act;

import houtbecke.rs.when.Act;
import houtbecke.rs.when.MetaData;
import houtbecke.rs.when.MetaDataStore;

public class CreateAndStoreMetaData<T> extends CreateMetaData<T> {

    MetaDataStore<T> store;

    public CreateAndStoreMetaData(MetaDataStore<T> store, Class clazz, T field, String value) {
        super(clazz, field, value);
        this.store = store;
    }

    @Override
    public void onMetaDataFound(Object object, T field, String value) {
        store.storeMetaData(new MetaData<>(object, field, value));
    }
}
