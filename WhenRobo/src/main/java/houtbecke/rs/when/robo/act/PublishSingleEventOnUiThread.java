package houtbecke.rs.when.robo.act;

import com.squareup.otto.Bus;

import javax.inject.Inject;

/**
 * Use this Act to post a single event to the Bus from the UI Thread.
 * The event posted will be the event returned by the overridden event() method.
 * This method is called only once after creation of this Act.
 *
 * @param <T> The class type of your event
 */
public abstract class PublishSingleEventOnUiThread<T> extends PublishEvent {
    final T event;

    @Inject
    public PublishSingleEventOnUiThread(Bus bus) {
        super(bus, true, false);
        event = event();
    }

    protected abstract T event();

    @Override
    public void act(Object... things) {
        super.act(event);
    }
}
