package com.yzc35326.runningassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yzc35326.runningassistant.R;
import com.yzc35326.runningassistant.activity.LocationActivity;
import com.yzc35326.runningassistant.activity.MainActivity;
import com.yzc35326.runningassistant.activity.NavActivity;

public class ListFragmentNav extends Fragment {
    private ListView listView;
    private static final String[] data = new String[]{"定位功能", "跑步功能"};

    public static interface OnTitleClickListener {
        void onClick(String title);
    }

    private OnTitleClickListener onTitleClickListener;

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener) {
        this.onTitleClickListener = onTitleClickListener;
    }

    /**
     * 静态内部类实现单例模式
     */
    private static class ListFragmentRunningHolder {
        private static final ListFragmentNav instance = new ListFragmentNav();
    }

    public ListFragmentNav() {

    }

    public static ListFragmentNav getInstance() {
        return ListFragmentRunningHolder.instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 完成对控件的初始化操作
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.listView = view.findViewById(R.id.lv_fragment);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(((MainActivity) getActivity()), android.R.layout.simple_list_item_1,
                data);
        listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (data[i].equals("定位功能")) {
                    getActivity().startActivity(new Intent(getActivity(), LocationActivity.class));
                }
                if (data[i].equals("跑步功能")) {
                    getActivity().startActivity(new Intent(getActivity(), NavActivity.class));
                }
            }
        });
    }


    public void changeTitle(String title) {
        if (!isAdded()) {
            return;
        }
        // this.textView.setText(title);
    }
}
