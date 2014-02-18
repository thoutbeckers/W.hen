package houtbecke.rs.when.logic;

import houtbecke.rs.when.W;

public abstract class AbstractLogic implements Logic {

    public final void start() {
        logic();
        W.ork();
    }

    public abstract void logic();
}
