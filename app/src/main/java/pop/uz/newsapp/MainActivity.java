package pop.uz.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import pop.uz.newsapp.adapter.ArticleAdapter;
import pop.uz.newsapp.databinding.ActivityMainBinding;
import pop.uz.newsapp.model.Article;
import pop.uz.newsapp.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>>,
        ArticleAdapter.OnItemClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private ArrayList<Article> articles;
    private ArticleAdapter adapter;
    private ActivityMainBinding binding;

    private static final String THEGUARDIAN_NEWS_URL = "https://content.guardianapis.com/search?q=all&api-key=c094492e-9f9e-46a2-987c-8cf4d7412fa7";
    private static final int LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerview.setHasFixedSize(true);

        adapter = new ArticleAdapter(new ArrayList<Article>(), this);
        binding.recyclerview.setAdapter(adapter);
        articles = new ArrayList<>();

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.recyclerview.addItemDecoration(itemDecoration);

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            binding.progressbar.setVisibility(View.GONE);
            binding.tvEmpty.setText(R.string.no_internet_conn);
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    private void setEmptyView(ArrayList<Article> data) {
        if (data.isEmpty()) {
            binding.recyclerview.setVisibility(View.GONE);
            binding.tvEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerview.setVisibility(View.VISIBLE);
            binding.tvEmpty.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int id, @Nullable Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String pageSize = sharedPreferences.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default)
        );

        String category = sharedPreferences.getString(
                getString(R.string.settings_category_key),
                getString(R.string.settings_category_default)
        );

        Uri baseUri = Uri.parse(THEGUARDIAN_NEWS_URL);
        Uri.Builder builder = baseUri.buildUpon();

        builder.appendQueryParameter("order-by", orderBy);
        builder.appendQueryParameter("page-size", pageSize);
        builder.appendQueryParameter("section", category);

        return new NewsLoader(this, builder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
        binding.progressbar.setVisibility(View.GONE);
        setEmptyView(data);
        binding.tvEmpty.setText(R.string.no_news);
        adapter.setData(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Article>> loader) {
        adapter.setData(new ArrayList<Article>());
    }

    @Override
    public void onItemClick(String webUrl) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
        startActivity(intent);
    }

    @Override
    // This method initialize the contents of the Activity's options menu.
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.settings_order_by_key))) {
            adapter.setData(articles);

            binding.tvEmpty.setVisibility(View.GONE);
            binding.progressbar.setVisibility(View.VISIBLE);
            getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }
}