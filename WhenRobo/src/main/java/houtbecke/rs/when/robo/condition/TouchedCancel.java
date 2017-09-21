package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.robo.condition.event.ViewTouchCancel;

@Singleton
public class TouchedCancel extends BasePushCondition {

    Bus bus;

    @Inject
    public TouchedCancel(Bus bus) {
        bus.register(this);
        this.bus = bus;
    }

    @Subscribe public void onTouchCancel(ViewTouchCancel view) {
        eventForThing(view.getResourceId(),view.getObject());
        eventForThing(view.getSourceClass(),view.getObject());
        eventForThing(view.getObject());
    }
}
