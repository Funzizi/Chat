package com.example.appchat.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.appchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("NonConstantResourceId")
public class StartActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick({R.id.button_login, R.id.button_sign_up})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                startActivity(new Intent(StartActivity.this, LoginActivity.class));
                break;

            case R.id.button_sign_up:
                startActivity(new Intent(StartActivity.this, SignUpActivity.class));
                break;
        }
    }
}