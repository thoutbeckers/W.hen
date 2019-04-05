package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.TypedPushCondition;
import houtbecke.rs.when.robo.condition.event.ActivityPause;
import houtbecke.rs.when.robo.condition.event.ActivityResume;
import houtbecke.rs.when.robo.condition.event.FragmentPause;
import houtbecke.rs.when.robo.condition.event.FragmentResume;
import houtbecke.rs.when.robo.event.PauseEvent;
import houtbecke.rs.when.robo.event.ResumeEvent;

@Singleton
public class NotActive extends BaseActive {

    @Inject
    public NotActive(Bus bus) {
        super(bus, false, true, false, false);
    }

}
