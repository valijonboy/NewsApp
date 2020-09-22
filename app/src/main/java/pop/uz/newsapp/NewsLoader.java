package pop.uz.newsapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;

import pop.uz.newsapp.model.Article;
import pop.uz.newsapp.utils.QueryUtils;

public class NewsLoader extends AsyncTaskLoader<ArrayList<Article>> {

    private String mUrl;
    public NewsLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<Article> loadInBackground() {

        if (mUrl == null){
            return null;
        }

        ArrayList<Article> articles = QueryUtils.fetchNewsData(mUrl);
        return articles;
    }
}
