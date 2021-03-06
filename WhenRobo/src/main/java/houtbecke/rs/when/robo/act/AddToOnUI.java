package houtbecke.rs.when.robo.act;

import android.os.Handler;
import android.os.Looper;

import houtbecke.rs.when.ConditionThings;
import houtbecke.rs.when.act.AddTo;

public class AddToOnUI extends AddTo {
    final Handler handler;


    public AddToOnUI(ConditionThings things) {
        super(things);
        handler = new Handler(Looper.getMainLooper());

    }

    public AddToOnUI(ConditionThings things, Class... classes) {
        super(things, classes);
        handler = new Handler(Looper.getMainLooper());
    }



    @Override
    protected void addThing(final Object thing) {
        if (Looper.getMainLooper() == Looper.myLooper())
            super.addThing(thing);
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    AddToOnUI.super.addThing(thing);
                }
            });
        }
    }
}
