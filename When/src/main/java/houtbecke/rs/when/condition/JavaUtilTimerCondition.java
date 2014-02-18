package houtbecke.rs.when.condition;

import java.util.TimerTask;

import houtbecke.rs.when.BasePushCondition;

public class JavaUtilTimerCondition extends BasePushCondition implements Timer {

    java.util.Timer timer;
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            event();
        }
    };

    public JavaUtilTimerCondition() {
        timer = new java.util.Timer();
    }

    long start = -1, repeat;

    @Override
    public void configure(long start, long repeat) {
        this.start = start;
        this.repeat = repeat;

    }

    @Override
    public void start() {
        if (start == -1)
            throw new IllegalStateException("Call configure first");
        timer.schedule(task, start, repeat);
    }

    @Override
    public void stop() {
        task.cancel();
    }
}
