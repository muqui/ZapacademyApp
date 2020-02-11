package com.example.asistencia;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
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
import java.text.SimpleDateFormat;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Evidencia extends AppCompatActivity implements View.OnClickListener {
    ImageView imgEvidencia;
    Button btnFinalizar;
    Token token;
    Event event;
    Beneficiary beneficiary;
    Bitmap bitmap;
    ProgressDialog progressDialog;
    private  Retrofit retrofitLogin;
    String nombreImagen;
    String imgf;
    FileOutputStream outStream = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evidencia);
        progressDialog = new ProgressDialog(Evidencia.this);
        Intent intent = getIntent();
        //bitmap = (Bitmap) intent.getParcelableExtra("evidencia");

        //Image Path
         imgf = (String) intent.getStringExtra("evidencia");


        beneficiary = (Beneficiary) getIntent().getSerializableExtra("resultado");
        event = (Event) getIntent().getSerializableExtra("event");
        token = (Token) getIntent().getSerializableExtra("token");

        File file = new File(imgf);

        try {
            //Carga la imagen en carpeta temporal a un bitmap
            bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Evento  En Evidencia = ", ""+ event.getNombre());
        imgEvidencia = (ImageView) findViewById(R.id.imgEvidencia);
        imgEvidencia.setImageBitmap(bitmap);
        btnFinalizar = (Button) findViewById(R.id.btnFinalizar);
        btnFinalizar.setOnClickListener(this);
        saveEvidence( beneficiary.getCurp()+"_"+ event.getId());

}


    // Si el Usuario se regresa será llevado al layout resultado, retornandole los datos que recibio.
    @Override
    public boolean onKeyDown(int keycode, KeyEvent event){
        if (keycode == KeyEvent.KEYCODE_BACK){

           Intent intent = new Intent (Evidencia.this, Resultado.class);
            intent.putExtra("resultado",beneficiary);
            intent.putExtra("token",token);
            intent.putExtra("event",event);
            startActivityForResult(intent, 1);

        }
        return super.onKeyDown(keycode, event);
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
        Call<String> call = api.saveAttendance("1",asistencia.getFecha(),""+event.getId(),""+ token.getUsuario().getId(),""+beneficiary.getId(), nombreImagen,"Bearer "+token.getToken());
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Registrando asistencia ........");
        pd.setIndeterminate(true);
        pd.setCancelable(false);

        call.enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                pd.show();
                String respuesta = response.body();

                if(respuesta.equalsIgnoreCase("exists")){
                   showDialog();
                    Toast.makeText(Evidencia.this, "Asistencia ya ha sido registrada "  , Toast.LENGTH_SHORT).show();

                }
                else{
                    saveEvidence(""+ beneficiary.getCurp()+"_"+event.getId());

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


        // Write to SD Card
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            File dir = new File(sdCard.getAbsolutePath() + "/zapacademy/") ;
            dir.mkdirs();

            String fileName = String.format("%S.jpg", nombre);
            File outFile = new File(dir, fileName);

            outStream = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, outStream);
            outStream.flush();
            outStream.close();



        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }

    }



    //Subir evidencia al server
   private void uploadEvidence (){
       final ProgressDialog pd = new ProgressDialog(this);
       pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       pd.setMessage("Subiendo evidencia ........");
       pd.setIndeterminate(true);
       pd.setCancelable(false);
       saveEvidence( beneficiary.getCurp()+"_"+ event.getId());
       File sdCard = Environment.getExternalStorageDirectory();
       File dir = new File(sdCard.getAbsolutePath() + "/zapacademy/" + beneficiary.getCurp()+"_"+ event.getId()+".jpg") ;


       File file = new File(imgf);
       Log.d("dir ss", "" + dir.getPath());
       File newFile = new File(file.getParent()+"/"+ beneficiary.getCurp()+"_"+ event.getId()+".jpg");

       Log.d("Imagen evidencia new", "" + newFile.getName());


       file.renameTo(newFile);
       Log.d("Imagen file", "" + file.getName());
       Log.d("Imagen new ", "" + newFile.getName());

       //RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), newFile);
       //MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", newFile.getName(), requestBody);
       RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), dir);
       MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", dir.getName(), requestBody);


       if(retrofitLogin == null)
            retrofitLogin = new Retrofit.Builder()
                    .baseUrl("https://zapacademy.herokuapp.com/")


                    .build();
        INodeJS api = retrofitLogin.create(INodeJS.class);
        Call<ResponseBody> call = api.uploadEvindence(body,"Bearer "+token.getToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                pd.show();
                showDialog();
                 Intent intent = new Intent (Evidencia.this, home.class);
                 intent.putExtra("resultado",beneficiary);
                 intent.putExtra("token",token);
                 intent.putExtra("event",event);
                 startActivityForResult(intent, 0);
                Toast.makeText(Evidencia.this, "Asistencia realiza con  Exito!!" , Toast.LENGTH_SHORT).show();
                pd.dismiss();
}

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Token", ""+ t);
                Toast.makeText(Evidencia.this, "Error " + t , Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public void onClick(View v) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Log.d("Imagen evidencia new", "" + f.format(new Date()));
        Attendance asistencia = new Attendance("1",""+f.format(new Date()),""+event.getId(),""+token.getUsuario().getId(),beneficiary.getId());
              saveAttendance(asistencia);
        uploadEvidence();


    }

    public void showDialog() {

        if(progressDialog != null && !progressDialog.isShowing())
            progressDialog.setMessage("Subiendo evidencia ........");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
            progressDialog.show();
    }
}
