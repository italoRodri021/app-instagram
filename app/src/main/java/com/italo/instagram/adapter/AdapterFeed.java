package com.italo.instagram.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.italo.instagram.R;
import com.italo.instagram.activity.CommentsActivity;
import com.italo.instagram.activity.PreviewPostActivity;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.config.UserFirebase;
import com.italo.instagram.model.Post;
import com.like.IconType;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    Context context;
    List<Post> listPost;

    public AdapterFeed(Context context, List<Post> listPost) {
        this.context = context;
        this.listPost = listPost;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Post post = listPost.get(position);

        List<String> listUrl = post.getListUrlPhotos();
        final List<SlideModel> slideModels = new ArrayList<>();

        holder.likeButton.setIcon(IconType.Heart);
        holder.textNameUser.setText(post.getNameUser());
        holder.textDescription.setText(post.getDescription());
        holder.textDateHour.setText(post.getDateHour());

        if (post.getDescription().length() < 1){

            holder.textDescription.setVisibility(View.GONE);
        }

        if (post.getUrlPhotoUser() !=null){
            Glide.with(context).load(post.getUrlPhotoUser()).into(holder.imagePhotoUser);
        }

        holder.btnComments.setOnClickListener((v) -> {

            Intent i = new Intent(context, CommentsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("DATA_POST",post);
            context.startActivity(i);

        });

        getImageSlides(holder, post, listUrl, slideModels);
        getLikes(holder, post);
    }

    public void getImageSlides(MyViewHolder holder, Post post, List<String> listUrl, List<SlideModel> slideModels) {

        for (String URL : listUrl) { // -> Config slide

            SlideModel sm = new SlideModel(URL, "", ScaleTypes.FIT);
            slideModels.add(sm);
            holder.imageSlider.setImageList(slideModels);

        }

    }

    public void getLikes(MyViewHolder holder, Post post) {

        String idCurrentUser = UserFirebase.getIdUser();

        DatabaseReference data = ConfigFirebase.getDatabase().child("PostsLikes").child(post.getIdPost()); // -> Recuperando curtidas
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int countLike = 0;

                /*
                 * Verificando se o id do usuario já existe
                 * Se existir set o likeButton para true ou false baseado na resposta do firebase
                 */
                if (snapshot.child(idCurrentUser).exists()) {

                    holder.likeButton.setLiked(true);
                } else {
                    holder.likeButton.setLiked(false);
                }

                /*
                 * Verificando se existe o contador no firebase
                 * Se existir o objt Post recupera o valor atual e set no textView
                 */
                if (snapshot.hasChild("countLike")) {

                    Post post = snapshot.getValue(Post.class);
                    countLike = post.getCountLike();
                    holder.textNumberLikes.setText(post.getCountLike() + " Curtidas");

                }

                /*
                 * Criando novo objeto Post para atualizar o contador de likes
                 * database.child( "PostsLikes" ).child( getIdPost() ).updateChildren( map );
                 */
                Post like = new Post();
                like.setIdPost(post.getIdPost());
                like.setCountLike(countLike);

                /*
                 * Config click no likeButton
                 * Ao clicar enviar a resposta para o objt Post se é um "LIKED" ou "DESLIKE" para salvar a curtida
                 * "LIKED" add um like!!!
                 * "UNLIKED" remove o like!!!
                 */
                holder.likeButton.setOnLikeListener(new OnLikeListener() { // -> Config Clik LikeButton
                    @Override
                    public void liked(LikeButton likeButton) {

                        if (!snapshot.child(idCurrentUser).exists()) {

                            like.saveLikesPost();
                            like.updateLikePost("LIKED", idCurrentUser);
                        }

                    }

                    @Override
                    public void unLiked(LikeButton likeButton) {

                        if (snapshot.child(idCurrentUser).exists()) {
                            like.updateLikePost("UNLIKED", idCurrentUser);
                        }

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.getMessage());
            }
        });

    }

    @Override
    public int getItemCount() {
        return listPost.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imagePhotoUser;
        ImageSlider imageSlider;
        ImageButton btnComments, btnDirect;
        LikeButton likeButton;
        TextView textNameUser, textNumberLikes, textDescription, textDateHour;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imagePhotoUser = itemView.findViewById(R.id.imageFotoUsuarioFeedAdapter);
            imageSlider = itemView.findViewById(R.id.imageSlideFeedAdapter);
            textNameUser = itemView.findViewById(R.id.textNomeUsuarioFeedAdapter);
            textNumberLikes = itemView.findViewById(R.id.textNumeroCurtidasFeedAdapter);
            textDescription = itemView.findViewById(R.id.textLegendaFeedAdapter);
            textDateHour = itemView.findViewById(R.id.textDataFeedAdapter);
            likeButton = itemView.findViewById(R.id.icBtnCurtirFeedAdapter);
            btnComments = itemView.findViewById(R.id.icBtnComentarFeedAdapter);
            btnDirect = itemView.findViewById(R.id.icBtnDirectFeedAdapter);


        }
    }

}
