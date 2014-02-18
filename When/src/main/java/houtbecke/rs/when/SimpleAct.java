package houtbecke.rs.when;

import houtbecke.rs.when.Act;

public abstract class SimpleAct implements Act {

    @Override
    public void act(Object... things) {
        for (Object thing: things)
            act(thing);
    }

    public abstract void act(Object thing);
}
