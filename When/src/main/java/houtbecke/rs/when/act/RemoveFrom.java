package houtbecke.rs.when.act;

import houtbecke.rs.when.Act;
import houtbecke.rs.when.ConditionThings;

public class RemoveFrom implements Act {
    ConditionThings theThings;
    Class[] classes = null;

    public RemoveFrom(ConditionThings things) {
        this.theThings = things;
    }

    public RemoveFrom(ConditionThings things, Class... classes) {
        this.theThings = things;
        this.classes = classes;
    }

    @Override
    public void act(Object... things) {
        for (Object thing: things)
            if (classes != null)
                for (Class c: classes) {
                    if (c.isInstance(thing)) {
                        removeThing(thing);
                        break;
                    }
                }
            else
                removeThing(thing);

    }

    @SuppressWarnings("unchecked")
    protected void removeThing(Object thing) {
        theThings.removeThing(thing);
    }
}
