package com.yzc35326.runningassistant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.yzc35326.runningassistant.R;
import com.yzc35326.runningassistant.model.User;
import com.yzc35326.runningassistant.util.LogUtil;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String host = "192.168.0.105";
    private static final String port = "9999";
    private static final String TAG = "LoginActivity";
    private Button login;
    private Button register;
    private ImageButton login_qq;

    private EditText etPhone,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login = (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.register_button);
        login_qq = (ImageButton) findViewById(R.id.qq_login_button);
        this.etPhone=(EditText)findViewById(R.id.et_phone);
        this.etPassword=(EditText)findViewById(R.id.et_password);
        this.login.setOnClickListener((v)->{
            Request.Builder requestBuiler = new Request.Builder().url("http://" + host + ":" + port + "/login");
            RequestBody requestBody = new FormBody.Builder()
                    .add("phone", this.etPhone.getText().toString())
                    .add("password", this.etPassword.getText().toString())
                    .build();
            Request request = requestBuiler.post(requestBody).build();
            OkHttpClient okHttpClient = new OkHttpClient();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    LogUtil.d(TAG,"onFailure"+e.getMessage());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Gson gson = new Gson();
                    User user = gson.fromJson(response.body().string(), User.class);
                    Intent intent = new Intent();
//                    intent.putExtra("user",user);
//                    setResult(RESULT_OK,intent);
//                    finish();
                    runOnUiThread(() -> {
                        Toast.makeText(LoginActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
                    });
                }
            });
        });
//        login.setOnClickListener((v) -> {




//            Request.Builder requestBuiler = new Request.Builder().url("http://" + host + ":" + port + "/login");
//            RequestBody requestBody = new FormBody.Builder()
//                    .add("phone", tl_userphone.getEditText().getText().toString())
//                    .add("password", tl_password.getEditText().getText().toString())
//                    .build();
//            Request request = requestBuiler.post(requestBody).build();
//            OkHttpClient okHttpClient = new OkHttpClient();
//            Call call = okHttpClient.newCall(request);
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                    Log.d(TAG, "onFailure: ");
//                    Log.d(TAG, "" + e.getMessage());
//                }
//
//                @Override
//                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                    Gson gson = new Gson();
//                    User user = gson.fromJson(response.body().string(), User.class);
//                    Intent intent = new Intent();
//                    Log.d(TAG, "onResponse: ");
////                    intent.putExtra("user",user);
////                    setResult(RESULT_OK,intent);
////                    finish();
//                    runOnUiThread(() -> {
//                        Toast.makeText(LoginActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
//                    });
//                }
//            });
//        });

    }
}
