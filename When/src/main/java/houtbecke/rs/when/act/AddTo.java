package houtbecke.rs.when.act;

import java.util.Arrays;

import houtbecke.rs.when.Act;
import houtbecke.rs.when.ConditionThings;

public class AddTo implements Act {

    Class[] classes = null;
    ConditionThings theThings;

    public AddTo(ConditionThings things) {
        this.theThings = things;
    }

    public AddTo(ConditionThings things, Class... classes) {
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
                        addThing(thing);
                        break;
                    }
                }
            else
                //noinspection unchecked
                addThing(thing);
    }

    @SuppressWarnings("unchecked")
    protected void addThing(Object thing) {
        theThings.addThing(thing);
    }

    @Override
    public String toString() {
        return "AddTo{" +
                "classes=" + Arrays.toString(classes) +
                ", theThings(" + theThings +")"+
                '}';
    }
}
