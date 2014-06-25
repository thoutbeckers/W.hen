package houtbecke.rs.when.act;

import houtbecke.rs.when.Act;
import houtbecke.rs.when.MetaData;
import houtbecke.rs.when.MetaDataStore;

public abstract class CreateMetaData<T> implements Act {

    protected final Class clazz;
    protected final T field;
    protected final String value;

    public CreateMetaData(Class clazz, T field, String value) {
        this.clazz = clazz;
        this.field = field;
        this.value = value;
    }

    @Override
    public void act(Object... things) {
        for (Object thing: things)
            if (clazz.isInstance(thing)) {
                onMetaDataFound(thing, field, value);
            }
    }

    public abstract void onMetaDataFound(Object object, T field, String value);

}
