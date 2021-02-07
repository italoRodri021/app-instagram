package com.italo.instagram.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.italo.instagram.R;
import com.italo.instagram.activity.NewPostActivity;
import com.italo.instagram.config.ConfigFirebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TypePostFragment extends Fragment {

    private final List<String> listFollows = new ArrayList<>();
    private Button btnPost, btnStory;
    private DatabaseReference database;

    public TypePostFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_type_post, container, false);

        iniComponents(v);
        getFollowsUser();

        return v;
    }

    public void getFollowsUser() {

        DatabaseReference data = database.child("Followers");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot idCurrentUser : snapshot.getChildren()) {

                    for (DataSnapshot idFriends : idCurrentUser.getChildren()) {

                        listFollows.add(idFriends.getKey()); // -> add ids dos seguidores

                    }
                }
                configInterface();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void configInterface() {

        btnPost.setOnClickListener((v) -> {

            Intent i = new Intent(getContext(), NewPostActivity.class);
            i.putExtra("LIST_DATA_FRIENDS", (Serializable) listFollows);
            i.putExtra("TYPE", "FEED");
            i.putExtra("TITLE", "Nova Publicação");
            startActivity(i);

        });

        btnStory.setOnClickListener((v) -> {

            Intent i = new Intent(getContext(), NewPostActivity.class);
            i.putExtra("LIST_DATA_FRIENDS", (Serializable) listFollows);
            i.putExtra("TYPE", "STORY");
            i.putExtra("TITLE", "Novo Story");
            startActivity(i);

        });

    }

    public void iniComponents(View v) {

        btnPost = v.findViewById(R.id.btnPublicação);
        btnStory = v.findViewById(R.id.btnStory);

        database = ConfigFirebase.getDatabase();
    }

}