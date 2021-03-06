package com.example.appchat.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.appchat.R;
import com.example.appchat.adapter.MessageAdapter;
import com.example.appchat.base.Utillity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("NonConstantResourceId")
public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.button_send)
    ImageButton buttonSend;
    @BindView(R.id.import_message)
    EditText importMessage;
    @BindView(R.id.rcv_message)
    RecyclerView rcvMessage;

    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String Uid;

    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);

        initViews();

        Uid = getIntent().getStringExtra("Uid");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(Uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Account account = snapshot.getValue(Account.class);
                if (account != null) {
                    userName.setText(account.getUsername());
                    Utillity.loadAvatar(profileImage, account.getImageURL());
                    responseMessage(firebaseUser.getUid(), Uid, account.getImageURL());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        rcvMessage.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        rcvMessage.setLayoutManager(linearLayoutManager);
    }

    private void responseMessage(String myId, String Uid, String imageUrl) {
        messageList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message.getReceiver().equals(myId) && message.getSender().equals(Uid) || message.getSender().equals(myId)) {
                        messageList.add(message);
                    }
                }
                messageAdapter = new MessageAdapter(MessageActivity.this, messageList, imageUrl);
                rcvMessage.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @OnClick({R.id.button_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_send :
                String message = importMessage.getText() != null ? importMessage.getText().toString() : "";
                if (!message.equals("")) {
                    sendMessage(firebaseUser.getUid(), Uid, message);
                } else {
                    Toast.makeText(MessageActivity.this, "Kh??ng th??? g???i tin nh???n r???ng", Toast.LENGTH_SHORT).show();
                }
                importMessage.setText("");
                break;
        }
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        databaseReference.child("Chats").push().setValue(hashMap);
        Toast.makeText(MessageActivity.this, "G???i tin nh???n th??nh c??ng", Toast.LENGTH_SHORT).show();
    }
}