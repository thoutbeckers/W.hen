package houtbecke.rs.when.robo.condition.event;

import android.app.Activity;

import houtbecke.rs.when.robo.event.ResumeEvent;

public class ActivityResume extends ActivityEvent implements ResumeEvent {

    public ActivityResume(Activity activity) {
        super(activity);
    }
}
