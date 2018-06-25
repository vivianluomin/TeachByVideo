package com.example.asus1.teacherbyvideo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus1.teacherbyvideo.Models.ComModel;
import com.example.asus1.teacherbyvideo.Models.LoadModel;
import com.example.asus1.teacherbyvideo.R;
import com.example.asus1.teacherbyvideo.Services.LoadService;
import com.example.asus1.teacherbyvideo.Util.ActivityManager;
import com.example.asus1.teacherbyvideo.Util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class LoadActivity extends BaseActivity implements View.OnClickListener {

    private EditText mUser;
    private EditText mPassword;
    private TextView mLoad;
    private String[] permissions;
    private List<String> mPer;

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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_load:
//                String name = mUser.getText().toString();
//                String password = mLoad.getText().toString();
//                checkPhoneF(name,password);
                setPermission();
                break;
        }
    }

    private void setPermission(){
        permissions = new String[3];
        mPer = new ArrayList<>();
        permissions[0] = Manifest.permission.RECORD_AUDIO;
        permissions[1] = Manifest.permission.CAMERA;
        permissions[2] = Manifest.permission.WRITE_EXTERNAL_STORAGE;


        for(int i = 0;i<permissions.length;i++){
            if(ActivityCompat.checkSelfPermission
                    (this,permissions[i])
                    != PackageManager.PERMISSION_GRANTED){
                mPer.add(permissions[i]);
            }
        }
        if(mPer.size()>0){
            this.requestPermissions((String[]) mPer.toArray(new String[mPer.size()]),100);
        }else {
            startActivity(new Intent(LoadActivity.this,MainActivity.class));
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults.length>0){
                for(int i = 0;i<grantResults.length;i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"你拒绝了该请求",Toast.LENGTH_SHORT).show();
                    }
                }
                startActivity(new Intent(LoadActivity.this,MainActivity.class));
            }
        }

    }

    private void register(String name,String password){
        LoadService service = HttpUtil.getService(LoadService.class);
        Call<ComModel<String>> call = service.registerPhone(name,password);
        HttpUtil.doRequest(call,registerCallback);
    }

    private void load(String name,String password){
        LoadService service = HttpUtil.getService(LoadService.class);
        Call<ComModel<LoadModel>> call = service.
                getLoadInfo(mUser.getText().toString(),mPassword.getText().toString());
        HttpUtil.doRequest(call,callBack);

    }

    private void checkPhoneF(String name,String password){
        LoadService service = HttpUtil.getService(LoadService.class);
        Call<ComModel<String>> call = service.checkPhone(name);
        HttpUtil.doRequest(call,checkPhone);
    }

    private HttpUtil.ResquestCallBack<LoadModel> callBack = new HttpUtil.ResquestCallBack<LoadModel>() {
        @Override
        public void onRespone(ComModel<LoadModel> response) {
            if(response!=null){
                if(response.getmStatus() == 200){
                    setPermission();
                    //startActivity(new Intent(LoadActivity.this,MainActivity.class));
                }else if(response.getmStatus() == 501){
                    Toast.makeText(LoadActivity.this
                            ,getString(R.string.load_pass_fail),Toast.LENGTH_SHORT).show();
                }else if(response.getmStatus() == 502){
                    Toast.makeText(LoadActivity.this
                            ,getString(R.string.loaded),Toast.LENGTH_SHORT).show();
                }
            }

        }

        @Override
        public void onError() {

        }
    };


    private HttpUtil.ResquestCallBack<String> checkPhone = new HttpUtil.ResquestCallBack<String>() {
        @Override
        public void onRespone(ComModel<String> response) {
            if(response.getmStatus() == 200){
                register(mUser.getText().toString(),mPassword.getText().toString());
            }else {
              load(mUser.getText().toString(),mPassword.getText().toString());
            }
        }

        @Override
        public void onError() {

        }
    };

    HttpUtil.ResquestCallBack<String> registerCallback = new HttpUtil.ResquestCallBack<String>() {
        @Override
        public void onRespone(ComModel<String> response) {
            if (response.getmStatus() == 200){
                register(mUser.getText().toString(),mPassword.getText().toString());
            }else {
                Toast.makeText(LoadActivity.this
                        ,getString(R.string.register_fail),Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError() {

        }
    };
}
