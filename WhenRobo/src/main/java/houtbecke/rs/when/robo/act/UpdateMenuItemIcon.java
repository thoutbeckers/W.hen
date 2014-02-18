package houtbecke.rs.when.robo.act;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import com.squareup.otto.Bus;

public class UpdateMenuItemIcon extends BaseUpdateMenuItem {

    @javax.inject.Inject
    public UpdateMenuItemIcon(Bus bus) {
        super(bus);
    }

    @Override
    public void changeMenu(MenuItem menuItem, int change) {
        menuItem.setIcon(change);
    }

    @Override
    public void changeMenu(MenuItem menuItem, Drawable change) {
        menuItem.setIcon(change);
    }
}
