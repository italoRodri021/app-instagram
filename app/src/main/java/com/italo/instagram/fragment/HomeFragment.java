package com.italo.instagram.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.italo.instagram.R;
import com.italo.instagram.activity.StoryPreviewActivity;
import com.italo.instagram.adapter.AdapterFeed;
import com.italo.instagram.adapter.AdapterStory;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.config.UserFirebase;
import com.italo.instagram.helper.RecyclerClick;
import com.italo.instagram.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private final List<Post> listPost = new ArrayList<>();
    private final List<Post> listStory = new ArrayList<>();
    private RecyclerView recyclerStory, recyclerPosts;
    private AdapterStory adapterStory;
    private AdapterFeed adapterFeed;
    private String idCurrentUser;
    private DatabaseReference databaseStory;
    private DatabaseReference databasePost;
    private ValueEventListener valueEventListenerPosts;
    private ValueEventListener valueEventListenerStorys;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        iniComponents(v);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        getPostsFeed();
        getStory();
    }

    @Override
    public void onStop() {
        super.onStop();

        databasePost.removeEventListener(valueEventListenerPosts);
        databaseStory.removeEventListener(valueEventListenerStorys);
    }

    public void getStory() {

        DatabaseReference story = databaseStory.child("Storys").child(idCurrentUser);
        valueEventListenerStorys = story.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listStory.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post newStory = ds.getValue(Post.class);

                    listStory.add(newStory);

                }
                adapterStory.notifyDataSetChanged();
                clickStory();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void clickStory() {

        recyclerStory.addOnItemTouchListener(
                new RecyclerClick(getContext(), recyclerStory,
                        new RecyclerClick.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Post post = listStory.get(position);

                                Intent i = new Intent(getContext(), StoryPreviewActivity.class);
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

    public void getPostsFeed() {

        DatabaseReference post = databasePost.child("Feed").child(idCurrentUser);
        Query query = post.orderByChild("dateHour");
        valueEventListenerPosts = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                listPost.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post newPost = ds.getValue(Post.class);

                    listPost.add(newPost);

                }
                Collections.reverse(listPost);
                adapterFeed.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.getMessage());
            }
        });


    }

    public void iniComponents(View v) {

        recyclerStory = v.findViewById(R.id.recyclerStory);
        recyclerPosts = v.findViewById(R.id.recyclerPostsFeed);

        adapterStory = new AdapterStory(getContext(), listStory);
        recyclerStory.setHasFixedSize(true);
        recyclerStory.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.HORIZONTAL, false));
        recyclerStory.setAdapter(adapterStory);

        adapterFeed = new AdapterFeed(getContext(), listPost);
        recyclerPosts.setHasFixedSize(true);
        recyclerPosts.setLayoutManager(new LinearLayoutManager(getContext(),
                RecyclerView.VERTICAL, false));
        recyclerPosts.setAdapter(adapterFeed);

        idCurrentUser = UserFirebase.getIdUser();
        databaseStory = ConfigFirebase.getDatabase();
        databasePost = ConfigFirebase.getDatabase();

    }

}