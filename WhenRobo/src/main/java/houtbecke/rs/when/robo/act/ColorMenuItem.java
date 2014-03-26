package houtbecke.rs.when.robo.act;

import android.graphics.PorterDuff;
import android.view.MenuItem;

import com.squareup.otto.Bus;

public class ColorMenuItem extends BaseUpdateMenuItem {

    @javax.inject.Inject
    public ColorMenuItem(Bus bus) {
        super(bus);
    }

    @Override
    public void changeMenu(MenuItem menuItem, int color) {
        if (menuItem.getIcon() != null)
            menuItem.getIcon().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }
    @Override
    public void changeMenu(MenuItem menuItem,boolean change) {
        if (!change)
            menuItem.getIcon().clearColorFilter();
    }

}
