package com.example.appchat.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appchat.R;
import com.example.appchat.model.Message;
import com.example.appchat.model.User;
import com.example.appchat.view.activity.MessageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int TYPE_MESSAGE_LEFT = 0;
    public static final int TYPE_MESSAGE_RIGHT = 1;

    private Context mContext;
    private List<Message> messageList;
    private String imageUrl;

    private FirebaseUser firebaseUser;

    public MessageAdapter (Context context, List<Message> messageList, String imageUrl) {
        this.mContext = context;
        this.messageList = messageList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_MESSAGE_LEFT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_message_left, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_message_right, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.iMessage.setText(message.getMessage());
        if (imageUrl.equals("default")) {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageUrl).into(holder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @SuppressLint("NonConstantResourceId")
    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.profile_image)
        CircleImageView profileImage;
        @BindView(R.id.i_messege)
        TextView iMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (messageList.get(position).getSender().equals(firebaseUser.getUid())) {
            return TYPE_MESSAGE_RIGHT;
        } else {
            return TYPE_MESSAGE_LEFT;
        }
    }
}
