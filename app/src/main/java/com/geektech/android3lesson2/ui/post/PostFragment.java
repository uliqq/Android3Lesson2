package com.geektech.android3lesson2.ui.post;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geektech.android3lesson2.App;
import com.geektech.android3lesson2.R;
import com.geektech.android3lesson2.data.models.Post;
import com.geektech.android3lesson2.databinding.FragmentPostBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostFragment extends Fragment implements PostAdapter.OnItemClickListener {

    private FragmentPostBinding binding;
    private NavController controller;
    private NavHostFragment navHostFragment;
    private PostAdapter adapter;
//    private AlertDialog alertDialog;
//

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostBinding.inflate(getLayoutInflater(), container, false);
        navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        controller = navHostFragment.getNavController();
        adapter = new PostAdapter();
        adapter.setOnItemClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.navigate(R.id.action_postFragment_to_formFragment);
            }
        });

        binding.recycler.setAdapter(adapter);

        App.api.getPosts().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPosts(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void OnItemClick(int pos) {
        Bundle bundle = new Bundle();
        Post post = adapter.getItem(pos);
        bundle.putSerializable("post", post);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.formFragment, bundle);

    }

    @Override
    public void OnLongItemClick(int pos) {
        AlertDialog.Builder alert = new AlertDialog.Builder(requireActivity());
        alert.setTitle("Attention!");
        alert.setMessage("Do you want to delete this post?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                App.api.deletePost(adapter.getItem(pos).getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.e("TAG", "Delete: " + adapter.getItem(pos));
                            adapter.deleteItem(pos);
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("TAG", "Failed!");
                    }
                });
            }
        });
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }
}