package com.italo.instagram.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.italo.instagram.R;
import com.italo.instagram.adapter.AdapterComments;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.config.UserFirebase;
import com.italo.instagram.model.Comment;
import com.italo.instagram.model.Post;
import com.italo.instagram.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private Post post;
    private RecyclerView recyclerComment;
    private ImageView imagePhotoCurrentUser, imagePhotoUserPost;
    private TextInputEditText editComment;
    private TextView btnSendComment, textNameUserPost, textDescriptionPost, textAlertComent;
    private String idCurrentUser, nameUser, urlPhoto;
    private DatabaseReference database;
    private final List<Comment> listCommet = new ArrayList<>();
    private AdapterComments adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        iniCompenents();
        getDataCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();
        getComments();
        configInterface();

    }

    public void getDataCurrentUser() {

        DatabaseReference data = database.child("User").child("Profile").child(idCurrentUser);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    User u = snapshot.getValue(User.class);
                    nameUser = u.getNameUser();
                    urlPhoto = u.getUrlPhoto();
                    editComment.setHint("Comentar como " + u.getNameUser());

                    if (u.getUrlPhoto() != null){
                        Glide.with(getApplicationContext()).load(u.getUrlPhoto()).into(imagePhotoCurrentUser);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR",error.getMessage());
            }
        });

    }

    public void configInterface() {

        if (post.getUrlPhotoUser() != null){
            Glide.with(this).load(post.getUrlPhotoUser()).into(imagePhotoUserPost);
        }

        btnSendComment.setOnClickListener((v) -> {

            Snackbar.make(v, "Deseja publicar este comentário?", BaseTransientBottomBar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(R.color.white))
                    .setTextColor(getResources().getColor(R.color.black))
                    .setAction("CONTINUAR", action -> {

                        validateComment();

                    }).show();

        });

    }

    public void getComments(){

        DatabaseReference comments = database.child("Comments").child(post.getIdPost());
        comments.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listCommet.clear();

               for (DataSnapshot ds : snapshot.getChildren()){
                   Comment comment = ds.getValue(Comment.class);

                    listCommet.add(comment);
                    textAlertComent.setVisibility(View.GONE);

               }
               adapter.notifyDataSetChanged();
                Collections.reverse(listCommet);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("ERROR",error.getMessage());
            }
        });

    }

    public void validateComment() {

        String comment = editComment.getText().toString();

        if (comment != null && comment.length() > 0 && !comment.isEmpty()) {

            String id = database.push().getKey();

            Comment c = new Comment();
            c.setIdPost(post.getIdPost());
            c.setIdComment(id);
            c.setComment(comment);
            c.setIdUser(idCurrentUser);
            c.setNameUser(nameUser);
            c.setUrlPhoto(urlPhoto);
            c.saveNewComment();

            showToast("Comentário Publicado!");

        } else {
            showToast("Digite seu comentário!");
        }

    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void iniCompenents() {

        recyclerComment = findViewById(R.id.recyclerComentarios);
        textAlertComent = findViewById(R.id.textPrimeiroComentario);
        textNameUserPost = findViewById(R.id.textNomeUsuarioComentarioPost);
        textDescriptionPost = findViewById(R.id.textLegendaPostComentarios);
        imagePhotoCurrentUser = findViewById(R.id.imageUsuarioEditTextComentario);
        imagePhotoUserPost = findViewById(R.id.imageAmigoComentario);
        editComment = findViewById(R.id.editTextComentario);
        btnSendComment = findViewById(R.id.textBtnEnviarComentario);
        Toolbar toolbar = findViewById(R.id.toolbarComentarios);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comentários");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        post = (Post) i.getSerializableExtra("DATA_POST");
        textNameUserPost.setText(post.getNameUser());
        textDescriptionPost.setText(post.getDescription());


        idCurrentUser = UserFirebase.getIdUser();
        database = ConfigFirebase.getDatabase();

        adapter = new AdapterComments(this,listCommet);
        recyclerComment.setHasFixedSize(true);
        recyclerComment.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        recyclerComment.setAdapter(adapter);

    }

}