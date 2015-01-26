package houtbecke.rs.when.condition;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.ObservableThings;
import houtbecke.rs.when.Things;
import houtbecke.rs.when.ThingsListener;

public class Removed extends BasePushCondition implements ThingsListener {

    ObservableThings things;
    @SuppressWarnings("unchecked")
    public Removed(ObservableThings things) {
        this.things = things;
        things.observe(this);
    }

    @Override
    public void thingAdded(Things things, Object thing) {
    }

    @Override
    public void thingRemoved(Things things, Object thing) {
        event(thing);
    }

    @Override
    public String toString() {
        return "Removed{" +
                "things=" + things +
                '}';
    }
}
