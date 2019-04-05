package houtbecke.rs.when.robo.condition.event;

import android.app.Activity;

import houtbecke.rs.when.robo.event.PauseEvent;

public class ActivityPostPause extends ActivityEvent implements PauseEvent {
    public ActivityPostPause(Activity activity) {
        super(activity);
    }
}
