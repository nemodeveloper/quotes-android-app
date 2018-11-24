package ru.nemodev.project.quotes.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.internal.CategoryInternal;

@Dao
public interface CategoryInternalDAO
{
    @Query("SELECT * FROM categories")
    Single<List<CategoryInternal>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<CategoryInternal> categoryInternals);
}
