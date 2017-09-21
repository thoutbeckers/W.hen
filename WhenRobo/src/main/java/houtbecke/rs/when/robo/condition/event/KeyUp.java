package houtbecke.rs.when.robo.condition.event;

import android.app.Activity;
import android.view.KeyEvent;

public class KeyUp {

    final public Activity activity;
    final public int keyCode;
    final public KeyEvent event;

    public KeyUp(Activity activity, int keyCode, KeyEvent event) {
        this.activity = activity;
        this.keyCode = keyCode;
        this.event = event;
    }
}
