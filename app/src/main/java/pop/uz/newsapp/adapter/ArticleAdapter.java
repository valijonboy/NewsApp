package pop.uz.newsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pop.uz.newsapp.R;
import pop.uz.newsapp.model.Article;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    private ArrayList<Article> articles;
    private OnItemClickListener mOnItemClickListener;

    public ArticleAdapter(ArrayList<Article> articles, OnItemClickListener onItemClickListener) {
        this.articles = articles;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ArticleHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {

        Article article = articles.get(position);

        holder.title.setText(article.getTitle());
        holder.nameSection.setText(article.getNameSection());
        holder.publicDate.setText(article.getPublicDate());
    }

    @Override
    public int getItemCount() {
        return articles != null ? articles.size() : 0;
    }

    public class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, nameSection, publicDate;
        OnItemClickListener onItemClickListener;

        public ArticleHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            title = itemView.findViewById(R.id.title_article);
            nameSection = itemView.findViewById(R.id.name_section);
            publicDate = itemView.findViewById(R.id.public_date);

            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Article article = articles.get(getAdapterPosition());
            String webUrl = article.getWebUrl();
            onItemClickListener.onItemClick(webUrl);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String webUrl);
    }

    public void setData(ArrayList<Article> data) {
        this.articles = data;
        notifyDataSetChanged();
    }
}
