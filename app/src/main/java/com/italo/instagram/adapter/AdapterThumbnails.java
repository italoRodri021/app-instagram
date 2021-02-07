package com.italo.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.italo.instagram.R;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

public class AdapterThumbnails extends RecyclerView.Adapter<AdapterThumbnails.MyViewHolder> {

    private final Context context;
    private final List<ThumbnailItem> thumList;

    public AdapterThumbnails(Context context, List<ThumbnailItem> thumList) {

        this.context = context;
        this.thumList = thumList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_thubnails, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ThumbnailItem item = thumList.get(position);
        holder.textTitle.setText(item.filterName);
        holder.imagePhoto.setImageBitmap(item.image);

    }

    @Override
    public int getItemCount() {
        return thumList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePhoto;
        TextView textTitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textTituloAdapterThumbnail);
            imagePhoto = itemView.findViewById(R.id.imageAdapterThumbnail);

        }
    }

}
