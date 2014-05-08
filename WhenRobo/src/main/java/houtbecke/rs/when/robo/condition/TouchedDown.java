package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.robo.condition.event.ViewTouchDown;

public class TouchedDown extends BasePushCondition {

    Bus bus;

    @Inject
    public TouchedDown(Bus bus) {
        bus.register(this);
        this.bus = bus;
    }

    @Subscribe public void onTouchDown(ViewTouchDown view) {
        eventForThing(view.getResourceId());
        eventForThing(view.getSourceClass());
        eventForThing(view.getObject());

    }
}
