package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.robo.condition.event.SwipeRefresh;

@SuppressWarnings("UnusedDeclaration")
public class SwipeRefreshed extends BasePushCondition {

    @Inject
    public SwipeRefreshed(Bus bus) {
        bus.register(this);
    }

    @Subscribe public void onRefresh(SwipeRefresh view) {
        eventForThing(view.getResourceId(), view.getObject(), view.getActivity());
        eventForThing(view.getSourceClass(),view.getObject());
        eventForThing(view.getObject(),view.getObject());

        Object refreshAction = view.getObject().getTag(houtbecke.rs.when.robo.R.id.tag_refresh);
        if (refreshAction != null)
            eventForThing(refreshAction, refreshAction);
    }
}
