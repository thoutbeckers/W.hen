package houtbecke.rs.when.act;

import houtbecke.rs.when.MetaData;
import houtbecke.rs.when.MetaDataStore;
import houtbecke.rs.when.TypedAct;

public class StoreMetaData extends TypedAct {

    @SuppressWarnings("unchecked")
    public void act(MetaData data, MetaDataStore store) {
        store.storeMetaData(data);
    }
}
