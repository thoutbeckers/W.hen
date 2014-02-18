package houtbecke.rs.when;

public abstract class GeneralAct implements Act {

    @Override
    public void act(Object... ignore) {
        act();

    }

    public abstract void act();
}
