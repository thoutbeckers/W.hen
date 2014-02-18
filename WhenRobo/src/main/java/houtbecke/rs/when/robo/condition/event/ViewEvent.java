package houtbecke.rs.when.robo.condition.event;

import android.view.View;

import houtbecke.rs.when.robo.event.Sourceable;

public class ViewEvent implements Sourceable{

    View view;

    public ViewEvent(View view) {
        this.view = view;
    }

    @Override
    public Class getSourceClass() {
        return view.getClass();
    }

    @Override
    public Object getObject() {
        return view;
    }

    @Override
    public Integer getResourceId() {
        return view.getId();
    }

}
