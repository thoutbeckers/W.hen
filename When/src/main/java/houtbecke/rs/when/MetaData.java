package houtbecke.rs.when;

public class MetaData<T> {

    public final Object object;
    public final T field;
    public final String value;

    public MetaData(Object object, T field, String value) {
        this.object = object;
        this.field = field;
        this.value = value;
    }
}
