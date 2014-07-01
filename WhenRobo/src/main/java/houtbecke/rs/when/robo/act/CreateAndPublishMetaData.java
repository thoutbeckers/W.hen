package houtbecke.rs.when.robo.act;

import com.squareup.otto.Bus;

import houtbecke.rs.when.MetaData;
import houtbecke.rs.when.act.CreateMetaData;

public class CreateAndPublishMetaData<T> extends CreateMetaData<T> {

    protected final Bus bus;

    public CreateAndPublishMetaData(Bus bus, Class clazz, T field, String value) {
        super(clazz, field, value);
        this.bus = bus;
    }

    public void onMetaDataFound(Object object, T field, String value) {
        bus.post(new MetaData<>(object, field, value));
    }

}
