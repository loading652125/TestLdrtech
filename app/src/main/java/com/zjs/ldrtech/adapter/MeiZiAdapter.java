package com.zjs.ldrtech.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zjs.ldrtech.R;
import com.zjs.ldrtech.bean.Result;
import com.zjs.ldrtech.view.PhotoActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zjs on 2017/4/25 14:13
 * email:loading652125@163.com
 * 　　　　　　　　┏┓　　　┏┓+ +
 * 　　　　　　　┏┛┻━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 ████━████ ┃+
 * 　　　　　　　┃　　　　　　　┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃ + +
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃ + + + +
 * 　　　　　　　　　┃　　　┃　　　　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ + 　　　　神兽保佑,代码无bug
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　　┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛ + + + +
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛+ + + +
 */
public class MeiZiAdapter extends RecyclerView.Adapter<MeiZiAdapter.ViewHolder> {
    private List<Result> mDatas;
    private Context mContext;
    private List<Integer> heightList;

    public MeiZiAdapter(Context context) {
        mContext = context;
        mDatas = new ArrayList<>();
        heightList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_meizi_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.imageView.getLayoutParams();
        layoutParams.height = heightList.get(position);
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        holder.imageView.setLayoutParams(layoutParams);
        final Result data = mDatas.get(position);
        if (data.getUrl() != null) {
            Glide.with(mContext)
                    .load(data.getUrl())
                    .error(R.drawable.haoqi)
                    .centerCrop()
                    .placeholder(R.drawable.bg_cyan)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);
        }
        else {
            holder.imageView.setImageResource(R.drawable.haoqi);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoActivity.startActivity((AppCompatActivity) mContext,data.getUrl(),holder.imageView);
            }
        });
        holder.textView.setText(data.getDesc());

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_img);
            textView = (TextView) itemView.findViewById(R.id.item_title);
        }
    }
    public void addData(List<Result> datas) {
        this.mDatas.addAll(datas);
        for (int i = 0; i < mDatas.size() ; i++) {
            //300到500高度的妹子图
            int height = new Random().nextInt(200) + 300;
            heightList.add(height);
        }
    }
    public void setData(List<Result> datas) {
        this.mDatas = datas;
        for (int i = 0; i < mDatas.size() ; i++) {
            //300到500高度的妹子图
            int height = new Random().nextInt(200) + 300;
            heightList.add(height);
        }
    }
}
