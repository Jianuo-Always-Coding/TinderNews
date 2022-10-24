package com.laioffer.tinnews.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.laioffer.tinnews.model.Article;

import java.util.List;

@Dao
public interface ArticleDao {
    @Insert
    void saveArticle(Article article);

    @Query("SELECT * FROM Article")
    List<Article> getAllArticles();

    @Query("SELECT * FROM Article")
    LiveData<List<Article>> getSavedAllArticles();

    @Delete
    void deleteArticle(Article article);
}
