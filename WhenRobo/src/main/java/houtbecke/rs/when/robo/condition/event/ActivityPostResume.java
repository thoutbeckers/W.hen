package houtbecke.rs.when.robo.condition.event;

import android.app.Activity;

import houtbecke.rs.when.robo.event.ResumeEvent;

public class ActivityPostResume extends ActivityEvent implements ResumeEvent {

    public ActivityPostResume(Activity activity) {
        super(activity);
    }
}
