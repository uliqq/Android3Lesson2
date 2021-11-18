package com.geektech.android3lesson2.ui.form;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geektech.android3lesson2.App;
import com.geektech.android3lesson2.R;
import com.geektech.android3lesson2.data.models.Post;
import com.geektech.android3lesson2.databinding.FragmentFormBinding;
import com.geektech.android3lesson2.ui.post.PostAdapter;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormFragment extends Fragment {

    private FragmentFormBinding binding;
    private PostAdapter adapter;
    private Post post;
    private NavController controller;
    private NavHostFragment navHostFragment;
    private boolean isUpdate = false;

    public FormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormBinding.inflate(getLayoutInflater(), container, false);
        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        controller = navHostFragment.getNavController();

        binding.btnCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post(
                        binding.etTitle.getText().toString(),
                        binding.etDescription.getText().toString(),
                        Integer.parseInt(binding.etUserId.getText().toString()),
                        35
                );
                App.api.createPost(post).enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        if (response.isSuccessful()) {
                            controller.popBackStack();
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
                    }
                });
            }
        });
        adapter = new PostAdapter();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            post = (Post) getArguments().getSerializable("post");
            isUpdate = true;
            setPost();
        }

        binding.btnCreatePost.setOnClickListener(view1 -> {
            if (!isUpdate) {
                createPost();
            } else {
                updatePost();
            }
        });
    }

    private void updatePost() {
        App.api.putPost(getPost().getUser(), getPost()).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    Log.e("TAG", post.getTitle() + " is updated!");
                    controller.popBackStack();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                    Log.e("TAG", "Update is failed!");
            }
        });
    }

    private void createPost() {
        App.api.createPost(getPost()).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.isSuccessful()) {
                    controller.popBackStack();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Log.e("TAG", "Failed!");

            }
        });
    }

    private Post getPost() {
        Post post = new Post(
                binding.etTitle.getText().toString(),
                binding.etDescription.getText().toString(),
                Integer.parseInt(String.valueOf(binding.etUserId.getText())), 35);
        return post;
    }

    private void setPost() {

        binding.etUserId.setText(String.valueOf(post.getUser()));
        binding.etTitle.setText(post.getTitle());
        binding.etDescription.setText(post.getContent());
    }
}