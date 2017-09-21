package houtbecke.rs.when.robo.condition.event;

import android.app.Activity;
import android.view.MenuItem;
import android.view.View;

public class MenuItemSelect extends MenuItemEvent {

    final public Activity activity;
    final public View view;

    public MenuItemSelect(MenuItem menuItem, Activity activity, View view) {
        super(menuItem);
        this.activity = activity;
        this.view = view;
    }
}
