package houtbecke.rs.when.robo.condition;

import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.robo.condition.event.MenuItemSelect;

public class Selected extends BasePushCondition {
    Bus bus;

    @Inject
    public Selected(Bus bus) {
        bus.register(this);
        this.bus = bus;
    }

    @Subscribe public void onSelected(MenuItemSelect item) {
        if (item.activity != null && item.view != null) {
            eventForThing(item.getObject(), item.getObject(), item.activity, item.view);
            eventForThing(item.getResourceId(), item.getResourceId(), item.activity, item.view);
            eventForThing(item.getSourceClass(), item.getSourceClass(), item.activity, item.view);
        }
        else if (item.activity != null) {
            eventForThing(item.getObject(), item.getObject(), item.activity);
            eventForThing(item.getResourceId(), item.getResourceId(), item.activity);
            eventForThing(item.getSourceClass(), item.getSourceClass(), item.activity);
        }
        else if (item.view != null) {
            eventForThing(item.getObject(), item.getObject(), item.view);
            eventForThing(item.getResourceId(), item.getResourceId(), item.view);
            eventForThing(item.getSourceClass(), item.getSourceClass(), item.view);
        }
        else {
            eventForThing(item.getObject(), item.getObject());
            eventForThing(item.getResourceId(), item.getResourceId());
            eventForThing(item.getSourceClass(), item.getSourceClass());
        }
    }
}
