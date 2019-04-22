package com.luck.slideleftfinish;

/*************************************************************************************
 * Module Name:
 * Description:
 * Author: 李桐桐
 * Date:   2019/3/18
 *************************************************************************************/

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TestFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private static final String KEY = "key";
    private String title = "测试";
    private SimpleRecyclerViewAdapter mAdapter;
    private List<String> mDatas = new ArrayList<>();

    static TestFragment newInstance(String title) {
        TestFragment fragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            title = arguments.getString(KEY);
        }
        mRecyclerView = view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getContext(),
                LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);

        for (int i = 0; i < 50; i++) {

            String s = String.format("我是第%d个" + title, i);
            if (title.equals("页码3")) {
                s = String.format("点我进第三页面%d" + title, i);
            }
            mDatas.add(s);
        }

        mAdapter = new SimpleRecyclerViewAdapter(getContext(), mDatas);
        mAdapter.setOnItemClickListener(new SimpleRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String content) {
                if (title.equals("页码3"))
                    ((TwoActivity) Objects.requireNonNull(getActivity())).startNextActivity();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

    }

    public static class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<BookViewHolder> {
        private OnItemClickListener onItemClickListener;
        Context context;
        List<String> list;

        public SimpleRecyclerViewAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @NonNull
        @Override
        public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_string, parent, false);
            return new BookViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BookViewHolder holder, final int position) {
            holder.textview.setText(list.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position, list.get(position));
                    }
                }
            });
        }

        public interface OnItemClickListener {
            void onItemClick(int position, String content);
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.onItemClickListener = listener;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    /**
     * RecyclerView 的 ViewHolder 实现类
     */
    static class BookViewHolder extends RecyclerView.ViewHolder {

        TextView textview;

        BookViewHolder(View itemView) {
            super(itemView);
            textview = itemView.findViewById(R.id.tv);
        }
    }

}

