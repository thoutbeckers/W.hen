package houtbecke.rs.when;

public class DefaultConditionThing<T> extends DefaultConditionThings<T> implements Thing<T> {

    public DefaultConditionThing(String name) {
        super(name);
    }

    @Override
    public void setOrReplaceThing(T thing) {
        clear();
        super.addThing(thing);
    }

    @Override
    public void addThing(T thing) {
        throw new RuntimeException("Cannot use add method for single thing");
    }

}
