package houtbecke.rs.when.act;

import houtbecke.rs.when.Act;
import houtbecke.rs.when.DefaultConditionThing;
import houtbecke.rs.when.DefaultConditionThings;

public class Clear implements Act {
    DefaultConditionThing theThing;

    public Clear(DefaultConditionThing thing) {
        this.theThing = thing;
    }


    @Override
    public void act(Object... things) {
        theThing.clearThing();
    }
}
