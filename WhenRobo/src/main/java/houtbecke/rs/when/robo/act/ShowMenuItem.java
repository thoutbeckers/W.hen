package houtbecke.rs.when.robo.act;

import android.view.MenuItem;

import com.squareup.otto.Bus;

public class ShowMenuItem extends BaseUpdateMenuItem {

    @javax.inject.Inject
    public ShowMenuItem(Bus bus) {
        super(bus);
    }

    @Override
    public void changeMenu(MenuItem menuItem, boolean change) {
            menuItem.setVisible(change);
    }

}
