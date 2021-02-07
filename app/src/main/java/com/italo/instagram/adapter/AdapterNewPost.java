package com.italo.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.italo.instagram.R;
import com.italo.instagram.model.Photo;

import java.util.List;

public class AdapterNewPost extends RecyclerView.Adapter<AdapterNewPost.MyViewHolder> {

    Context context;
    List<Photo> listPost;

    public AdapterNewPost(Context context, List<Photo> posts) {

        this.context = context;
        this.listPost = posts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_new_post, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Photo photo = listPost.get(position);
        holder.imagePhoto.setImageBitmap(photo.getImageBitmap());

    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePhoto = itemView.findViewById(R.id.imageAdpterNovaPostPhoto);
        }
    }

}
