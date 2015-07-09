package houtbecke.rs.when.robo.act;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.*;

import houtbecke.rs.when.Act;
import houtbecke.rs.when.TypedAct;
import houtbecke.rs.when.robo.condition.event.ActivityResume;

public class PublishEvent extends TypedAct {

    @Inject
    Bus bus;
    final Map<Class, Object> eventTypeValueMap;
    final boolean saveLastValue;
    final boolean postAll;

    final Handler handler;

    public PublishEvent(Bus bus, boolean fromUiThread, boolean saveLastValue, Class... eventClasses) {
        this(null, bus, fromUiThread, saveLastValue, eventClasses);
    }

    /**
     * Use this constructor if you will use dependency injection beforeRegisterUseContext
     * @param context
     * @param fromUiThread
     * @param saveLastValue
     * @param eventClasses
     */
    public PublishEvent(Context context, boolean fromUiThread, boolean saveLastValue, Class... eventClasses) {
        this(context, null, fromUiThread, saveLastValue, eventClasses);
    }

    public PublishEvent(Context context, Bus bus, boolean fromUiThread, boolean saveLastValue, Class... eventClasses) {
        handler = fromUiThread ? new Handler(Looper.getMainLooper()) : null;
        this.bus = bus;
        this.saveLastValue = saveLastValue;

        eventTypeValueMap = fromUiThread ? new LinkedHashMap<Class, Object>(0) : Collections.synchronizedMap(new LinkedHashMap<Class, Object>(0));

        postAll = eventClasses == null || eventClasses.length == 0;

        if (!postAll)
            for (Class eventClass: eventClasses)
                eventTypeValueMap.put(eventClass, null);

        if (context != null) beforeRegisterUseContext(context);

        if (bus == null)
            throw new RuntimeException("You must supply a non-null Bus or supply a Bus through Dependency Injection using beforeRegisterUseContext()");

        bus.register(this);
    }

    protected void beforeRegisterUseContext(Context context) {}

    public <T> T getLastValue(Class<? extends T> c) {
        return (T) eventTypeValueMap.get(c);
    }

    public <T> T getLastValue(Class<? extends T> c, T defaultValue) {
        T last = getLastValue(c);
        return last == null ? defaultValue : last;
    }

    public boolean hasLastValue(Class c) {
        return getLastValue(c) != null;
    }


    @Override
    public void defaultAct(Object... things) {

        for (final Object thing: things) {

            if (handler != null){
                if (handler.getLooper() == Looper.myLooper())
                    post(thing);
                else
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            post(thing);
                        }
                    });
            }
            else
                post(thing);
        }
    }

    protected void post(Object thing) {
        if (postAll || eventTypeValueMap.containsKey(thing.getClass())) {

            if (saveLastValue)
                eventTypeValueMap.put(thing.getClass(), thing);

            bus.post(thing);
        }
    }

}

