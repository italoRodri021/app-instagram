package com.italo.instagram.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.italo.instagram.R;
import com.italo.instagram.config.ConfigFirebase;
import com.italo.instagram.fragment.LoginFragment;
import com.italo.instagram.fragment.RegisterFragment;

public class AuthActivity extends AppCompatActivity {

    private LinearLayout contentAuth;
    private FrameLayout containerFrame;
    private LoginFragment loginFrag;
    private RegisterFragment registerFrag;
    private Button btnLogin, btnRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        iniComponents();
        configInterface();

    }

    public void configInterface() {

        if (auth.getCurrentUser() != null) {

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);

        }

        btnLogin.setOnClickListener((v) -> {

            contentAuth.setVisibility(View.GONE);

            containerFrame.setVisibility(View.VISIBLE);
            FragmentTransaction login = getSupportFragmentManager()
                    .beginTransaction().addToBackStack(null)
                    .replace(R.id.frameConteudoAuth, loginFrag);
            login.commit();

        });

        btnRegister.setOnClickListener((v) -> {

            contentAuth.setVisibility(View.GONE);

            containerFrame.setVisibility(View.VISIBLE);
            FragmentTransaction register = getSupportFragmentManager()
                    .beginTransaction().addToBackStack(null)
                    .replace(R.id.frameConteudoAuth, registerFrag);
            register.commit();

        });

    }

    @Override
    public void onBackPressed() {

        if (containerFrame.getVisibility() == View.VISIBLE) {

            contentAuth.setVisibility(View.VISIBLE);
            containerFrame.setVisibility(View.GONE);
        }

    }

    public void iniComponents() {

        btnLogin = findViewById(R.id.btnEntrarInicio);
        btnRegister = findViewById(R.id.btnCadastroInicio);
        containerFrame = findViewById(R.id.frameConteudoAuth);
        contentAuth = findViewById(R.id.contentAuth);

        loginFrag = new LoginFragment();
        registerFrag = new RegisterFragment();

        auth = ConfigFirebase.getAuth();
    }
}