package houtbecke.rs.when.robo.condition.event;

import androidx.fragment.app.Fragment;

import houtbecke.rs.when.robo.event.ResumeEvent;

public class FragmentResume extends FragmentEvent implements ResumeEvent {
    public FragmentResume(Fragment fragment) {
        super(fragment);
    }
}
