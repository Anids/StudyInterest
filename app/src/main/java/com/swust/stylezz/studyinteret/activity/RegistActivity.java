package com.swust.stylezz.studyinteret.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mob.MobSDK;
import com.swust.stylezz.studyinteret.MD5Utils;
import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.http.HttpClient;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout relativeLayoutReturn;
    private EditText editTextNickName;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private EditText editTextPhone;
    private EditText yanzhengma;
    private String NickName;
    private String Username;
    private String Password;
    private String RepeatPassword;
    private String phonenumber;
    private String checkCode;
    private Button btn_Regist;
    private Button yanzhengma_btn;
    private JSONObject RegistObj=null;
    private ProgressDialog dialog;
    private HttpClient ServerSquire;
    private boolean Regist_Status=true;
    private static RegistActivity registActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_regist );
        /**
         * 1.实现返回至登录界面；
         * 2.获取文本框中的数据；
         * 3.对文本框中的数据进行格式判断；
         * 4.尝试请求插入新用户信息；
         * */
        registActivity=this;
        MobSDK.init ( this );
        InitView();
    }

    private void InitView() {
        RegistFindViewByID();
        SetOnClickListener();
    }

    private void SetOnClickListener() {
        getData();
        relativeLayoutReturn.setOnClickListener ( this );
        btn_Regist.setOnClickListener ( this );
        yanzhengma_btn.setOnClickListener ( this );
        //注册短信回调
        SMSSDK.registerEventHandler(ev);
    }

    /*
    * 短信验证的回调监听
    * */
    private EventHandler ev = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            if (result == SMSSDK.RESULT_COMPLETE) { //回调完成
                //提交验证码成功,如果验证成功会在data里返回数据。data数据类型为HashMap<number,code>
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Log.e("TAG", "提交验证码成功" + data.toString());
                    HashMap<String, Object> mData = (HashMap<String, Object>) data;
                    String country = (String) mData.get("country");//返回的国家编号
                    String phone = (String) mData.get("phone");//返回用户注册的手机号

                    Log.e("TAG", country + "====" + phone);

                    if (phone.equals(phonenumber)) {
                        runOnUiThread(new Runnable() {//更改ui的操作要放在主线程，实际可以发送hander
                            @Override
                            public void run() {
                                showDailog("恭喜你！通过验证");
                                dialog.dismiss();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showDailog("验证失败");
                                dialog.dismiss();
                            }
                        });
                    }

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {//获取验证码成功
                    Log.e("TAG", "获取验证码成功");
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {//返回支持发送验证码的国家列表

                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };

    private void showDailog(String text) {
        new AlertDialog.Builder(this)
                .setTitle(text)
                .setPositiveButton("确定", null)
                .show();
    }
    /**
     * 获取验证码
     */
    public void getCheckCode() {
        phonenumber=editTextPhone.getText ().toString ().trim ();
        //发送短信，传入国家号和电话号码
        if (TextUtils.isEmpty(phonenumber)) {
            toast("号码不能为空！");
        } else {
            SMSSDK.getVerificationCode("+86", phonenumber);
            toast("发送成功!");
        }
    }

    /**
     * 向服务器提交验证码，在监听回调中监听是否验证
     */
    private boolean sendCheckCode() {
        checkCode = yanzhengma.getText().toString();
        if (!TextUtils.isEmpty(checkCode)) {
            dialog = ProgressDialog.show(this, null, "正在验证...", false, true);
            //提交短信验证码
            SMSSDK.submitVerificationCode("+86", phonenumber, checkCode);//国家号，手机号码，验证码
            Toast.makeText(this, "提交了注册信息:" + phonenumber, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    /**
     * Toast
     * @param
     */
    public void toast(String info){
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler ( ev );
        super.onDestroy ();
    }

    private void RegistFindViewByID() {
        relativeLayoutReturn=findViewById ( R.id.regist_return );
        editTextNickName=findViewById ( R.id.et_accountnext_name );
        editTextPassword=findViewById ( R.id.et_passwordnext );
        editTextRepeatPassword=findViewById ( R.id.et_repassword );
        editTextPhone=findViewById ( R.id.et_phone );
        btn_Regist=findViewById ( R.id.btn_regist );
        yanzhengma_btn=findViewById ( R.id.yanzhengma_btn );
        yanzhengma=findViewById ( R.id.ed_yanzhengma );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()){
            case R.id.regist_return:
                //返回到登录界面；
                ReturnToLoginInterface();
                break;
            case R.id.btn_regist:
                //尝试注册
                TryToRegist();
                break;
            case R.id.yanzhengma_btn:
                getCheckCode ();
                break;
        }
    }

    private void TryToRegist() {
        getData ();
        if(NickName.isEmpty ()){
            Toast.makeText ( RegistActivity.this,"昵称不能为空，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        else if(Username.isEmpty ()){
            Toast.makeText ( RegistActivity.this,"账号不能为空，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        else if(Password.isEmpty ()){
            Toast.makeText ( RegistActivity.this,"密码不能为空，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        else if(RepeatPassword.isEmpty ()){
            Toast.makeText ( RegistActivity.this,"再次输入密码不能为空，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        else if(!Password.equals ( RepeatPassword )) {
            Toast.makeText ( RegistActivity.this,"两次密码不一致，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        else if (!sendCheckCode ()){
            //return;
        }
        String accRegex = "[0-9]{11}";//账号11位数字组成
        String pasRegex = "[0-9a-zA-Z]{6,18}";//密码6-10位数字或字母组成
        if (Username.matches ( accRegex )){

        }else{
            Toast.makeText ( RegistActivity.this,"手机号由11位数字组成，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        if(Password.matches ( pasRegex )){

        }else{
            Toast.makeText ( RegistActivity.this,"密码有6~18位数字组成，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        InsertIntoClient();
    }


    private void InsertIntoClient() {
        JSONObject Obj=new JSONObject (  );
        try {
            Obj.put ( "nickname",NickName );
            Obj.put ( "username", Username);
            Obj.put ( "password", MD5Utils.md5Password ( Password ));
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        String status=null;
        try {
            RegistObj=HttpClient.sendRequestWithHttpClient ( "POST","http://interestion.xyz:3000/app/register",Obj,null );
            status=(String)RegistObj.get ( "status" );
        } catch (InterruptedException e) {
            e.printStackTrace ();
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        if(status.equals ( "SUCCESS" )){

        }else{
            Toast.makeText ( RegistActivity.this,"账号已存在，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        Toast.makeText ( RegistActivity.this,"注册成功！",Toast.LENGTH_LONG ).show ();
        ReturnToLoginInterface();
    }

    private void getData() {
        NickName=editTextNickName.getText ().toString ();
        phonenumber=editTextPhone.getText ().toString ();
        Username=phonenumber;
        Password=editTextPassword.getText ().toString ();
        RepeatPassword=editTextRepeatPassword.getText ().toString ();
    }

    private void ReturnToLoginInterface() {
        RegistActivity.this.finish ();
    }
    // Activity页面onResume函数重载
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 不能遗漏

    }

    // Activity页面onResume函数重载
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 不能遗漏
    }
}
