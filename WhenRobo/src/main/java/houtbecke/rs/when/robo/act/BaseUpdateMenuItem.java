package houtbecke.rs.when.robo.act;

import android.graphics.drawable.Drawable;
import android.view.MenuItem;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import houtbecke.rs.when.TypedAct;
import houtbecke.rs.when.robo.act.event.InvalidateMenus;
import houtbecke.rs.when.robo.condition.event.MenuCreated;
import houtbecke.rs.when.robo.condition.event.MenuItemEvent;

public class BaseUpdateMenuItem extends TypedAct {

    Bus bus;
    MenuUpdater updater = new MenuUpdater(getClass());

    @javax.inject.Inject
    public BaseUpdateMenuItem(Bus bus) {
        this.bus = bus;
        bus.register(updater);
    }



    static Map<Class, Map<Integer, Object>> menuChanges = new HashMap<Class, Map<Integer, Object>>();

    class MenuUpdater {

        public MenuUpdater(Class clazz) {
            this.clazz = clazz;
        }
        Class clazz;

        @Subscribe public void onMenuCreated(MenuCreated menuCreated) {
            MenuItem item;

            synchronized (menuChanges) {
                Map<Integer, Object> changes = menuChanges.get(clazz);

                if (changes == null)
                    return;

                for (Integer id: changes.keySet())
                    if ((item = menuCreated.menu.findItem(id)) != null) {
                        Object change = changes.get(id);
                        if (change instanceof Integer)
                            changeMenu(item, (Integer)change);
                        else if (change instanceof Drawable)
                            changeMenu(item, (Drawable)change);
                        else if (change instanceof String)
                            changeMenu(item, (String)change);
                        else if (change instanceof Boolean)
                            changeMenu(item, (boolean)change);
                        else if (change == null)
                            changeMenu(item);

                    }

            }
        }
    }

    public void changeMenu(MenuItem menuItem, int change) {}
    public void changeMenu(MenuItem menuItem, String change) {}
    public void changeMenu(MenuItem menuItem, Drawable change) {}
    public void changeMenu(MenuItem menuItem, boolean change) {}
    public void changeMenu(MenuItem menuItem) {}



    void addChange(int menuId, Object change) {
        synchronized (menuChanges) {
            Map<Integer, Object> changes = menuChanges.get(getClass());
            if (changes == null) {
                changes = new LinkedHashMap<Integer, Object>(1);
                menuChanges.put(getClass(), changes);
            }
            changes.put(menuId, change);
        }
        bus.post(new InvalidateMenus());
    }

    public void act(Integer menuId, Integer resourceId) {
        addChange(menuId, resourceId);
    }

    public void act(Integer menuId, Drawable d) {
        addChange(menuId, d);
    }

    public void act(Integer menuId, String s) {
        addChange(menuId, s);
    }


}
