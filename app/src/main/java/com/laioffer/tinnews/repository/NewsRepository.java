package com.laioffer.tinnews.repository;

import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.laioffer.tinnews.TinNewsApplication;
import com.laioffer.tinnews.database.TinNewsDatabase;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.model.NewsResponse;
import com.laioffer.tinnews.network.NewsApi;
import com.laioffer.tinnews.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {

    private final NewsApi newsApi;
    private final TinNewsDatabase database;

    public NewsRepository() {
        newsApi = RetrofitClient.newInstance().create(NewsApi.class);
        database = TinNewsApplication.getDatabase();
    }

    public LiveData<NewsResponse> getTopHeadlines(String country) {
        MutableLiveData<NewsResponse> topHeadlinesLiveData = new MutableLiveData<>();
        Call<NewsResponse> responseCall = newsApi.getTopHeadlines(country, 40);
        // new a task, make the call<NewsResponse>
        // add task to queue
        // while(true) { retrofit keep check the queue }
        // if queue has `task`, retrofit do task: call endpoint, parse json , etc
        // once retrofit finish the task
        // callback.onRsponse(response)
        // if (task success) newsResponseCallback.onResponse()
        // else newsResponseCallback.onFailure()
        Log.d("tim", Thread.currentThread().getName() + "1");
        Callback<NewsResponse> newsResponseCallback = new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                Log.d("tim", Thread.currentThread().getName() + "2");
                if (response.isSuccessful()) {
                    topHeadlinesLiveData.setValue(response.body());
                    Log.d("getTopHeadlines", response.body().toString());
                } else {
                    topHeadlinesLiveData.setValue(null);
                    Log.d("getTopHeadlines", response.toString());
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                topHeadlinesLiveData.setValue(null);
                Log.d("getTopHeadlines", t.toString());
            }
        };

        responseCall.enqueue(newsResponseCallback);
        Log.d("tim", Thread.currentThread().getName() + "3");
        return topHeadlinesLiveData;
    }

    public LiveData<NewsResponse> searchNews(String query) {
        MutableLiveData<NewsResponse> everyThingLiveData = new MutableLiveData<>();
        newsApi.getEverything(query, 40)
                .enqueue(
                        new Callback<NewsResponse>() {
                            @Override
                            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                                if (response.isSuccessful()) {
                                    everyThingLiveData.setValue(response.body());
                                } else {
                                    everyThingLiveData.setValue(null);
                                }
                            }

                            @Override
                            public void onFailure(Call<NewsResponse> call, Throwable t) {
                                everyThingLiveData.setValue(null);
                            }
                        });
        return everyThingLiveData;
    }

    public LiveData<Boolean> favoriteArticle(Article article) {
        MutableLiveData<Boolean> resultLiveData = new MutableLiveData<>();
//        database.articleDao().saveArticle(article);
        Log.d("Tim test", Thread.currentThread().getName() + " 1");
        new FavoriteAsyncTask(database, resultLiveData).execute(article);
        Log.d("Tim test", Thread.currentThread().getName() + " 6");
        return resultLiveData;
    }

    public LiveData<List<Article>> getAllSavedArticles() {
//        MutableLiveData<List<Article>> mutableLiveData = new MutableLiveData();
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                mutableLiveData.postValue(database.articleDao().getAllArticles());
//            }
//        });
////        mutableLiveData.setValue(articles);
//        return mutableLiveData;
        return database.articleDao().getSavedAllArticles();
    }

    public void deleteSavedArticle(Article article) {
        AsyncTask.execute(() -> database.articleDao().deleteArticle(article));
    }

    private static class FavoriteAsyncTask extends AsyncTask<Article, Integer, Boolean> {

        private final TinNewsDatabase database;
        private final MutableLiveData<Boolean> liveData;

        private FavoriteAsyncTask(TinNewsDatabase database, MutableLiveData<Boolean> liveData) {
            this.database = database;
            this.liveData = liveData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Tim test", Thread.currentThread().getName() + " 2");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("Tim test", Thread.currentThread().getName() + " 4");
        }

        @Override
        protected Boolean doInBackground(Article... articles) {
            Log.d("Tim test", Thread.currentThread().getName() + " 3");
            Article article = articles[0];
            try {
                database.articleDao().saveArticle(article);
                publishProgress(10);
            } catch (Exception e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            Log.d("Tim test", Thread.currentThread().getName() + " 5");
            liveData.setValue(success);
        }
    }

}
