package houtbecke.rs.when.robo.condition.event;

import android.app.Activity;
import android.view.View;

public class ViewClick extends ViewEvent {

    final public Activity activity;

    public ViewClick(View view, Activity activity) {
        super(view);
        this.activity = activity;
    }

    public Activity getActivity() {
        return this.activity;
    }
}
