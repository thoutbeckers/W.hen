package houtbecke.rs.when.robo.condition.event;

import android.view.MenuItem;

import houtbecke.rs.when.robo.event.Sourceable;

public class MenuItemEvent implements Sourceable {
    MenuItem menuItem;

    public MenuItemEvent(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    @Override
    public Class getSourceClass() {
        return menuItem.getClass();
    }

    @Override
    public Object getObject() {
        return menuItem;
    }

    @Override
    public Integer getResourceId() {
        return menuItem.getItemId();
    }
    
}
