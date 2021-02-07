package com.italo.instagram.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.italo.instagram.R;
import com.italo.instagram.fragment.HomeFragment;
import com.italo.instagram.fragment.ProfileFragment;
import com.italo.instagram.fragment.SearchFragment;
import com.italo.instagram.fragment.TypePostFragment;
import com.italo.instagram.helper.Permission;
import com.ss.bottomnavigation.BottomNavigation;

public class MainActivity extends AppCompatActivity {

    private BottomNavigation bottomNav;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniConponents();
        configInterface();

    }

    public void configInterface() {

        bottomNav.setDefaultItem(0);
        bottomNav.setOnSelectedItemChangeListener(itemId -> {

            switch (itemId) {
                case R.id.tabHome:
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameConteudoMain, new HomeFragment());

                    break;
                case R.id.tabPesquisa:
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameConteudoMain, new SearchFragment());
                    break;
                case R.id.tabNovaPostagem:
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameConteudoMain, new TypePostFragment());
                    break;
                case R.id.tabPerfil:
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameConteudoMain, new ProfileFragment());
                    break;
            }
            transaction.commit();

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int PERMISSIONS : grantResults) {
            if (PERMISSIONS == PackageManager.PERMISSION_DENIED) {

                Permission.validatePermissions(Permission.PERMISSIONS_USER, this, 1);
            }
        }
    }

    public void iniConponents() {

        bottomNav = findViewById(R.id.bottomNavigationMenu);

        Permission.validatePermissions(Permission.PERMISSIONS_USER, this, 1);

    }

    @Override
    public void onBackPressed() {

    }
}