package factory;

public abstract class BaseFactory<T> {
    public abstract T create(String id, String name, String additionalInfo);
}