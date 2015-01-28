package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.robo.condition.event.SwipeRefresh;
import houtbecke.rs.when.robo.condition.event.ViewClick;

public class SwipeRefreshed extends BasePushCondition {

    Bus bus;

    @Inject
    public SwipeRefreshed(Bus bus) {
        bus.register(this);
        this.bus = bus;
    }

    @Subscribe public void onRefresh(SwipeRefresh view) {
        eventForThing(view.getResourceId(), view.getObject(), view.getActivity());
        eventForThing(view.getSourceClass(),view.getObject());
        eventForThing(view.getObject(),view.getObject());
    }
}
