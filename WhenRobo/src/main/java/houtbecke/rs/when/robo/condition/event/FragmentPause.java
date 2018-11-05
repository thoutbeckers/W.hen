package houtbecke.rs.when.robo.condition.event;

import androidx.fragment.app.Fragment;

import houtbecke.rs.when.robo.event.PauseEvent;

public class FragmentPause extends FragmentEvent implements PauseEvent {
    public FragmentPause(Fragment fragment) {
        super(fragment);
    }
}
