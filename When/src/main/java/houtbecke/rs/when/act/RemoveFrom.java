package houtbecke.rs.when.act;

import houtbecke.rs.when.Act;
import houtbecke.rs.when.DefaultConditionThings;

public class RemoveFrom implements Act {
    DefaultConditionThings theThings;
    Class[] classes = null;

    public RemoveFrom(DefaultConditionThings things) {
        this.theThings = things;
    }

    public RemoveFrom(DefaultConditionThings things, Class... classes) {
        this.theThings = things;
        this.classes = classes;
    }

    @Override
    public void act(Object... things) {
        for (Object thing: things)
            if (classes != null)
                for (Class c: classes) {
                    if (c.isInstance(thing)) {
                        theThings.removeThing(thing);
                        break;
                    }
                }
            else
                theThings.removeThing(thing);

    }
}
