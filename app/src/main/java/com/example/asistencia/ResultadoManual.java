package com.example.asistencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapter.Adapter;
import com.example.model.Beneficiary;
import com.example.model.Persona;
import com.example.model.Token;

import java.util.ArrayList;
import java.util.List;

public class ResultadoManual extends AppCompatActivity {
    private ListView listaPersonas;
    private Adapter adapter;
    List<Beneficiary> beneficiaries;
    Token token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado_manual);
        //datos recibidos del BusquedaManual.
        Bundle bundle = getIntent().getExtras();
        beneficiaries = (List<Beneficiary>) bundle.getSerializable("beneficiaries");
        token = (Token) getIntent().getSerializableExtra("token");

        for(Beneficiary b : beneficiaries){
            Log.d("CURP : ", b.getCurp());
            Log.d("Nombre :  ", b.getNombre());
        }


        listaPersonas = (ListView) findViewById(R.id.listResult);
        adapter = new Adapter(this, new ArrayList<Beneficiary>(beneficiaries));


        listaPersonas.setAdapter(adapter);
        listaPersonas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Beneficiary persona = (Beneficiary) listaPersonas.getAdapter().getItem(position);

                Intent intent = new Intent (getApplicationContext(), Resultado.class);
                intent.putExtra("resultado",persona);
                intent.putExtra("token",token);
                startActivityForResult(intent, 0);
                Log.d("scanx" , persona.getCurp());
                Log.d("CURP" , persona.getCurp());
                Toast.makeText(ResultadoManual.this,persona.getCurp(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<Beneficiary> getPersona(){
      ArrayList<Beneficiary> listPersonas = new ArrayList<>();
      listPersonas.add(new Beneficiary("corona", "corona"));
        listPersonas.add(new Beneficiary("navarro", "navarro"));

      return listPersonas;
    }
}
