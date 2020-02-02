package com.example.asistencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.Retrofit.INodeJS;
import com.example.model.Attendance;
import com.example.model.Beneficiary;
import com.example.model.Event;
import com.example.model.Token;
import com.example.model.User;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Evidencia extends AppCompatActivity {
    ImageView imgEvidencia;
    Button btnFinalizar;
    Token token;
    Event event;
    Beneficiary beneficiary;
    private  Retrofit retrofitLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidencia);
        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("evidencia");
        //token = (Token) getIntent().getSerializableExtra("token");
        beneficiary = (Beneficiary) getIntent().getSerializableExtra("resultado");
        event = (Event) getIntent().getSerializableExtra("event");
        token = (Token) getIntent().getSerializableExtra("token");



        Log.d("Evento  En Evidencia = ", ""+ event.getNombre());
        imgEvidencia = (ImageView) findViewById(R.id.imgEvidencia);
        imgEvidencia.setImageBitmap(bitmap);
        btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                /*
                Intent intent = new Intent (v.getContext(), home.class);
                intent.putExtra("resultado",beneficiary);
                intent.putExtra("token",token);
                intent.putExtra("event",event);
                startActivityForResult(intent, 0);
                */
               // Log.d("Token Finalizar", ""+ token.getToken() );
                Attendance asistencia = new Attendance("1","2020-02-01",""+event.getId(),""+token.getUsuario().getId(),beneficiary.getId());
                saveAttendance(asistencia);


            }
        });
}

//Registra evidencia

    private void saveAttendance (Attendance asistencia){
        User user = null;


        if(retrofitLogin == null)
            retrofitLogin = new Retrofit.Builder()
                    .baseUrl("https://zapacademy.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        INodeJS api = retrofitLogin.create(INodeJS.class);
        Call<String> call = api.saveAttendance("1","2020-02-01","1","1","97", "Bearer "+token.getToken());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Intent intent = new Intent (Evidencia.this, home.class);
                intent.putExtra("resultado",beneficiary);
                intent.putExtra("token",token);
                intent.putExtra("event",event);
                startActivityForResult(intent, 0);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Token", ""+ t);
                Toast.makeText(Evidencia.this, "Error " + t , Toast.LENGTH_SHORT).show();
            }
        });




    }


}
