package houtbecke.rs.when.robo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import javax.inject.Inject;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import houtbecke.rs.when.robo.act.event.InvalidateMenus;
import houtbecke.rs.when.robo.condition.event.ActivityPause;
import houtbecke.rs.when.robo.condition.event.ActivityResume;
import houtbecke.rs.when.robo.condition.event.FragmentPause;
import houtbecke.rs.when.robo.condition.event.FragmentResume;
import houtbecke.rs.when.robo.condition.event.MenuItemSelect;
import houtbecke.rs.when.robo.condition.event.SwipeRefresh;
import houtbecke.rs.when.robo.condition.event.ViewClick;
import houtbecke.rs.when.robo.condition.event.MenuCreated;

import android.view.MotionEvent;

import java.lang.ref.WeakReference;

import houtbecke.rs.when.robo.condition.event.ViewTouchCancel;
import houtbecke.rs.when.robo.condition.event.ViewTouchDown;
import houtbecke.rs.when.robo.condition.event.ViewTouchUp;
import houtbecke.rs.when.robo.condition.event.KeyUp;

public class EventHelper {

    Bus bus;
    @Inject
    public EventHelper(Bus bus) {
        this.bus = bus;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Subscribe public void onInvalidateMenus(InvalidateMenus invalidateMenus) {
        Activity activity = myActivity.get();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && activity != null)
            activity.invalidateOptionsMenu();

    }

    WeakReference<Fragment> myFragment;
    WeakReference<Activity> myActivity;

    public void onCreate(Activity activity) {
        myActivity = new WeakReference<>(activity);
    }
    
    public void onCreate(Fragment fragment) {
        myFragment = new WeakReference<>(fragment);
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

    public void onKeyUp(Activity activity, int keyCode, KeyEvent event) {
        bus.post(new KeyUp(activity, keyCode, event));
    }

    public void onClick(View v, Activity activity) {
        bus.post(new ViewClick(v, activity));
    }

    public void onMenuItemSelected(MenuItem item,Activity activity) {
        onMenuItemSelected(item, activity, null);
    }
    public void onMenuItemSelected(MenuItem item,Activity activity, View view) {
        bus.post(new MenuItemSelect(item,activity, view));
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

    public void onRefresh(View v, Activity activity) {
        bus.post(new SwipeRefresh(v, activity));
    }

    public void setOnRefreshListenerForAction(final SwipeRefreshLayout swipeRefreshLayout, Object action) {
        swipeRefreshLayout.setTag(houtbecke.rs.when.robo.R.id.tag_refresh, action);
        swipeRefreshLayout.setOnRefreshListener(
            new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Context c = swipeRefreshLayout.getContext();
                    EventHelper.this.onRefresh(swipeRefreshLayout, c instanceof Activity ? (Activity) c : null);
                }
            });
    }
}
