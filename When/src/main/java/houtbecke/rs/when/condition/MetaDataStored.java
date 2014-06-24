package houtbecke.rs.when.condition;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.MetaData;
import houtbecke.rs.when.MetaDataListener;
import houtbecke.rs.when.ObservableMetaDataStore;
import houtbecke.rs.when.PushCondition;
import houtbecke.rs.when.TypedPushCondition;

public class MetaDataStored<T> extends TypedPushCondition implements MetaDataListener<T> {

    final T field;

    public MetaDataStored() {
        this(null);
    }

    public MetaDataStored(T field) {
        this.field = field;
    }

    public void thing(ObservableMetaDataStore<T> metaDataStore) {
        metaDataStore.addMetaDataListener(this);
    }

    @Override
    public void metaDataSet(MetaData<T> metaData) {
        if (field == null || metaData.field == field)
            eventForThing(metaData.object, metaData, metaData.object);
    }
}
