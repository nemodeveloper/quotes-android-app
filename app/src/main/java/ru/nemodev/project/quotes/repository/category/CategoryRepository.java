package ru.nemodev.project.quotes.repository.category;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.Single;
import ru.nemodev.project.quotes.entity.category.Category;

@Dao
public interface CategoryRepository
{
    @Transaction
    @Query("SELECT * FROM categories ORDER BY name")
    Single<List<Category>> getAll();

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<Category> categories);
}
