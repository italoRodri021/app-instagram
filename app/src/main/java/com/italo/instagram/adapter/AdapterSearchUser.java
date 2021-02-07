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
import com.italo.instagram.model.User;

import java.util.List;

public class AdapterSearchUser extends RecyclerView.Adapter<AdapterSearchUser.MyViewHolder> {

    private final Context context;
    private final List<User> listUser;

    public AdapterSearchUser(Context context, List<User> listUser) {

        this.context = context;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_search_user, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        User u = listUser.get(position);
        holder.textNameUser.setText(u.getNameUser());
        holder.textName.setText(u.getName());

        if (u.getUrlPhoto() != null) {

            Uri url = Uri.parse(u.getUrlPhoto());
            Glide.with(context).load(url).into(holder.imagePhoto);
        }

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePhoto;
        TextView textName, textNameUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePhoto = itemView.findViewById(R.id.imageFotoAdapterPesquisa);
            textNameUser = itemView.findViewById(R.id.textNomeUsuarioAdapterPesquisa);
            textName = itemView.findViewById(R.id.textNomeAdapterPesquisa);

        }

    }

}
