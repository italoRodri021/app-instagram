package com.italo.instagram.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.italo.instagram.R;
import com.italo.instagram.activity.EditProfileActivity;
import com.italo.instagram.activity.PreviewPostActivity;
import com.italo.instagram.adapter.AdapterGaleryPosts;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.config.UserFirebase;
import com.italo.instagram.helper.RecyclerClick;
import com.italo.instagram.model.Post;
import com.italo.instagram.model.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    private User user;
    private final List<Post> listPosts = new ArrayList<>();
    private ImageView imagePhoto;
    private Button btnEditProfile;
    private TextView textName, textNameUser, textBio, textPosts, textFollowers, textFollowing, textNotPosts;
    private RecyclerView recyclerPhotos;
    private AdapterGaleryPosts adapter;
    private String idUser;
    private DatabaseReference database;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        iniComponents(v);
        getDataUser();
        getPostsUser();
        configInterface();
        getPreview();


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    public void getDataUser() {

        DatabaseReference data = database.child("User").child("Profile").child(idUser);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    user = snapshot.getValue(User.class);

                    textName.setText(user.getName());
                    textNameUser.setText(user.getNameUser());
                    textBio.setText(user.getBioDescription());
                    textPosts.setText(String.valueOf(user.getCountPosts()));
                    textFollowers.setText(String.valueOf(user.getCountFollowers()));
                    textFollowing.setText(String.valueOf(user.getCountFollowing()));

                    if (user.getUrlPhoto() != null) {

                        Uri url = Uri.parse(user.getUrlPhoto());
                        Glide.with(getActivity()).load(url).into(imagePhoto);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void configInterface() {

        btnEditProfile.setOnClickListener((v) -> {

            Intent i = new Intent(getContext(), EditProfileActivity.class);
            startActivity(i);

        });

    }

    public void getPostsUser() {

        DatabaseReference posts = database.child("PostsUser").child(idUser);
        posts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    Post post = ds.getValue(Post.class);
                    listPosts.add(post);
                    textPosts.setText(String.valueOf(listPosts.size()));
                    textNotPosts.setVisibility(View.GONE);

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void getPreview() {

        recyclerPhotos.addOnItemTouchListener(
                new RecyclerClick(getContext(), recyclerPhotos,
                        new RecyclerClick.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Post post = listPosts.get(position);

                                Intent i = new Intent(getContext(), PreviewPostActivity.class);
                                i.putExtra("DATA_POST", post);
                                startActivity(i);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {


                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }));

    }

    public void iniComponents(View v) {

        imagePhoto = v.findViewById(R.id.imageFotoPerfil);
        textName = v.findViewById(R.id.textNomePerfil);
        textNameUser = v.findViewById(R.id.textNomeUsuarioPerfil);
        textBio = v.findViewById(R.id.textBiografiaPerfil);
        textPosts = v.findViewById(R.id.textPublicacoes);
        textFollowers = v.findViewById(R.id.textSeguidores);
        textFollowing = v.findViewById(R.id.textSeguindo);
        textNotPosts = v.findViewById(R.id.textSemPublicacoesPerfil);
        btnEditProfile = v.findViewById(R.id.btnEditarPerfil);
        recyclerPhotos = v.findViewById(R.id.recyclerPublicacaoPerfil);

        idUser = UserFirebase.getIdUser();
        database = ConfigFirebase.getDatabase();

        adapter = new AdapterGaleryPosts(getContext(), listPosts);
        recyclerPhotos.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerPhotos.setHasFixedSize(true);
        recyclerPhotos.setAdapter(adapter);

    }

}