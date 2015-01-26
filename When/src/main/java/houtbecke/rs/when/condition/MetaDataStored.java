package houtbecke.rs.when.condition;

import houtbecke.rs.when.MetaData;
import houtbecke.rs.when.MetaDataListener;
import houtbecke.rs.when.ObservableMetaDataStore;
import houtbecke.rs.when.TypedPushCondition;

public class MetaDataStored<T> extends TypedPushCondition implements MetaDataListener<T> {

    final T field;

    final String value;
    
    final ObservableMetaDataStore<T> store;

    public MetaDataStored(ObservableMetaDataStore<T> store) {
        this(store, null);
    }

    public MetaDataStored(ObservableMetaDataStore<T> store, T field) {
        this(store, field , null);
    }

    public MetaDataStored(ObservableMetaDataStore<T> store, T field, String value) {
        this.value = value;
        this.field = field;
        this.store = store;
        store.addMetaDataListener(this);
    }

    @Override
    public void metaDataSet(MetaData<T> metaData) {
        if (field == null || metaData.field == field)
            if (value == null || value.equals(metaData.value))
                eventForThing(metaData.object, metaData, metaData.object);
    }
}
