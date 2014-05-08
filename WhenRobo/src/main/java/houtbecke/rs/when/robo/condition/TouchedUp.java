package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.robo.condition.event.ViewTouchUp;

public class TouchedUp extends BasePushCondition {

    Bus bus;

    @Inject
    public TouchedUp(Bus bus) {
        bus.register(this);
        this.bus = bus;
    }

    @Subscribe public void onTouchUp(ViewTouchUp view) {
        eventForThing(view.getResourceId());
        eventForThing(view.getSourceClass());
        eventForThing(view.getObject());

    }
}
