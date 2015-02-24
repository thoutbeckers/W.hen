package houtbecke.rs.when.robo.event;

public interface Sourceable<T> {

    public Class getSourceClass();
    public T getObject();
    public Integer getResourceId();

}
