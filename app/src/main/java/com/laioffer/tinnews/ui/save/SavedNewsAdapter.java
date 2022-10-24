package com.laioffer.tinnews.ui.save;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.laioffer.tinnews.R;
import com.laioffer.tinnews.databinding.SavedNewsItemBinding;
import com.laioffer.tinnews.model.Article;

import java.util.ArrayList;
import java.util.List;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.SavedNewsViewHolder> {

    private List<Article> articles = new ArrayList<>();

    public void setArticles(List<Article> newsList) {
        articles.clear();
        articles.addAll(newsList);
        notifyDataSetChanged();
    }
    // recyclerview -> ViewGroup  -> itemView
    // Adapter -> ViewHolder(itemView)
    // LayoutManager ->

// 错误示范
//    private SaveViewModel viewModel;
//    private SaveFragment fragment;
//
//    public void setViewModel(SaveViewModel viewModel) {
//        this.viewModel = viewModel;
//    }
//
//    public void setFragment(SaveFragment fragment) {
//        this.fragment = fragment;
//    }

    interface ItemCallback {
        void onOpenDetails(Article article);
        void onRemoveFavorite(Article article);
    }

    private ItemCallback itemCallback;

    public void setItemCallback(ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }

    @NonNull
    @Override
    public SavedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_news_item, parent, false);
        return new SavedNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedNewsViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.authorTextView.setText(article.author);
        holder.descriptionTextView.setText(article.description);
        holder.favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCallback.onRemoveFavorite(article);
//                fragment.deleteArticle(article);
//                viewModel.deleteSavedArticle(article);
            }
        });
        holder.itemView.setOnClickListener(v -> {
            itemCallback.onOpenDetails(article);
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class SavedNewsViewHolder extends RecyclerView.ViewHolder {
        TextView authorTextView;
        TextView descriptionTextView;
        ImageView favoriteIcon;

        public SavedNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            SavedNewsItemBinding binding = SavedNewsItemBinding.bind(itemView);
            authorTextView = binding.savedItemAuthorContent;
            descriptionTextView = binding.savedItemDescriptionContent;
            favoriteIcon = binding.savedItemFavoriteImageView;
        }
    }
}
