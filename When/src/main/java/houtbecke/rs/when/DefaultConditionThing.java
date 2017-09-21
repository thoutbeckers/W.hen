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

    @SuppressWarnings("LoopStatementThatDoesntLoop") // we only want the first entry as there is either 1 or 0 entries
    @Override
    public T get() {

        for (T t: things) {
            return t;
        }
        return null;
    }

    @Override
    public void addThing(T thing) {
        throw new RuntimeException("Cannot use add method for single thing");
    }

}
