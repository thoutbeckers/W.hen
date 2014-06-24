package houtbecke.rs.when;

public interface ObservableMetaDataStore<T> extends MetaDataStore<T> {

    public void addMetaDataListener(Object object, MetaDataListener<T> listener);
    public void addMetaDataListener(MetaDataListener<T> listener);

}
