package houtbecke.rs.when.condition;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.ObservableThings;
import houtbecke.rs.when.Things;
import houtbecke.rs.when.ThingsListener;

public class Added extends BasePushCondition implements ThingsListener {

    ObservableThings things;
    @SuppressWarnings("unchecked")
    public Added(ObservableThings things) {
        this.things = things;
        things.observe(this);
    }

    @Override
    public void thingAdded(Things things, Object thing) {
        event(thing);
    }

    @Override
    public void thingRemoved(Things things, Object thing) {
    }

    @Override
    public String toString() {
        return "Added{" +
                "things=" + things +
                '}';
    }
}
