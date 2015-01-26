package houtbecke.rs.when.robo.act;

import android.os.Handler;
import android.os.Looper;

import houtbecke.rs.when.ConditionThings;
import houtbecke.rs.when.act.RemoveFrom;

public class RemoveFromOnUI extends RemoveFrom {
    final Handler handler;


    public RemoveFromOnUI(ConditionThings things) {
        super(things);
        handler = new Handler(Looper.getMainLooper());

    }

    public RemoveFromOnUI(ConditionThings things, Class... classes) {
        super(things, classes);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void removeThing(final Object thing) {
        if (Looper.getMainLooper() == Looper.myLooper())
            super.removeThing(thing);
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    RemoveFromOnUI.super.removeThing(thing);
                }
            });
        }
    }
}
