package houtbecke.rs.when.robo.act;

import android.os.Handler;
import android.os.Looper;

import houtbecke.rs.when.ObservableThings;
import houtbecke.rs.when.act.Clear;

public class ClearOnUi extends Clear {
    final Handler handler;

    public ClearOnUi(ObservableThings thing) {
        super(thing);
        handler = new Handler(Looper.getMainLooper());
    }

    public void act(final Object... things) {
        if (Looper.getMainLooper() == Looper.myLooper())
            super.act(things);
        else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    ClearOnUi.super.act(things);
                }
            });
        }
    }
}
