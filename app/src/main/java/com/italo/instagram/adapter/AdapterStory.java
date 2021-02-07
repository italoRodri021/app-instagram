package com.italo.instagram.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.italo.instagram.R;
import com.italo.instagram.model.Post;

import java.util.List;

public class AdapterStory extends RecyclerView.Adapter<AdapterStory.MyViewHolder> {

    Context context;
    List<Post> listStory;

    public AdapterStory(Context context, List<Post> listStory) {
        this.context = context;
        this.listStory = listStory;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_story, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post story = listStory.get(position);
        holder.nameUser.setText(story.getNameUser());

        if(story.getUrlPhotoUser() != null){

            Glide.with(context).load(story.getUrlPhotoUser()).into(holder.iconPhoto);

        }
    }

    @Override
    public int getItemCount() {
        return listStory.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iconPhoto;
        TextView nameUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iconPhoto = itemView.findViewById(R.id.imageIconeAdapterStory);
            nameUser = itemView.findViewById(R.id.textNomeStoryAdapter);

        }

    }

}
