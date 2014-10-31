package houtbecke.rs.when.robo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import houtbecke.rs.when.robo.act.event.InvalidateMenus;
import houtbecke.rs.when.robo.condition.event.ActivityPause;
import houtbecke.rs.when.robo.condition.event.ActivityResume;
import houtbecke.rs.when.robo.condition.event.FragmentPause;
import houtbecke.rs.when.robo.condition.event.FragmentResume;
import houtbecke.rs.when.robo.condition.event.MenuItemSelect;
import houtbecke.rs.when.robo.condition.event.ViewClick;
import houtbecke.rs.when.robo.condition.event.MenuCreated;

import android.view.MotionEvent;

import houtbecke.rs.when.robo.condition.event.ViewTouchCancel;
import houtbecke.rs.when.robo.condition.event.ViewTouchDown;
import houtbecke.rs.when.robo.condition.event.ViewTouchUp;

public class EventHelper {

    Bus bus;
    @Inject
    public EventHelper(Bus bus) {
        this.bus = bus;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Subscribe public void onInvalidateMenus(InvalidateMenus invalidateMenus) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && myActivity != null)
            myActivity.invalidateOptionsMenu();
    }

    Fragment myFragment;
    Activity myActivity;

    public void onCreate(Activity activity) {
        myActivity = activity;
    }

    public void onCreate(Fragment fragment) {
        myFragment = fragment;
    }

    public void onResume(Activity a) {
        bus.post(new ActivityResume(a));
        bus.register(this);
        bus.register(a);
    }

    public void onResume(Fragment f) {
        bus.post(new FragmentResume(f));
        bus.register(this);
        bus.register(f);
    }

    public void onPause(Activity a) {
        bus.post(new ActivityPause(a));
        bus.unregister(this);
        bus.unregister(a);
    }

    public void onPause(Fragment f) {
        bus.post(new FragmentPause(f));
        bus.unregister(this);
        bus.unregister(f);
    }

    public void onClick(View v, Activity activity) {
        bus.post(new ViewClick(v, activity));
    }

    public void onMenuItemSelected(MenuItem item,Activity activity) {
        Log.i("posting", "posting onmenuitem");
        bus.post(new MenuItemSelect(item,activity));
    }

    public void optionsMenuCreated(Menu menu) {
        bus.post(new MenuCreated(menu));
    }

    public void onTouch(android.view.View v, MotionEvent motionevent) {
        int action = motionevent.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            bus.post(new ViewTouchDown(v));
        } else if (action == MotionEvent.ACTION_UP) {
            bus.post(new ViewTouchUp(v));
        } else if (action == MotionEvent.ACTION_CANCEL) {
             bus.post(new ViewTouchCancel(v));
        }


    }
}
