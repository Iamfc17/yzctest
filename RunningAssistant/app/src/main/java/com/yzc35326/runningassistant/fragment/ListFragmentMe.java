package com.yzc35326.runningassistant.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yzc35326.runningassistant.R;
import com.yzc35326.runningassistant.activity.LocationActivity;
import com.yzc35326.runningassistant.activity.LoginActivity;
import com.yzc35326.runningassistant.activity.MainActivity;

public class ListFragmentMe extends Fragment {
    private ListView listViewMe;
    private Button btLogon,btLogoff;
    private static final String[] data=new String[]{"我的信息"};

    public static interface OnTitleClickListener{
        void onClick(String title);
    }
    private OnTitleClickListener onTitleClickListener;

    public void setOnTitleClickListener(OnTitleClickListener onTitleClickListener){
        this.onTitleClickListener=onTitleClickListener;
    }

    /**
     *静态内部类实现单例模式
     */
    private static class ListFragmentRunningHolder{
        private static final ListFragmentMe instance = new ListFragmentMe();
    }

    public ListFragmentMe(){

    }

    public static ListFragmentMe getInstance(){
        return ListFragmentRunningHolder.instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me,container,false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 完成对控件的初始化操作
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.listViewMe=view.findViewById(R.id.listview_me);

        this.btLogoff=view.findViewById(R.id.bt_logoff);
        this.btLogon=view.findViewById(R.id.bt_logon);

        this.btLogon.setOnClickListener((v)->{
            getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(((MainActivity)getActivity()),android.R.layout.simple_list_item_1,
                data);
        this.listViewMe.setAdapter(adapter);
        this.listViewMe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    getActivity().startActivity(new Intent(getActivity(), LocationActivity.class));
                }

            }
        });
    }



    public void changeTitle(String title){
        if(!isAdded()){
            return;
        }
        // this.textView.setText(title);
    }
}
