package com.yzc35326.runningassistant.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yzc35326.runningassistant.R;

public class TabFragment extends Fragment {
    private static final String BUNDLE_KEY_TITLE="key_title";
    private TextView textView;
    private String title;

    public static interface OnTitleClickListener{
        void onClick(String title);
    }
    private OnTitleClickListener onTitleClickListener;

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener){
        this.onTitleClickListener=onTitleClickListener;
    }
    /**
     * 类似于静态工厂方法，完成Fragment的创建
     * @param title
     * @return
     */
    public static TabFragment newInstance(String title){
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_TITLE,title);
        TabFragment tabFragment = new TabFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments!=null){
            this.title = arguments.getString(BUNDLE_KEY_TITLE);
        }
    }

    /**
     * 完成对控件的初始化操作
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView=view.findViewById(R.id.tv_title);
        textView.setText(this.title);

        textView.setOnClickListener((v)->{
            //问题在于：我们Fragment会触发一些事件，Activity去响应这些事件
            if(this.onTitleClickListener!=null){
                this.onTitleClickListener.onClick("回调成功！");
            }
        });
    }

    public void changeTitle(String title){
        if(!isAdded()){
            return;
        }
        this.textView.setText(title);
    }
}
