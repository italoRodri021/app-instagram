package com.italo.instagram.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.italo.instagram.R;
import com.italo.instagram.activity.PreviewPostActivity;
import com.italo.instagram.activity.ProfileFriendActivity;
import com.italo.instagram.adapter.AdapterGaleryPosts;
import com.italo.instagram.adapter.AdapterSearchUser;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.config.UserFirebase;
import com.italo.instagram.helper.RecyclerClick;
import com.italo.instagram.model.Post;
import com.italo.instagram.model.User;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private final List<User> listUser = new ArrayList<>();
    private final List<Post> listSuggestions = new ArrayList<>();
    private SearchView searchView;
    private RecyclerView recyclerSearch;
    private RecyclerView recyclerSuggestions;
    private AdapterGaleryPosts adapterPosts;
    private AdapterSearchUser adapterSearchUser;
    private DatabaseReference database;
    private String idCurrentUser;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        iniComponets(v);
        getSuggestions();
        clickResultSeach();
        clickSuggestions();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        configSearch();
    }

    public void getSuggestions() {

        DatabaseReference data = database.child("Feed");
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {

                    for(DataSnapshot phost : ds.getChildren()){
                        Post postPublic = phost.getValue(Post.class);
                            listSuggestions.add(postPublic);

                    }

                }
                adapterPosts.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("ERROR", error.getMessage());
            }
        });

    }

    public void configSearch() {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchResult(newText);
                return true;
            }
        });

    }

    public void searchResult(String text) {

        DatabaseReference result = database.child("User").child("Profile");
        Query query = result.orderByChild("nameUser").startAt(text).endAt("\uf8ff");

        if (text.length() > 4) {

            recyclerSearch.setVisibility(View.VISIBLE);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    listUser.clear();

                    for (DataSnapshot ds : snapshot.getChildren()) {
                        User user = ds.getValue(User.class);

                        if (!user.getIdUser().equals(idCurrentUser)) {
                            listUser.add(user);
                        }


                    }
                    adapterSearchUser.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Log.d("ERROR", error.getMessage());
                }
            });

        } else {
            listUser.clear();
            adapterSearchUser.notifyDataSetChanged();
            recyclerSearch.setVisibility(View.GONE);
        }

    }

    public void clickResultSeach() {

        recyclerSearch.addOnItemTouchListener(
                new RecyclerClick(getContext(), recyclerSearch,
                        new RecyclerClick.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                User user = listUser.get(position);

                                Intent i = new Intent(getContext(), ProfileFriendActivity.class);
                                i.putExtra("DATA_USER", user);
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

    public void clickSuggestions() {

        recyclerSuggestions.addOnItemTouchListener(
                new RecyclerClick(getContext(), recyclerSearch,
                        new RecyclerClick.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Post post = listSuggestions.get(position);

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

    public void iniComponets(View v) {

        searchView = v.findViewById(R.id.searchViewUsuario);
        recyclerSearch = v.findViewById(R.id.recyclerPesquisa);
        recyclerSuggestions = v.findViewById(R.id.recyclerSugestoes);

        idCurrentUser = UserFirebase.getIdUser();
        database = ConfigFirebase.getDatabase();

        adapterSearchUser = new AdapterSearchUser(getContext(), listUser);
        recyclerSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerSearch.setHasFixedSize(true);
        recyclerSearch.setAdapter(adapterSearchUser);

        adapterPosts = new AdapterGaleryPosts(getContext(), listSuggestions);
        recyclerSuggestions.setHasFixedSize(true);
        recyclerSuggestions.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerSuggestions.setAdapter(adapterPosts);
    }

}