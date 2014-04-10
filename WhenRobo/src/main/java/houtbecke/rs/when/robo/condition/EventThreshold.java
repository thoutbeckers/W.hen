package houtbecke.rs.when.robo.condition;

import com.squareup.otto.Bus;

import java.util.Iterator;
import java.util.TreeSet;

import javax.inject.Inject;

import houtbecke.rs.when.BasePushCondition;

/**
 * A Condition which gets triggered when an amount of events occurs in a specific time frame
 *
 * Events can be anything, simple call one of the addEventAndTriggerIfTooManyEvents methods.
 *
 * For convenience's sake when overriding this class, a Bus can be passed to the constructor
 * where it will be automatically registered
 */
public class EventThreshold extends BasePushCondition {

    protected Bus bus;
    protected int numEvents, inTime;
    boolean requiresReset, needsReset = false;

    public EventThreshold(int numEvents, int inTime) {
        this(null, numEvents, inTime, false);
    }
    public EventThreshold(int numEvents, int inTime, boolean requiresReset) {
        this(null, numEvents, inTime, requiresReset);
    }
    public EventThreshold(Bus bus, int numEvents, int inTime) {
        this(bus, numEvents, inTime, false);
    }

    public EventThreshold(Bus bus, int numEvents, int inTime, boolean requiresReset) {
        this.bus = bus;
        this.numEvents = numEvents;
        this.inTime = inTime;
        this.requiresReset = requiresReset;
        if (bus != null)
            bus.register(this);
    }


    TreeSet<Long> eventTimes = new TreeSet<>();

    /**
     * Add another event to count at the current timestamp
     *
     * @return true if the condition was triggered by this event
     */
    public boolean addEventAndTriggerIfTooManyEvents() {
        return addEventAndTriggerIfTooManyEvents(System.currentTimeMillis());
    }

    /**
     * Add another event at the specified timestamp.
     *
     * The condition will trigger if there are more than numEvents in indicated range
     * counting back by inTime from the the specified timestamp.
     *
     * @param timeStamp the timestamp at which an event should be added and from which this method will look back to see if the condition should be triggered.
     * @return true if the condition was triggered by this event
     */
    public synchronized boolean addEventAndTriggerIfTooManyEvents(long timeStamp) {
        if (needsReset)
            return false;

        // avoid duplicate entries
        while (eventTimes.contains(timeStamp))
            timeStamp++;

        eventTimes.add(timeStamp);


        Iterator<Long> it = eventTimes.iterator();
        boolean stop = false;

        Long barrier = timeStamp - inTime;

        while(it.hasNext() && !stop) {
            if (it.next() < barrier)
                it.remove();
            else
                stop = true;
        }

        if (eventTimes.size() >= numEvents) {
            if (requiresReset)
                needsReset = true;

            event();
            return true;
        }
        return false;
    }

    public void reset() {
        needsReset = false;
    }
}
