package com.example.asistencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.model.Beneficiary;
import com.example.model.Event;
import com.example.model.Token;

public class Evidencia extends AppCompatActivity {
    ImageView imgEvidencia;
    Button btnFinalizar;
    Token token;
    Event event;
    Beneficiary beneficiary;
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



        Log.d("Token En Evidencia = ", ""+ token.getToken());
        Log.d("User En Evidencia = ", ""+ token.getUsuario().toString());
        imgEvidencia = (ImageView) findViewById(R.id.imgEvidencia);
        imgEvidencia.setImageBitmap(bitmap);
        btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), home.class);
               // intent.putExtra("token",token);
                intent.putExtra("resultado",beneficiary);
                intent.putExtra("token",token);
                intent.putExtra("event",event);
                startActivityForResult(intent, 0);
            }
        });
}
}
