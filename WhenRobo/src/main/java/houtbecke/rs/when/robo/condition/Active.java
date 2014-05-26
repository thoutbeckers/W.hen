package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.robo.condition.event.ActivityPause;
import houtbecke.rs.when.robo.condition.event.ActivityResume;
import houtbecke.rs.when.robo.event.PauseEvent;
import houtbecke.rs.when.robo.event.ResumeEvent;

public class Active extends BasePushCondition {

    // maybe use a weakhashmap to track behaviour for multiple object of same classes etc.

    Bus bus;

    @Inject
    public Active(Bus bus) {
        bus.register(this);
        this.bus = bus;
    }

    @Subscribe
    public void onActivityEvent(ActivityResume event) {
       onResume(event);
    }

    @Subscribe
    public void onActivityEvent(ActivityPause event) {
        onPause(event);
    }

    @Subscribe
    public void onFragmentEvent(FragmentResume event) {
        onResume(event);
    }

    @Subscribe
    public void onFragmentEvent(FragmentPause event) {
        onPause(event);
    }

    void onPause(PauseEvent pause) {
        stickForThing(pause.getSourceClass(), false);
        stickForThing(pause.getObject(), false);
        stickForThing(pause.getResourceId(), false);
    }

    void onResume(ResumeEvent resume) {
        stickForThing(resume.getSourceClass(),true);
        stickForThing(resume.getObject(),true);
        stickForThing(resume.getResourceId(),true);
    }


}
