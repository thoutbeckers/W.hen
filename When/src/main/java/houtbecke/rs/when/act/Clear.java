package houtbecke.rs.when.act;

import houtbecke.rs.when.Act;
import houtbecke.rs.when.ObservableThings;
import houtbecke.rs.when.Things;

public class Clear implements Act {
    ObservableThings theThing;

    public Clear(ObservableThings thing) {
        this.theThing = thing;
    }

    @Override
    public void act(Object... things) {
        theThing.clear();
    }
}
