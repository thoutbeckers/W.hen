package houtbecke.rs.when.robo.act;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;

import houtbecke.rs.when.TypedAct;
import houtbecke.rs.when.robo.OnUiThread;

public class AndroidTypedAct extends TypedAct {

    Handler handler = null;

    @Override
    public void invokeMethod(final Method cachedMethod, final Object... parameters) {

        if (cachedMethod.getAnnotation(OnUiThread.class) != null && Looper.myLooper() != Looper.getMainLooper()) {
            // lazily initialize a Handler if ever needed
            // if two are ever concurrently created it doesn't matter so no synchronization is needed

            if (handler == null)
                handler = new Handler(Looper.getMainLooper());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    AndroidTypedAct.super.invokeMethod(cachedMethod, parameters);
                }
            });

        } else
            super.invokeMethod(cachedMethod, parameters);
    }
}
