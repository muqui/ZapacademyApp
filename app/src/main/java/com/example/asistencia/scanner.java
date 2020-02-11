package com.example.asistencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.media.MediaBrowserServiceCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.Retrofit.INodeJS;
import com.example.model.Beneficiary;
import com.example.model.Event;
import com.example.model.Token;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class scanner extends AppCompatActivity  implements  ZXingScannerView.ResultHandler{
    private ZXingScannerView mScannerView;
    private static final int WRITE_EXST = 1;
    private static final int REQUEST_PERMISSION = 123;
    int PERMISSIONS_REQUEST_ACCESS_CAMERA=0;
    private static Retrofit retrofit;
    int CAMERA;
    String position,formt;
    Token token;
    Event evento;
    Beneficiary beneficiary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Log.d("qr", "SE LANZO cAMARA");

        token = (Token) getIntent().getSerializableExtra("token");
        evento = (Event) getIntent().getSerializableExtra("event");
        Log.d("Regresa QR", "Lector QR regresa" + token.getToken());

        if( ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},5);
            }
        }


        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

    }
    //Regresar
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event){
        if (keycode == KeyEvent.KEYCODE_BACK){


           Intent intent = new Intent (scanner.this, Evento.class);
            intent.putExtra("token",token);
            intent.putExtra("event",evento);

            startActivityForResult(intent, 1);

        }
        return super.onKeyDown(keycode, event);
    }
    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        String resultado =  rawResult.getText();
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

    public void  buscarBeneficiario(String curp, final String event){
        Log.d("Curp", curp);
        Log.d("ID ", event);

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
                    Toast.makeText(scanner.this, "No se encontro CURP " , Toast.LENGTH_SHORT).show();
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
                Toast.makeText(scanner.this, "fail "+ t.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
        //return  retrofit;

    }

    //Lanza la camara en busca de un QR
    private void leerCodigoQR() {
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



