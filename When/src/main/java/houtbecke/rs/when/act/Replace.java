package houtbecke.rs.when.act;

import java.util.Arrays;

import houtbecke.rs.when.Act;
import houtbecke.rs.when.DefaultConditionThing;
import houtbecke.rs.when.DefaultConditionThings;

public class Replace<T> implements Act {

    Class<T> clazz;
    DefaultConditionThing theThing;


    public Replace(DefaultConditionThing thing, Class<T> clazz) {
        this.theThing = thing;
        this.clazz = clazz;
    }

    @Override
    public void act(Object... things) {
        for (Object thing: things)
            if (clazz.isInstance(thing)) {
                //noinspection unchecked
                this.theThing.setOrReplaceThing(thing);
                break;
            }
    }

    @Override
    public String toString() {
        return "AddTo{" +
                "classes=" + clazz +
                ", theThing(" + Arrays.deepToString(theThing.getThings().toArray())+")"+
                '}';
    }
}
