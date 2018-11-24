package ru.nemodev.project.quotes.core.load;

import java.util.List;

import io.reactivex.Observable;

public interface LoadDataListener<T>
{
    Observable<List<T>> getLoadObservable(LoadEventInfo loadEventInfo);

    Observable<List<T>> getLoadObservable();

    default boolean isInfinitySource()
    {
        return true;
    }

    default void onFirstDataLoaded() {}
}
