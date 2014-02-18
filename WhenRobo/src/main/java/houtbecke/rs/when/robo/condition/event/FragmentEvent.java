package houtbecke.rs.when.robo.condition.event;

import android.app.Activity;
import android.app.Fragment;

import houtbecke.rs.when.robo.event.Sourceable;

public class FragmentEvent implements Sourceable {

    Fragment fragment;

    public FragmentEvent(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public Class getSourceClass() {
        return fragment.getClass();
    }

    @Override
    public Object getObject() {
        return fragment;
    }

    @Override
    public Integer getResourceId() {
        return null;
    }



}
