package com.italo.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.italo.instagram.R;
import com.italo.instagram.model.Comment;

import java.util.List;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyViewHolder> {

    Context context;
    List<Comment> listComment;

    public AdapterComments(Context context, List<Comment> listComment) {
        this.context = context;
        this.listComment = listComment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_commets, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Comment c = listComment.get(position);
        holder.textNameUser.setText(c.getNameUser());
        holder.textComment.setText(c.getComment());

        if(c.getUrlPhoto() != null){
            Glide.with(context).load(c.getUrlPhoto()).into(holder.imagePhotoUser);
        }
    }

    @Override
    public int getItemCount() {
        return listComment.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textNameUser, textComment;
        ImageView imagePhotoUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePhotoUser = itemView.findViewById(R.id.imageFotoUsuarioComentAdapter);
            textNameUser = itemView.findViewById(R.id.textNomeUsuarioComentAdapter);
            textComment = itemView.findViewById(R.id.textComentarioAdapter);

        }


    }

}
