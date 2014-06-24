package houtbecke.rs.when;

public interface MetaDataStore<T> {

    String getMetaData(Object object, T field);
    void storeMetaData(MetaData<T> metaData);

}
