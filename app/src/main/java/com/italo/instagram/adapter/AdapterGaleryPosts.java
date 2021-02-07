package com.italo.instagram.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.italo.instagram.R;
import com.italo.instagram.model.Post;

import java.util.List;

public class AdapterGaleryPosts extends RecyclerView.Adapter<AdapterGaleryPosts.MyViewHolder> {

    Context context;
    List<Post> listPosts;

    public AdapterGaleryPosts(Context context, List<Post> listPosts){
        this.context = context;
        this.listPosts = listPosts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_galery_posts,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post = listPosts.get(position);

        Uri url = Uri.parse(post.getListUrlPhotos().get(0));
        Glide.with(context).load(url).into(holder.imagePhoto);

    }

    @Override
    public int getItemCount() {
        return listPosts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imagePhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePhoto = itemView.findViewById(R.id.imageGaleryFotosAdapter);

        }
    }

}
