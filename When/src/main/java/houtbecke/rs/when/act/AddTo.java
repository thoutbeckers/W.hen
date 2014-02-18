package houtbecke.rs.when.act;

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
                        this.theThings.addThing(thing);
                        break;
                    }
                }
            else
                this.theThings.addThing(thing);
    }
}
