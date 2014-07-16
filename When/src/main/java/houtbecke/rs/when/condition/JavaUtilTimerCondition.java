package houtbecke.rs.when.condition;

import java.util.TimerTask;
import java.util.Timer;

import houtbecke.rs.when.BasePushCondition;

public class JavaUtilTimerCondition extends BasePushCondition implements houtbecke.rs.when.condition.Timer {

    final Timer timer;
    TimerTask task;

    public JavaUtilTimerCondition() {
        timer = new Timer();
    }

    boolean started = false;

    public final int NOT_CONFIGURED = -1;

    long start = NOT_CONFIGURED, repeat;

    @Override
    public void configure(long start, long repeat) {
        this.start = start;
        this.repeat = repeat;
    }

   public void restart() {
        if (started) {
            
            this.stop();
            this.start();
        }
    }

    public synchronized void start() {
        // ensure old task no longer is scheduled.
        if (task != null)
            task.cancel();

        task = new TimerTask() {
            @Override
            public void run() {
                event();
            }
        };

        if (start == NOT_CONFIGURED)
            throw new IllegalStateException("Call configure() first");
        timer.schedule(task, start, repeat);
        started = true;
    }

    @Override
    public void stop() {
        if (task != null)
            task.cancel();
        started = false;
    }
}
