package houtbecke.rs.when.robo.condition.event;

import android.app.Activity;
import android.view.MenuItem;

public class MenuItemSelect extends MenuItemEvent {

    final public Activity activity;

    public MenuItemSelect(MenuItem menuItem, Activity activity) {
        super(menuItem);
        this.activity = activity;
    }

    public Activity getActivity() {
        return this.activity;
    }
}
