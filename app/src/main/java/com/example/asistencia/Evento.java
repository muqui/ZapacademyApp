package com.example.asistencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.Retrofit.INodeJS;
import com.example.adapter.AdapterEvent;
import com.example.model.Beneficiary;
import com.example.model.Event;
import com.example.model.Token;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Evento extends AppCompatActivity {
    private ListView listEvents;
    private AdapterEvent adapter;
    List<Event> events;
    Token token;
   // private static Retrofit retrofit;
   private  Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        //recibe datos desde MainActivity
        Bundle bundle = getIntent().getExtras();
        token = (Token) getIntent().getSerializableExtra("token");

        listEvents = (ListView) findViewById(R.id.listEvents);

        Log.d("TOKEN = ", ""+ token.getToken());
            listEvent();







    }



    /*Lista de los eventos vigentes*/
    public void listEvent(){

        if(retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://zapacademy.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        INodeJS api = retrofit.create(INodeJS.class);
        Call<List<Event>> call = api.getListEvent( "Bearer "+token.getToken());
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                List<Event> eventos = response.body();
                events = eventos;
                adapter = new AdapterEvent(Evento.this, new ArrayList<Event>(eventos));
                    //listaPersonas.setAdapter(adapter);
                listEvents.setAdapter(adapter);
                listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //  Beneficiary persona = (Beneficiary) listaPersonas.getAdapter().getItem(position);
                            Event evento = (Event) listEvents.getAdapter().getItem(position);
                        Intent intent = new Intent (getApplicationContext(), home.class);
                        intent.putExtra("event",evento);
                        intent.putExtra("token",token);
                        startActivityForResult(intent, 0);



                        Toast.makeText(Evento.this,"OK",Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.d("Nombre", ""+ t);
                Toast.makeText(Evento.this, "Error " + t , Toast.LENGTH_SHORT).show();
            }
        });

    }
}
