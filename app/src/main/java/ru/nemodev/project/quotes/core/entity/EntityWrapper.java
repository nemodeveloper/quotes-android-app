package ru.nemodev.project.quotes.core.entity;


public class EntityWrapper<T> {
    private final T entity;
    private final String errorMessage;

    public EntityWrapper(T entity, String errorMessage) {
        this.entity = entity;
        this.errorMessage = errorMessage;
    }

    public T getEntity() {
        return entity;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static <T> EntityWrapper<T> of(T entity) {
        return of(entity, null);
    }

    public static <T> EntityWrapper<T> of(String errorMessage) {
        return of(null, errorMessage);
    }

    public static <T> EntityWrapper<T> of(T entity, String errorMessage) {
        return new EntityWrapper<>(entity, errorMessage);
    }
}
