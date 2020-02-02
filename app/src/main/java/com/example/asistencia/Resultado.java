package com.example.asistencia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.model.Beneficiary;
import com.example.model.Event;
import com.example.model.Token;

public class Resultado extends AppCompatActivity implements View.OnClickListener  {
    TextView txtResultadoCurp, txtResultadonombre, txtResultadoPaterno, txtResultadoMaterno, txtResultadoSexo, txtResultadoCalle, txtResultadoNumint, txtResultadoNumExt;
    TextView txtResultadoCP, txtResultadoColonia, txtResultadoMunicipio, txtResultadoEstado, txtResultadoTelefono, txtResultadoCelular;
    Button btnTomarFotografia;
    Beneficiary beneficiary;
    Token token;
    Event evento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        Bundle bundle = getIntent().getExtras();
       // String message = bundle.getString("resultado");
        beneficiary = (Beneficiary) getIntent().getSerializableExtra("resultado");
        evento = (Event) getIntent().getSerializableExtra("event");
        token = (Token) getIntent().getSerializableExtra("token");


        Log.d("Token En resultado  = ", ""+ token.getToken());
      Log.d("User   en resultado  = ", ""+ evento.getNombre());
       // Log.d("scanx1" , message);
         btnTomarFotografia = (Button) findViewById(R.id.btnTomarFotografia);
         btnTomarFotografia.setOnClickListener(this);
        txtResultadoCurp  = (TextView) findViewById(R.id.txtResultadoCurp);
        txtResultadonombre = (TextView) findViewById(R.id.txtResultadoNombre);
        txtResultadoPaterno = (TextView) findViewById(R.id.txtResultadoAP);
        txtResultadoMaterno = (TextView) findViewById(R.id.txtResultadoAM);
        txtResultadoSexo = (TextView) findViewById(R.id.txtResultadoSexo);
        txtResultadoCalle = (TextView) findViewById(R.id.txtResultadoCalle);
        txtResultadoNumint = (TextView) findViewById(R.id.txtResultadoNumInt);
        txtResultadoNumExt = (TextView) findViewById(R.id.txtResultadoNumExt);
        txtResultadoCP = (TextView) findViewById(R.id.txtResultadoCP);
        txtResultadoColonia = (TextView) findViewById(R.id.txtResultadoColonia);
        txtResultadoMunicipio = (TextView) findViewById(R.id.txtResultadoMunicipio);
        txtResultadoEstado = (TextView) findViewById(R.id.txtResultadoEstado);
        txtResultadoTelefono = (TextView) findViewById(R.id.txtResultadoTelefono);
        txtResultadoCelular = (TextView) findViewById(R.id.txtResultadoCelular);

        txtResultadoCurp.setText("CURP: "+beneficiary.getCurp());
        txtResultadonombre.setText("Nombre: "+beneficiary.getNombre());
        txtResultadoPaterno.setText("A Paterno: "+beneficiary.getApellidoPaterno());
        txtResultadoMaterno.setText("A Materno: "+beneficiary.getApellidoMaterno());
        txtResultadoSexo.setText("Sexo: "+beneficiary.getSexo());
        txtResultadoCalle.setText("Calle:"+beneficiary.getCalle());
        txtResultadoNumint.setText("Num. Interior: "+beneficiary.getNumeroInt());
        txtResultadoNumExt.setText("Num. Exterior: "+beneficiary.getNumeroExt());
        txtResultadoCP.setText("Codigo Postal: "+beneficiary.getCodigoPostal());
        txtResultadoColonia.setText("Colonia: "+beneficiary.getColonia());
        txtResultadoMunicipio.setText("Municipio: "+beneficiary.getMunicipio());
        txtResultadoEstado.setText("Estado: "+beneficiary.getEstado());
        txtResultadoTelefono.setText("Telefono: "+beneficiary.getTelefono());
        txtResultadoCelular.setText("Celular: "+beneficiary.getCelular());





    }

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event){
        if (keycode == KeyEvent.KEYCODE_BACK){
            Log.d("regreso", "regreso");
            Intent intent = new Intent (getApplicationContext(), home.class);
            intent.putExtra("resultado",beneficiary);
            intent.putExtra("token",token);
            intent.putExtra("event",event);
            startActivityForResult(intent, 0);

            return true;

        }
        return super.onKeyDown(keycode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTomarFotografia:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,0);
                Log.d("foto", "Tomar foto");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        Intent intent = new Intent (getApplicationContext(), Evidencia.class);
        intent.putExtra("evidencia",bitmap);
        intent.putExtra("resultado",beneficiary);
        intent.putExtra("token",token);
        intent.putExtra("event",evento);

        startActivityForResult(intent, 0);

    }
}
