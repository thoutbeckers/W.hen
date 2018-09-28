package houtbecke.rs.when.robo.condition.event;

import android.support.v4.app.Fragment;

import houtbecke.rs.when.robo.event.ResumeEvent;

public class FragmentResume extends FragmentEvent implements ResumeEvent {
    public FragmentResume(Fragment fragment) {
        super(fragment);
    }
}
