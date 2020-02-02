package com.example.asistencia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.Retrofit.INodeJS;
import com.example.model.Beneficiary;
import com.example.model.Event;
import com.example.model.Token;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusquedaManual extends AppCompatActivity implements View.OnClickListener {
    Button btnBusquedaManual;
    ProgressDialog progressDialogBuscando;
    EditText txtBusquedaManual;
    private static Retrofit retrofit;
    Token token;
    Event event;
    Beneficiary beneficiary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_manual);
        btnBusquedaManual = (Button) findViewById(R.id.btnBusquedaManual);
        txtBusquedaManual = (EditText) findViewById(R.id.txtBusquedaManual);
        btnBusquedaManual.setOnClickListener(this);
        progressDialogBuscando = new ProgressDialog(BusquedaManual.this);
        Bundle bundle = getIntent().getExtras();
        // String message = bundle.getString("resultado");
        token = (Token) getIntent().getSerializableExtra("token");
        event = (Event) getIntent().getSerializableExtra("event");
        Log.d(" token Busqueda_manual", ""+ token.getToken());
        Log.d("token Busqueda_manual", ""+ token.getUsuario().getId());

    }

    @Override
    public void onClick(View v) {

        String filtro = txtBusquedaManual.getText().toString().trim();
        if(filtro.length() > 0)
        buscarBeneficiario(filtro, ""+event.getId());
        else
            Toast.makeText(BusquedaManual.this, "Ingrese un texto.. ", Toast.LENGTH_SHORT).show();
    }

    public void  buscarBeneficiario(String filtro, String evento){
        progressDialogBuscando.setTitle("Cargando .....");
        progressDialogBuscando.show();
        if(retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://zapacademy.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        INodeJS api = retrofit.create(INodeJS.class);
        Call<List<Beneficiary>> call = api.getListBeneficiary(filtro,evento,  "Bearer "+token.getToken());
        call.enqueue(new Callback<List<Beneficiary>>() {
            @Override
            public void onResponse(Call<List<Beneficiary>> call, Response<List<Beneficiary>> response) {
                List<Beneficiary> beneficiaries = response.body();

                 Intent intent = new Intent (BusquedaManual.this, ResultadoManual.class);
                intent.putExtra("beneficiaries", (Serializable) beneficiaries);
                intent.putExtra("token",token);

                intent.putExtra("event",event);




                startActivityForResult(intent, 0);
            }

            @Override
            public void onFailure(Call<List<Beneficiary>> call, Throwable t) {
                Log.d("Nombre", ""+ t);
                Toast.makeText(BusquedaManual.this, "Error " + t , Toast.LENGTH_SHORT).show();
            }
        });

        //return  retrofit;
        progressDialogBuscando.dismiss();
    }
}
