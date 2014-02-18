package houtbecke.rs.when.robo.condition.event;

import android.app.Activity;
import android.app.Fragment;

import houtbecke.rs.when.robo.event.PauseEvent;

public class FragmentPause extends FragmentEvent implements PauseEvent {
    public FragmentPause(Fragment fragment) {
        super(fragment);
    }
}
