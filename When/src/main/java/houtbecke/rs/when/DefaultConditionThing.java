package houtbecke.rs.when;

public class DefaultConditionThing<T> extends DefaultConditionThings<T> implements ObservableThing<T> {

    @Override
    public void setOrReplaceThing(T thing) {
        clearThing();
        super.addThing(thing);
    }

    @Override
    public void clearThing() {
        if (getThings().size() > 0)
            for (T t : getThings())
                super.removeThing(t);
    }

    @Override
    public void addThing(T thing) {
        throw new RuntimeException("Cannot use add method for single thing");
    }

    @Override
    public void removeThing(T thing) {
        throw new RuntimeException("Cannot use remove method for single thing");
    }
}
