package ru.nemodev.project.quotes.repository.db.category;


import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import io.reactivex.Observable;
import ru.nemodev.project.quotes.entity.category.Category;

@Dao
public interface CategoryRepository
{
    @Transaction
    @Query("SELECT * FROM categories ORDER BY name")
    Observable<List<Category>> getAll();

    @Transaction
    @Query("SELECT * FROM categories ORDER BY name")
    DataSource.Factory<Integer, Category> getAllLiveData();

    @Transaction
    @Query("SELECT * FROM categories c WHERE upper(c.name) like '%' || upper(:name) || '%' ORDER BY c.name")
    DataSource.Factory<Integer, Category> findByNameLiveData(String name);

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void add(List<Category> categories);
}
