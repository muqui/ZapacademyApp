package com.example.asistencia;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.Retrofit.INodeJS;
import com.example.model.Attendance;
import com.example.model.Beneficiary;
import com.example.model.Event;
import com.example.model.Token;
import com.example.model.User;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class Resultado extends AppCompatActivity implements View.OnClickListener  {
    TextView txtResultadoCurp, txtResultadonombre, txtResultadoPaterno, txtResultadoMaterno, txtResultadoSexo, txtResultadoCalle, txtResultadoNumint, txtResultadoNumExt;
    TextView txtResultadoCP, txtResultadoColonia, txtResultadoMunicipio, txtResultadoEstado, txtResultadoTelefono, txtResultadoCelular;
    Button btnTomarFotografia;
    Beneficiary beneficiary;
    Token token;
    Event evento;
    private Uri mImageUri;
    File photo;
    private  Retrofit retrofitLogin;
    String mCurrentPhotoPath;
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
                validateAttendance();
               // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
               // startActivityForResult(intent,0);
                Log.d("foto", "Tomar foto");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap bitmap = (Bitmap) data.getExtras().get("data");

        Log.d("Imagen temporal = ", ""+ mCurrentPhotoPath);

        try {
            File file = new File(mCurrentPhotoPath);
            String urlImagen = mCurrentPhotoPath;
            Bitmap  bitmap = MediaStore.Images.Media
                    .getBitmap(Resultado.this.getContentResolver(), Uri.fromFile(file));


            Intent intent = new Intent (getApplicationContext(), Evidencia.class);
           // intent.putExtra("evidencia",bitmap);
            intent.putExtra("evidencia",mCurrentPhotoPath);
              intent.putExtra("resultado",beneficiary);
             intent.putExtra("token",token);
             intent.putExtra("event",evento);

            startActivityForResult(intent, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(" temporal error = ", ""+ e);
        }


    }



    //Validar asisitencia
    private void validateAttendance (){
        User user = null;


        if(retrofitLogin == null)
            retrofitLogin = new Retrofit.Builder()
                    .baseUrl("https://zapacademy.herokuapp.com/")
                    .addConverterFactory(GsonConverterFactory.create())

                    .build();
        INodeJS api = retrofitLogin.create(INodeJS.class);
        String nombreImagen = ""+beneficiary.getCurp()+"_"+evento.getId();
        Log.d("Respuesta = ", ""+ evento.getId());
        Log.d("Respuesta = ", ""+ beneficiary.getId());
        Log.d("Respuesta = ", ""+ beneficiary.getCurp());
        Call<String> call = api.validateAttendance(""+evento.getId(),""+ beneficiary.getId(),"Bearer "+token.getToken());
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String respuesta = response.body();

                if(respuesta.equalsIgnoreCase("exists")){

                    Toast.makeText(Resultado.this, "Asistencia ya ha sido registrada "  , Toast.LENGTH_SHORT).show();

                }
                else{


                    try {


                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File photoFile = createImageFile();
                        Uri uri = FileProvider.getUriForFile(Resultado.this,
                                "com.example.asistencia.fileprovider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        startActivityForResult(intent,1);

                    }
                    catch (Exception e){
                        Log.d("Tomar foro", ""+ e);

                    }



                }



            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Token", ""+ t);
                Toast.makeText(Resultado.this, "Error " + t , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
       // File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }



}
