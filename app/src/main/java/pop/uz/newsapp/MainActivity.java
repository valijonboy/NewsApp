package pop.uz.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;

import pop.uz.newsapp.adapter.ArticleAdapter;
import pop.uz.newsapp.model.Article;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>>,
        ArticleAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Article> articles;
    private ArticleAdapter adapter;

    private static final String THEGUARDIAN_NEWS_URL = "https://content.guardianapis.com/search?q=politic&api-key=c094492e-9f9e-46a2-987c-8cf4d7412fa7";
    private static final int LOADER_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter = new ArticleAdapter(new ArrayList<Article>(), this);
        recyclerView.setAdapter(adapter);
        articles = new ArrayList<>();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(this, THEGUARDIAN_NEWS_URL);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Article>> loader) {
        adapter.setData(new ArrayList<Article>());
    }

    @Override
    public void onItemClick(int position) {
        String articleUlr = new ArrayList<Article>().get(position).getWebUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleUlr));
        startActivity(intent);
    }
}