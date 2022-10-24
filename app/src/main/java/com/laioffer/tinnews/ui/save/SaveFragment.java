package com.laioffer.tinnews.ui.save;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.laioffer.tinnews.databinding.FragmentSaveBinding;
import com.laioffer.tinnews.model.Article;
import com.laioffer.tinnews.repository.NewsRepository;
import com.laioffer.tinnews.repository.NewsViewModelFactory;

public class SaveFragment extends Fragment implements SavedNewsAdapter.ItemCallback {
    private FragmentSaveBinding binding;
    private SaveViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSaveBinding.inflate(inflater, container, false);
        return binding.getRoot();
        //return inflater.inflate(R.layout.fragment_save, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SavedNewsAdapter adapter = new SavedNewsAdapter();
        adapter.setItemCallback(new SavedNewsAdapter.ItemCallback() {
            @Override
            public void onOpenDetails(Article article) {
               SaveFragmentDirections.ActionNavigationSaveToNavigationDetails direction =
                       SaveFragmentDirections.actionNavigationSaveToNavigationDetails(article);
               NavHostFragment.findNavController(SaveFragment.this).navigate(direction);
            }

            @Override
            public void onRemoveFavorite(Article article) {
                viewModel.deleteSavedArticle(article);
            }
        });
//        adapter.setItemCallback(this);

        binding.newsSavedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.newsSavedRecyclerView.setAdapter(adapter);

        NewsRepository newsRepository = new NewsRepository();
        viewModel = new ViewModelProvider(this, new NewsViewModelFactory(newsRepository))
                .get(SaveViewModel.class);
//        adapter.setViewModel(viewModel);
//        adapter.setFragment(this);
        viewModel.getAllSavedArticles().observe(getViewLifecycleOwner(), savedArticles -> {
            if (savedArticles != null) {
                Log.d("SaveFragment", savedArticles.toString());
                adapter.setArticles(savedArticles);
            }
        });
    }

    public void deleteArticle(Article article) {
        viewModel.deleteSavedArticle(article);
    }

    @Override
    public void onOpenDetails(Article article) {

    }

    @Override
    public void onRemoveFavorite(Article article) {
        // 和anonymous 相同
    }


//    public class InnerClass implements SavedNewsAdapter.ItemCallback {
//
//        @Override
//        public void onOpenDetails(Article article) {
//            viewModel.deleteSavedArticle(article);
//        }
//
//        @Override
//        public void onRemoveFavorite(Article article) {
//
//        }
//    }
}