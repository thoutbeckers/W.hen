package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.robo.condition.event.ActivityPause;
import houtbecke.rs.when.robo.condition.event.ActivityPostPause;
import houtbecke.rs.when.robo.condition.event.ActivityPostResume;
import houtbecke.rs.when.robo.condition.event.ActivityResume;
import houtbecke.rs.when.robo.condition.event.FragmentPause;
import houtbecke.rs.when.robo.condition.event.FragmentResume;
import houtbecke.rs.when.robo.event.PauseEvent;
import houtbecke.rs.when.robo.event.ResumeEvent;

@Singleton
public abstract class BaseActive extends BasePushCondition {

    // maybe use a weakhashmap to track behaviour for multiple object of same classes etc.

    final boolean onPause, onResume, onPostPause, onPostResume;

    @Inject
    public BaseActive(Bus bus, Boolean onResume, Boolean onPause, Boolean onPostResume, Boolean onPostPause) {
        subscriber = new Subscriber(bus);
        this.onResume = onResume;
        this.onPause = onPause;
        this.onPostResume = onPostResume;
        this.onPostPause = onPostPause;
    }

    final Subscriber subscriber;

    private class Subscriber {

        Subscriber(Bus bus) {
            bus.register(this);
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
        public void onActivityEvent(ActivityPostPause event) {
            onPostPause(event);
        }

        @Subscribe
        public void onActivityEvent(ActivityPostResume event) {
            onPostResume(event);
        }

        @Subscribe
        public void onFragmentEvent(FragmentResume event) {
            onResume(event);
        }

        @Subscribe
        public void onFragmentEvent(FragmentPause event) {
            onPause(event);
        }
    }

    void onPause(PauseEvent pause) {
        stickForThing(pause.getSourceClass(), onPause);
        stickForThing(pause.getObject(), onPause);
        stickForThing(pause.getResourceId(), onPause);
    }

    void onResume(ResumeEvent resume) {
        stickForThing(resume.getSourceClass(),onResume);
        stickForThing(resume.getObject(),onResume);
        stickForThing(resume.getResourceId(),onResume);
    }

    void onPostPause(PauseEvent pause) {
        stickForThing(pause.getSourceClass(), onPostPause);
        stickForThing(pause.getObject(), onPostPause);
        stickForThing(pause.getResourceId(), onPostPause);
    }

    void onPostResume(ResumeEvent resume) {
        stickForThing(resume.getSourceClass(),onPostResume);
        stickForThing(resume.getObject(),onPostResume);
        stickForThing(resume.getResourceId(),onPostResume);
    }

}
