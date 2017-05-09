package com.zjs.ldrtech.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zjs.ldrtech.R;
import com.zjs.ldrtech.bean.GankDayResults;
import com.zjs.ldrtech.bean.Result;
import com.zjs.ldrtech.view.MyWebActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjs on 2017/5/3 22:31
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
public class DayFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "DayFragmentAdapter";
    private static final int TYPE_DESC = 1;
    private static final int TYPE_TITLE = 2;
    private List<ViewItem> mList;
    private Context mContext;
    private class ViewItem {
        String title;
        String url;
        int type;
        public ViewItem(String title,String url,int type){
            this.title = title;
            this.url = url;
            this.type = type;
        }
    }
    public DayFragmentAdapter(Context context) {
        mContext = context;
        mList = new ArrayList<>();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_TITLE) {
            view = inflater.inflate(R.layout.fragment_day_item_title, parent, false);
            return new TitleViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.fragment_day_item, parent, false);
            return new DescViewHolder(view);
        }
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        private ViewItem result;

        public TitleViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_title);
        }
        public  void setResult(ViewItem item) {
            result = item;
            textView.setText(item.title);
        }
    }

    public class DescViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        private ViewItem result;
        private String url;

        public DescViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_desc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Result r = new Result();
                    r.setDesc(result.title);
                    r.setUrl(result.url);
                    MyWebActivity.startWebActivity(mContext,r);
                }
            });

        }
        public void setResult(ViewItem item) {
            this.result = item;
            textView.setText(item.title);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleViewHolder) {
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.setResult(mList.get(position));
        } else if (holder instanceof DescViewHolder) {
            DescViewHolder descViewHolder = (DescViewHolder) holder;
            descViewHolder.setResult(mList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).type;
    }

    public void setData(GankDayResults results) {
        mList.clear();
        if (results.Android != null) {
            mList.add(new ViewItem("Android",null,TYPE_TITLE));
            for (Result r: results.Android) {
                mList.add(new ViewItem(r.getDesc(),r.getUrl(),TYPE_DESC));
            }
        }
        if (results.iOS != null) {
            mList.add(new ViewItem("iOS", null, TYPE_TITLE));
            for (Result r : results.iOS) {
                mList.add(new ViewItem(r.getDesc(), r.getUrl(),TYPE_DESC));
            }
        }
        if (results.App != null) {
            mList.add(new ViewItem("App", null, TYPE_TITLE));
            for (Result r : results.App) {
                mList.add(new ViewItem(r.getDesc(), r.getUrl(), TYPE_DESC));
            }
        }
        if (results.瞎推荐 != null) {
            mList.add(new ViewItem("瞎推荐", null, TYPE_TITLE));
            for (Result r : results.瞎推荐) {
                mList.add(new ViewItem(r.getDesc(), r.getUrl(), TYPE_DESC));
            }
        }
        if (results.休息视频 != null) {
            mList.add(new ViewItem("休息视频", null, TYPE_TITLE));
            for (Result r : results.休息视频) {
                mList.add(new ViewItem(r.getDesc(), r.getUrl(), TYPE_DESC));
            }
        }
        notifyDataSetChanged();
    }
}
