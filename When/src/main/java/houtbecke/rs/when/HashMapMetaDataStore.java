package houtbecke.rs.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HashMapMetaDataStore<T> implements ObservableMetaDataStore<T> {

    Map<Object, Map<T, String>> metaData = Collections.synchronizedMap(new HashMap<Object, Map<T, String>>());
    Map<Object, Set<MetaDataListener<T>>> metaDataListeners = Collections.synchronizedMap(new HashMap<Object, Set<MetaDataListener<T>>>());


    Map<T, String> getFieldValueMapForObject(Object object) {
        Map<T, String> ret = metaData.get(object);
        if (ret == null) {
            metaData.put(object, Collections.synchronizedMap(new HashMap<T, String>()));
            return metaData.get(object);
        }
        return ret;
    }

    Set<MetaDataListener<T>> getMetaDataListenersForObject(Object object) {
        Set<MetaDataListener<T>> ret = metaDataListeners.get(object);
        if (ret == null) {
            metaDataListeners.put(object, Collections.synchronizedSet(new HashSet<MetaDataListener<T>>()));
            return metaDataListeners.get(object);
        }
        return ret;
    }

    @Override
    public String getMetaData(Object object, T field) {
        return getFieldValueMapForObject(object).get(field);
    }

    @Override
    public void storeMetaData(MetaData<T> metaData) {
        getFieldValueMapForObject(metaData.object).put(metaData.field, metaData.value);
        for (MetaDataListener<T> listener: getMetaDataListenersForObject(metaData.object))
            listener.metaDataSet(metaData);
        for (MetaDataListener<T> listener: getMetaDataListenersForObject(null))
            listener.metaDataSet(metaData);
    }
    @Override
    public void addMetaDataListener(Object object, MetaDataListener<T> listener) {
        getMetaDataListenersForObject(object).add(listener);
    }

    @Override
    public void addMetaDataListener(MetaDataListener<T> listener) {
        // use the null key for the listeners that want updates on all objects
        getMetaDataListenersForObject(null).add(listener);
    }
}
