package com.example.asus1.teacherbyvideo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.asus1.teacherbyvideo.Models.ComModel;
import com.example.asus1.teacherbyvideo.Models.LoadModel;
import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.Services.LoadService;
import com.example.asus1.teacherbyvideo.Util.HttpUtil;

import retrofit2.Call;

public class LoadActivity extends BaseActivity implements View.OnClickListener {

    private EditText mUser;
    private EditText mPassword;
    private TextView mLoad;
    private TextView mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);
        init();
    }

    private void init(){
        mUser = findViewById(R.id.ed_username);
        mPassword = findViewById(R.id.ed_password);
        mLoad = findViewById(R.id.tv_load);
        mLoad.setOnClickListener(this);
        mRegister = findViewById(R.id.tv_register);
        mRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_load:
                String name = mUser.getText().toString();
                String password = mLoad.getText().toString();
                LoadService service = HttpUtil.getService(LoadService.class);
                Call<ComModel<LoadModel>> call = service.getLoadInfo(name,password);
                //HttpUtil.doRequest(call,callBack);
                startActivity(new Intent(LoadActivity.this,MainActivity.class));
                break;
            case R.id.tv_register:
                break;
        }
    }

    private HttpUtil.ResquestCallBack<LoadModel> callBack = new HttpUtil.ResquestCallBack<LoadModel>() {
        @Override
        public void onRespone(LoadModel response) {
            if(response!=null){
                startActivity(new Intent(LoadActivity.this,MainActivity.class));
            }
        }

        @Override
        public void onError() {

        }
    };


}
