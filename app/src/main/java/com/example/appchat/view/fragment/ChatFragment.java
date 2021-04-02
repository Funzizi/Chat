package com.example.appchat.view.fragment;

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
import com.example.appchat.model.Message;
import com.example.appchat.model.Account;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatFragment extends Fragment {

    @BindView(R.id.rcv)
    RecyclerView rcv;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    private UserAdapter userAdapter;
    private List<Account> accountList;
    private List<String> receiverList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);

        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(getContext()));

        receiverList = new ArrayList<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                receiverList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message != null && message.getSender().equals(firebaseUser.getUid())) {
                        receiverList.add(message.getReceiver());
                    }
                    if (message != null && message.getReceiver().equals(firebaseUser.getUid())) {
                        receiverList.add(message.getSender());
                    }
                }

                displayMessage();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void displayMessage() {
        accountList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                accountList.clear();
//                Map<String, Object> td = (HashMap<String, Object>) snapshot.child("Users").getValue();
//                Gson gson = new Gson();
//                for(Map.Entry<String, Object> entry : td.entrySet()) {
//                    User user = gson.fromJson(gson.toJsonTree(entry.getValue()), User.class);
//                    for (String id : receiverList) {
//                        if (user.getId().equals(id)) {
//                            if (userList.size() != 0) {
//                                for (User u : userList) {
//                                    if (!user.getId().equals(u.getId())) {
//                                        userList.add(user);
//                                    }
//                                }
//                            } else {
//                                userList.add(user);
//                            }
//                        }
//                    }
//                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Account account = dataSnapshot.getValue(Account.class);

                    // add 1 User
                    for (String id : receiverList) {
                        if (account.getId().equals(id)) {
                            if (accountList.size() != 0) {
                                for (Account u : accountList) {
                                    if (!account.getId().equals(u.getId())) {
                                        accountList.add(account);
                                    }
                                }
                            } else {
                                accountList.add(account);
                            }
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), accountList);
                rcv.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}