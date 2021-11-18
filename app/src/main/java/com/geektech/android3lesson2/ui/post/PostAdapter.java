package com.geektech.android3lesson2.ui.post;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geektech.android3lesson2.data.models.Post;
import com.geektech.android3lesson2.databinding.ItemPostBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> posts = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
        notifyDataSetChanged();


    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPostBinding binding = ItemPostBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PostViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.onBind(posts.get(position));
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void deleteItem(int pos) {
        posts.remove(pos);
        notifyDataSetChanged();
    }

    public Post getItem(int pos) {
        return posts.get(pos);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        private ItemPostBinding binding;

        public PostViewHolder(@NonNull ItemPostBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.getRoot().setOnClickListener(view -> {
                onItemClickListener.OnItemClick(getAdapterPosition());
            });

            binding.getRoot().setOnLongClickListener(view -> {
                onItemClickListener.OnLongItemClick(getAdapterPosition());
                return true;
            });
        }

        public void onBind(Post post) {

            binding.tvUserId.setText(String.valueOf(post.getUser()));
            binding.tvTitle.setText(post.getTitle());
            binding.tvDescription.setText(post.getContent());

        }
    }

    public interface OnItemClickListener {
        void OnItemClick(int pos);
        void OnLongItemClick(int pos);
    }

}
