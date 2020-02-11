package com.example.asistencia;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class BusquedaCURPFragment extends Fragment   implements View.OnClickListener{
    Button btnDatosPersonales;
    EditText txtBusquedaCurp;
    private static Retrofit retrofit;
    Token token;
    Event event;


    public BusquedaCURPFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_busqueda_cur, container, false);
        View view = inflater.inflate(R.layout.fragment_busqueda_cur, container, false);
        btnDatosPersonales = (Button) view.findViewById(R.id.btnDatosBuscar); //boton buscar mediante datos personales
        txtBusquedaCurp = (EditText) view.findViewById(R.id.txtBusquedaCurp);
        btnDatosPersonales.setOnClickListener(this);

        token = (Token) getArguments().getSerializable("token");
        event = (Event) getArguments().getSerializable("event");
        Log.d("Event en Fragment", ""+ event.getNombre());

        return view;
    }

    @Override
    public void onClick(View v) {
        String filtro = txtBusquedaCurp.getText().toString().trim();
        if(filtro.length() > 0)
            buscarBeneficiario(filtro, ""+event.getId());
        else
            Toast.makeText(getActivity(), "Ingrese un texto.. ", Toast.LENGTH_SHORT).show();
    }
    public void  buscarBeneficiario(String filtro, String evento){
       // progressDialogBuscando.setTitle("Cargando .....");
       // progressDialogBuscando.show();
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

                Intent intent = new Intent (getActivity(), ResultadoManual.class);
                intent.putExtra("beneficiaries", (Serializable) beneficiaries);
                intent.putExtra("token",token);

                intent.putExtra("event",event);




                startActivityForResult(intent, 0);
            }

            @Override
            public void onFailure(Call<List<Beneficiary>> call, Throwable t) {
                Log.d("Nombre", ""+ t);
                Toast.makeText(getActivity(), "Error " + t , Toast.LENGTH_SHORT).show();
            }
        });

        //return  retrofit;
      //  progressDialogBuscando.dismiss();
    }
}
