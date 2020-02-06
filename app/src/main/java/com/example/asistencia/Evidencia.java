package com.example.asistencia;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
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
    Bitmap bitmap;
    private  Retrofit retrofitLogin;
    String nombreImagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidencia);
        Intent intent = getIntent();
        //bitmap = (Bitmap) intent.getParcelableExtra("evidencia");
        String imgf = (String) intent.getStringExtra("evidencia");

        Log.d("Imagen bitmap  = ", ""+ imgf);

        beneficiary = (Beneficiary) getIntent().getSerializableExtra("resultado");
        event = (Event) getIntent().getSerializableExtra("event");
        token = (Token) getIntent().getSerializableExtra("token");

        File file = new File(imgf);

        try {
            bitmap = MediaStore.Images.Media
                    .getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Evento  En Evidencia = ", ""+ event.getNombre());
        imgEvidencia = (ImageView) findViewById(R.id.imgEvidencia);
        imgEvidencia.setImageBitmap(bitmap);
        btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



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
         nombreImagen = ""+beneficiary.getCurp()+"_"+event.getId();
        Log.d("Respuesta = ", ""+ event.getId());
        Log.d("Respuesta = ", ""+ beneficiary.getId());
        Log.d("Respuesta = ", ""+ beneficiary.getCurp());
        Call<String> call = api.saveAttendance("1","2020-02-01",""+event.getId(),""+ token.getUsuario().getId(),""+beneficiary.getId(), nombreImagen,"Bearer "+token.getToken());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String respuesta = response.body();

                if(respuesta.equalsIgnoreCase("exists")){

                    Toast.makeText(Evidencia.this, "Asistencia ya ha sido registrada "  , Toast.LENGTH_SHORT).show();

                }
                else{
                    saveEvidence(""+ beneficiary.getCurp()+"_"+event.getId());
                     Intent intent = new Intent (Evidencia.this, home.class);
                     intent.putExtra("resultado",beneficiary);
                    intent.putExtra("token",token);
                     intent.putExtra("event",event);
                     startActivityForResult(intent, 0);
                }



            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Token", ""+ t);
                Toast.makeText(Evidencia.this, "Error " + t , Toast.LENGTH_SHORT).show();
            }
        });




    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (!(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    Toast.makeText(Evidencia.this, "Permission denied to access your location.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    public void saveEvidence(String nombre){
        FileOutputStream outStream = null;





        // Write to SD Card
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/zapacademy/") ;
            dir.mkdirs();

            String fileName = String.format("%S.jpg", nombre);
            File outFile = new File(dir, fileName);

            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();



        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }


}
