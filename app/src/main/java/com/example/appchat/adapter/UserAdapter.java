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
import com.example.appchat.base.Utillity;
import com.example.appchat.model.Account;
import com.example.appchat.view.activity.MessageActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Account> accountList;
    private boolean isChat;


    public UserAdapter (Context context, List<Account> accountList, boolean isChat) {
        this.mContext = context;
        this.accountList = accountList;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Account account = accountList.get(position);
        holder.profileName.setText(account.getUsername());
        Utillity.loadAvatar(holder.profileImage, account.getImageURL());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("Uid", account.getId());
                mContext.startActivity(intent);
            }
        });

        if (isChat) {
            if (account.getStatus().equals("online")) {
                holder.statusOnline.setVisibility(View.VISIBLE);
                holder.statusOffline.setVisibility(View.GONE);
            } else {
                holder.statusOnline.setVisibility(View.GONE);
                holder.statusOffline.setVisibility(View.VISIBLE);
            }
        } else {
            holder.statusOnline.setVisibility(View.GONE);
            holder.statusOffline.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    @SuppressLint("NonConstantResourceId")
    public static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.profile_image)
        CircleImageView profileImage;
        @BindView(R.id.profile_name)
        TextView profileName;
        @BindView(R.id.online)
        CircleImageView statusOnline;
        @BindView(R.id.offline)
        CircleImageView statusOffline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
