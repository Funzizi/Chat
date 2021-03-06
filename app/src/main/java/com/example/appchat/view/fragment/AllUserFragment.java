package com.example.appchat.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appchat.R;
import com.example.appchat.adapter.UserAdapter;
import com.example.appchat.model.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class AllUserFragment extends Fragment {

    @BindView(R.id.rcv_all_user)
    RecyclerView rcvAllUser;

    private UserAdapter userAdapter;
    private List<Account> accountList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);

        rcvAllUser.setHasFixedSize(true);
        rcvAllUser.setLayoutManager(new LinearLayoutManager(getContext()));

        accountList = new ArrayList<>();

        getDataUser();

        return view;
    }

    private void getDataUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accountList.clear();
                Map<String, Object> td = (HashMap<String, Object>) snapshot.child("Users").getValue();
                Gson gson = new Gson();
                for(Map.Entry<String, Object> entry : td.entrySet()) {
                    Account account = gson.fromJson(gson.toJsonTree(entry.getValue()), Account.class);
                    if (!account.getId().equals(firebaseUser.getUid())) {
                        accountList.add(account);
                    }
                }
                userAdapter = new UserAdapter(getContext(), accountList, false);
                rcvAllUser.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}