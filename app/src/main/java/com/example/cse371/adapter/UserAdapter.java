package com.example.cse371.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.cse371.R;
import com.example.cse371.User;

import java.util.List;


public class UserAdapter extends BaseQuickAdapter<User, BaseViewHolder> {
    public UserAdapter(@Nullable List<User> data) {
        super(R.layout.user_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, User item) {
        helper.setText(R.id.name, "name：" +  item.getName())
                .setText(R.id.email, "email：" + item.getEmail());
    }
}
