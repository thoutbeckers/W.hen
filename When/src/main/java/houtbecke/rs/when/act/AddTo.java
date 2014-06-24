package houtbecke.rs.when.act;

import java.util.Arrays;

import houtbecke.rs.when.Act;
import houtbecke.rs.when.DefaultConditionThings;

public class AddTo implements Act {

    Class[] classes = null;
    DefaultConditionThings theThings;

    public AddTo(DefaultConditionThings things) {
        this.theThings = things;
    }

    public AddTo(DefaultConditionThings things, Class... classes) {
        this.theThings = things;
        this.classes = classes;
    }

    @Override
    public void act(Object... things) {
        for (Object thing: things)
            if (classes != null)
                for (Class c: classes) {
                    if (c.isInstance(thing)) {
                        //noinspection unchecked
                        this.theThings.addThing(thing);
                        break;
                    }
                }
            else
                //noinspection unchecked
                this.theThings.addThing(thing);
    }

    @Override
    public String toString() {
        return "AddTo{" +
                "classes=" + Arrays.toString(classes) +
                ", theThings(" + theThings.getThings().size() +")"+
                '}';
    }
}
