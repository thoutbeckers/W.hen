package houtbecke.rs.when.condition;

import java.util.TimerTask;

import houtbecke.rs.when.BasePushCondition;

public class JavaUtilTimerCondition extends BasePushCondition implements Timer {

    java.util.Timer timer;
    TimerTask task;

    public JavaUtilTimerCondition() {
        timer = new java.util.Timer();
    }

    long start = -1, repeat;
    boolean started = false;


    @Override
    public void configure(long start, long repeat) {
        this.start = start;
        this.repeat = repeat;

    }

    @Override
    public void restart() {
        if (started) {
            this.stop();
            this.start();
        }
    }

    @Override
    public void start() {
        if (start == -1)
            throw new IllegalStateException("Call configure first");
        task  = new TimerTask() {
            @Override
            public void run() {
                event();
            }
        };

        timer.schedule(task, start, repeat);
        started = true;
    }

    @Override
    public void stop() {
        task.cancel();
        started = false;
    }
}
