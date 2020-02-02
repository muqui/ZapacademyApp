package com.example.asistencia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Retrofit.INodeJS;
import com.example.Retrofit.RetrofitClient;
import com.example.model.Token;
import com.example.model.User;


import java.io.IOException;
import java.io.Serializable;

import io.reactivex.android.schedulers.AndroidSchedulers;


import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static Retrofit retrofitLogin;

    private Button btnLogin;
    EditText txtUser, txtPassword;
    ProgressDialog progressDialogLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUser = (EditText) findViewById(R.id.txtUser);
        txtPassword  = (EditText) findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        progressDialogLogin = new ProgressDialog(MainActivity.this);
          Retrofit retrofit = RetrofitClient.getInstance();
          myAPI = retrofit.create(INodeJS.class);

    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event){
        if (keycode == KeyEvent.KEYCODE_BACK){

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);

        }
        return super.onKeyDown(keycode, event);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        try {

            if(txtUser.getText().length() >0 && txtPassword.getText().length() > 0){
                progressDialogLogin.setMessage("loading");
                progressDialogLogin.show();
               // loginUser(txtUser.getText().toString(), txtPassword.getText().toString());
                getUser(txtUser.getText().toString(), txtPassword.getText().toString());
            }

            else
                Toast.makeText(MainActivity.this, "Capture Usuario y/o contraseña " , Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private User getUser (String email, String password){
        User user = null;


        if(retrofitLogin == null)
            retrofitLogin = new Retrofit.Builder()
                    .baseUrl("https://zapacademy.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        INodeJS api = retrofitLogin.create(INodeJS.class);
        Call<Token> call = api.getUser(email,password);
        call.enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                Token token = response.body();

                progressDialogLogin.dismiss();
                Intent intent = new Intent(MainActivity.this, Evento.class);
                intent.putExtra("token",token);
                startActivityForResult(intent, 0);

            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.d("Token", ""+ t);
                Toast.makeText(MainActivity.this, "Error " + t , Toast.LENGTH_SHORT).show();
            }
        });


        return null;

    }
    /*
    private void loginUser(String email, String password) throws Exception {



            compositeDisposable.add(myAPI.loginUser(email, password)
                    .subscribeOn(Schedulers.io())

                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {


                                   @Override
                                   public void accept(String s) {

                                       if (s.contains("email")) {
                                           progressDialogLogin.dismiss();
                                           Intent intent = new Intent(MainActivity.this, home.class);
                                           startActivityForResult(intent, 0);
                                       } else{
                                           progressDialogLogin.dismiss();
                                           Toast.makeText(MainActivity.this, "Error " + s, Toast.LENGTH_LONG).show();
                                       }



                                   }


                               }, new Consumer<Throwable>() {
                                   @Override
                                   public void accept(Throwable throwable) throws Exception {
                                       progressDialogLogin.dismiss();
                                      Toast.makeText(MainActivity.this, "fail " + throwable, Toast.LENGTH_SHORT).show();
                                   }
                               }
                    )



            );




    }

     */


}
