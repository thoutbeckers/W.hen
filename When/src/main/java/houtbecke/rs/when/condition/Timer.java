package houtbecke.rs.when.condition;

import houtbecke.rs.when.Condition;

public interface Timer extends Condition {

    public void configure(long tillFirst);
    public void configure(long tillFirst, long recurring);
    public void start();
    public void stop();
    public void restart();

}
