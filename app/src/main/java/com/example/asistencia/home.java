package com.example.asistencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.Retrofit.INodeJS;
import com.example.Retrofit.RetrofitClient;
import com.example.model.Beneficiary;
import com.example.model.Event;
import com.example.model.Token;
import com.google.zxing.Result;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class home extends AppCompatActivity implements   View.OnClickListener {
    int PERMISSIONS_REQUEST_ACCESS_CAMERA=0;
    private Button btnCode;
    private Button btnManual;
    private Button btnSalir;
    private ZXingScannerView mScannerView;
    ProgressDialog progressDialogBuscando;
    private Intent intent;
    private static Retrofit retrofit;
    Beneficiary beneficiary;
    Token token;
    Event evento;
    INodeJS myAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d("Home", "Home");
        btnCode = (Button) findViewById(R.id.btnScan);
        btnManual = (Button) findViewById(R.id.btnManual);
        btnSalir = (Button) findViewById(R.id.btnSalir);
        progressDialogBuscando = new ProgressDialog(home.this);
        btnCode.setOnClickListener(this);
        btnManual.setOnClickListener(this);
        btnSalir.setOnClickListener(this);
        Retrofit retrofit = RetrofitClient.getInstance();
        myAPI = retrofit.create(INodeJS.class);

        Bundle bundle = getIntent().getExtras();
        // String message = bundle.getString("resultado");
        token = (Token) getIntent().getSerializableExtra("token");
        evento = (Event) getIntent().getSerializableExtra("event");

        Log.d("Token  = ", ""+ token.getToken());
        Log.d("User  = ", ""+ token.getUsuario().toString());
        Log.d("Event id home  = ", ""+ evento.getId());


    }
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event){
        if (keycode == KeyEvent.KEYCODE_BACK){

            intent = new Intent (home.this, Evento.class);
            intent.putExtra("token",token);
            intent.putExtra("event",evento);
            startActivityForResult(intent, 1);

        }
        return super.onKeyDown(keycode, event);
    }

    @Override
    public void onResume(){
        super.onResume();
        if(PERMISSIONS_REQUEST_ACCESS_CAMERA == 1)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_ACCESS_CAMERA);
        } else {
            intent = new Intent (home.this, scanner.class);
            startActivityForResult(intent, 1);

            //mScannerView = new ZXingScannerView(getApplicationContext());
            //setContentView(mScannerView);
            //mScannerView.setResultHandler(this);
            //mScannerView.startCamera();

        }

    }



/*

    @Override
    public void handleResult(Result result) {

        String resultado =  result.getText();
        int finCurp = resultado.indexOf('|');
        String curp = "";
        if(finCurp >=0){  //Si existe la CURP pasa a la siguiente pantalla
          curp = resultado.substring(0,finCurp);
            buscarBeneficiario(curp, ""+ evento.getId());
        }
        else{  //Si no existe la CURP mantiene en la misma pagina
            curp ="x";
            Toast.makeText(getApplicationContext(), "CURP  no existe!!!", Toast.LENGTH_SHORT).show();
           //Toast.makeText(getApplicationContext(), result.getText(), Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    this.checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        PERMISSIONS_REQUEST_ACCESS_CAMERA);
            } else {
                mScannerView = new ZXingScannerView(getApplicationContext());
                setContentView(mScannerView);
                mScannerView.setResultHandler(this);
                mScannerView.startCamera();
                PERMISSIONS_REQUEST_ACCESS_CAMERA= 1;
            }

        }



    }
*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnScan:

                leerCodigoQR();

                break;
            case R.id.btnManual:

                intent = new Intent (v.getContext(), Busqueda.class);
                intent.putExtra("token",token);
                intent.putExtra("event",evento);
                startActivityForResult(intent, 0);


                break;
            case  R.id.btnSalir:
                 intent = new Intent (v.getContext(), MainActivity.class);
                startActivityForResult(intent, 0);
            break;
        }
    }

    //Lanza la camara en busca de un QR

    private void leerCodigoQR() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    PERMISSIONS_REQUEST_ACCESS_CAMERA);
        } else {
           // mScannerView = new ZXingScannerView(getApplicationContext());
           // setContentView(mScannerView);
           // mScannerView.setResultHandler(this);
           // mScannerView.startCamera();
           // PERMISSIONS_REQUEST_ACCESS_CAMERA= 1;
            intent = new Intent (home.this, scanner.class);
            intent.putExtra("token",token);
            intent.putExtra("event",evento);
            startActivityForResult(intent, 1);
        }
    }

    /*

    public void  buscarBeneficiario(String curp, final String event){
        Log.d("Curp", curp);
        Log.d("ID ", event);
        progressDialogBuscando.setTitle("Cargando .....");
        progressDialogBuscando.show();
        if(retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://zapacademy.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        INodeJS api = retrofit.create(INodeJS.class);
        Call<Beneficiary> call = api.getOneBeneficiary(curp,event, "Bearer "+token.getToken());
        call.enqueue(new Callback<Beneficiary>() {
            @Override
            public void onResponse(Call<Beneficiary> call, Response<Beneficiary> response) {

                Log.d("HOY10-02-2020", ""+ response.body());
                     beneficiary = response.body();


                     if(beneficiary.getCurp()  == null){
                         Toast.makeText(home.this, "No se encontro CURP " , Toast.LENGTH_SHORT).show();
                         leerCodigoQR();

                     }
                     else {
                         Intent intent = new Intent (getApplicationContext(), Resultado.class);
                         intent.putExtra("resultado",beneficiary);
                         intent.putExtra("token",token);
                         intent.putExtra("event",evento);



                         startActivityForResult(intent, 0);

                         Log.d("Beneficiario", beneficiary.toString());
                     }


            }

            @Override
            public void onFailure(Call<Beneficiary> call, Throwable t) {
                Toast.makeText(home.this, "fail "+ t.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
        //return  retrofit;
   progressDialogBuscando.dismiss();
    }
    */
}
