package houtbecke.rs.when.robo.act;

import android.os.Handler;
import android.os.Looper;

import houtbecke.rs.when.DefaultConditionThing;
import houtbecke.rs.when.act.Replace;

public class ReplaceOnUI<T> extends Replace<T> {

    final Handler handler;
    public ReplaceOnUI(DefaultConditionThing thing, Class<T> clazz) {
        super(thing, clazz);
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected void setOrReplaceThing(final Object thing) {
        if (Looper.myLooper() == Looper.getMainLooper())
            super.setOrReplaceThing(thing);
        else
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ReplaceOnUI.super.setOrReplaceThing(thing);
                }
            });
    }
}

