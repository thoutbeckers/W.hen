package houtbecke.rs.when.robo.condition.event;

import android.app.Activity;

import houtbecke.rs.when.robo.event.PauseEvent;

public class ActivityPause extends ActivityEvent implements PauseEvent {
    public ActivityPause(Activity activity) {
        super(activity);
    }
}
