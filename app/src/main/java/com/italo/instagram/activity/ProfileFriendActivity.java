package com.italo.instagram.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.italo.instagram.R;
import com.italo.instagram.adapter.AdapterGaleryPosts;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.config.UserFirebase;
import com.italo.instagram.fragment.PreviewPostFragment;
import com.italo.instagram.helper.RecyclerClick;
import com.italo.instagram.model.Post;
import com.italo.instagram.model.User;

import java.util.ArrayList;
import java.util.List;

public class ProfileFriendActivity extends AppCompatActivity {

    private static int countFollowers;
    private static int countFollowing;
    private final List<Post> listPosts = new ArrayList<>();
    private ImageView imagePhoto;
    private TextView textName, textBio, textPosts, textFollowers, textFollowing, textNotPosts;
    private RecyclerView recyclerPhotos;
    private ToggleButton toggleBtnFollow;
    private Button btnMessage;
    private String idCurrentUser, idFriend;
    private AdapterGaleryPosts adapter;
    private User userDataFriend, userDataCurrent;
    private DatabaseReference database;
    private PreviewPostFragment previewPostFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_friend);

        iniComponents();
        getDataCurrentUser();
        getPostsFriend();
        getPreview();

    }

    public void getDataFriend(String idFriend) {

        DatabaseReference data = database.child("User").child("Profile").child(idFriend);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    User u = snapshot.getValue(User.class);

                    textName.setText(u.getName());
                    textBio.setText(u.getBioDescription());
                    textPosts.setText(String.valueOf(u.getCountPosts()));
                    textFollowers.setText(String.valueOf(u.getCountFollowers()));
                    textFollowing.setText(String.valueOf(u.getCountFollowing()));
                    countFollowers = u.getCountFollowers();

                    if (userDataFriend.getUrlPhoto() != null) {

                        Glide.with(getApplicationContext()).load(userDataFriend.getUrlPhoto()).into(imagePhoto);
                    }

                    configToggleFollow(); // -> configurando toggle
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("ERRO", error.getMessage());
            }
        });

    }

    public void getDataCurrentUser() {

        DatabaseReference data = database.child("User").child("Profile").child(idCurrentUser);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    userDataCurrent = snapshot.getValue(User.class);
                    countFollowing = userDataCurrent.getCountFollowing();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void configToggleFollow() {

        DatabaseReference data = database.child("Followers").child(idFriend);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(idCurrentUser).exists()) {

                    toggleBtnFollow.setChecked(true); // -> Seguindo
                    toggleBtnFollow.setOnClickListener((v) -> {

                        DatabaseReference remove = database.child("Followers").child(idFriend); // -> Remover seguidor
                        remove.child(idCurrentUser).removeValue();

                        User friend = new User(); // -> Atualizando seguidores do amigo
                        friend.setIdUser(idFriend);
                        friend.setCountFollowers(countFollowers - 1);
                        friend.updateCountFollowers();

                        User user = new User();  // -> Atualizando seguindo do usuario logado
                        user.setIdUser(idCurrentUser);
                        user.setCountFollowing(countFollowing - 1);
                        user.updateCountFollowing();

                    });

                } else {

                    toggleBtnFollow.setChecked(false); // -> Seguir
                    toggleBtnFollow.setOnClickListener((v) -> {

                        User u = new User(); // -> Add seguidor
                        u.setIdFriend(idFriend);
                        u.setIdUser(idCurrentUser);
                        u.setName(userDataCurrent.getName());
                        u.setNameUser(userDataCurrent.getNameUser());
                        u.setUrlPhoto(userDataCurrent.getUrlPhoto());
                        u.saveDataFollow();

                        User friend = new User(); // -> Atualizando seguidores do amigo
                        friend.setIdUser(idFriend);
                        friend.setCountFollowers(countFollowers + 1);
                        friend.updateCountFollowers();

                        User user = new User();  // -> Atualizando seguindo do usuario logado
                        user.setIdUser(idCurrentUser);
                        user.setCountFollowing(countFollowing + 1);
                        user.updateCountFollowing();

                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void getPostsFriend() {

        DatabaseReference posts = database.child("PostsUser").child(idFriend);
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

    public void iniComponents() {

        imagePhoto = findViewById(R.id.imageFotoPerfilAmigo);
        textName = findViewById(R.id.textNomePerfilAmigo);
        textBio = findViewById(R.id.textBiografiaPerfilAmigo);
        recyclerPhotos = findViewById(R.id.recyclerPublicacaoPerfilAmigo);
        textPosts = findViewById(R.id.textPublicacoesAmigo);
        textFollowers = findViewById(R.id.textSeguidoresAmigo);
        textFollowing = findViewById(R.id.textSeguindoAmigo);
        textNotPosts = findViewById(R.id.textSemPublicaçõesAmigo);
        toggleBtnFollow = findViewById(R.id.btnSeguirPerfilAmigo);
        btnMessage = findViewById(R.id.btnMensagemPerfilAmigo);
        Toolbar toolbar = findViewById(R.id.toolbarPerfilAmigo);

        idCurrentUser = UserFirebase.getIdUser();
        database = ConfigFirebase.getDatabase();

        Intent i = getIntent();
        userDataFriend = (User) i.getSerializableExtra("DATA_USER");
        idFriend = userDataFriend.getIdUser();
        getDataFriend(userDataFriend.getIdUser());

        adapter = new AdapterGaleryPosts(this, listPosts);
        recyclerPhotos.setHasFixedSize(true);
        recyclerPhotos.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerPhotos.setAdapter(adapter);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(userDataFriend.getNameUser());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void getPreview() {

        recyclerPhotos.addOnItemTouchListener(
                new RecyclerClick(this, recyclerPhotos,
                        new RecyclerClick.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Post post = listPosts.get(position);

                                Intent i = new Intent(getApplicationContext(), PreviewPostActivity.class);
                                i.putExtra("DATA_POST", post);
                                startActivity(i);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                Post post = listPosts.get(position);

                                previewPostFrag = new PreviewPostFragment();

                                FragmentTransaction transaction = getSupportFragmentManager()
                                        .beginTransaction()
                                        .addToBackStack(null)
                                        .replace(R.id.frameConteudoPerfilAmigo,
                                                previewPostFrag);

                                Bundle bundle = new Bundle();
                                bundle.putSerializable("DATA_POST", post);
                                previewPostFrag.setArguments(bundle);

                                transaction.commit();
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}