package com.laioffer.tinnews.repository;


import android.os.Handler;
import android.os.Looper;

import com.laioffer.tinnews.model.Article;

public abstract class MyAsyncTask<Params, Progress, Result> {
    private Handler handler = new Handler(Looper.getMainLooper());
    protected void onPreExecute() {

    }

    protected void onProgressUpdate(Progress progress) {

    }

    public void publishProgress(Progress progress) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                onProgressUpdate(progress);
            }
        });
    }

    protected abstract Result doInBackground(Params... param);

    protected void onPostExecute(Result result) {

    }

    protected void execute(Params params) {
        onPreExecute();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Result result = doInBackground(params);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onPostExecute(result);
                    }
                });
            }
        }).start();
    }

    private static class FavoriteAsyncTask extends MyAsyncTask<Article, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Article... articles) {
            return null;
        }
    }
}
