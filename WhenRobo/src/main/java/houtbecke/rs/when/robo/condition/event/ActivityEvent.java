package houtbecke.rs.when.robo.condition.event;

import android.app.Activity;

import houtbecke.rs.when.robo.event.Sourceable;

public class ActivityEvent implements Sourceable{

    Activity activity;

    public ActivityEvent(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Class getSourceClass() {
        return activity.getClass();
    }

    @Override
    public Object getObject() {
        return activity;
    }

    @Override
    public Integer getResourceId() {
        return null;
    }



}
