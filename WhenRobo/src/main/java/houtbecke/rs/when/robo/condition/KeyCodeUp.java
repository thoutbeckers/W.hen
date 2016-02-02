package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;
import javax.inject.Singleton;

import houtbecke.rs.when.BasePushCondition;
import houtbecke.rs.when.robo.condition.event.KeyUp;

@Singleton
public class KeyCodeUp extends BasePushCondition {

    @Inject
    public KeyCodeUp(Bus bus) {
        bus.register(this);
    }

    @Subscribe
    public void keyUp(KeyUp keyUp) {

        eventForThing(keyUp.event.getKeyCode(), keyUp, keyUp.event, keyUp.activity);
    }

}
