package com.example.Retrofit;



import com.example.model.Attendance;
import com.example.model.Beneficiary;
import com.example.model.Event;
import com.example.model.Token;
import com.example.model.User;

import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registrerUser(@Field("email") String email, @Field("name") String name, @Field("password") String password);

    /*
    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email, @Field("password") String password);
*/
    @POST("login")
    @FormUrlEncoded
    Call<Token> getUser(@Field("email") String email, @Field("password") String password);




   // @GET("beneficiario/{curp}")
   // Observable<String> getBeneficiary(@Path("curp") String curp);



  //  @GET("beneficiario/{curp}")
  //  Call<Beneficiary> getOneBeneficiary(@Path("curp") String curp, @Header("Authorization") String token);


    @GET("beneficiario/{curp}/{evento}")
    Call<Beneficiary> getOneBeneficiary(@Path("curp") String curp,@Path("evento") String evento, @Header("Authorization") String token);



    //Busqueda de beneficiario a partir de la CURP
   // @GET("beneficiario/lista/{filtro}")
   // Call<List<Beneficiary>> getListBeneficiary(@Path("filtro") String filtro , @Header("Authorization") String token);

    //Busqueda de beneficiario a partir de su nombre, primer apellido, segundo apellido
    @GET("beneficiario/listadatos/{nombre}/{primerapellido}/{segundoapellido}/{evento}")
    Call<List<Beneficiary>> getListBeneficiary(@Path("nombre") String filtro ,@Path("primerapellido") String primerapellido ,@Path("segundoapellido") String segundoapellido ,@Path("evento") String evento , @Header("Authorization") String token);

    //Busqueda de beneficiario a partir de la CURP
    @GET("beneficiario/lista/{filtro}/{evento}")
    Call<List<Beneficiary>> getListBeneficiary(@Path("filtro") String filtro ,@Path("evento") String evento , @Header("Authorization") String token);


    @GET("evento/eventos")
    Call<List<Event>> getListEvent(@Header("Authorization") String token);

    @POST("evento/asistencia")
    @FormUrlEncoded
    Call<String> saveAttendance(@Field("status") String status,
                                    @Field("fecha") String fecha,
                                    @Field("event_id") String event_id,
                                    @Field("user_id") String user_id,
                                    @Field("beneficiary_id") String beneficiary_id,
                                @Field("imagen") String imagen,
                                    @Header("Authorization") String token
                                    );


    @POST("evento/asistencia/verificar")
    @FormUrlEncoded
    Call<String> validateAttendance(@Field("event_id") String event_id,@Field("beneficiary_id") String beneficiary_id, @Header("Authorization") String token);


    /*Sube evidencia al servidor imagen en jpg*/
    @POST("evento/asistencia/evidencia")
    @Multipart
    Call<ResponseBody> uploadEvindence(@Part MultipartBody.Part  avatar, @Header("Authorization") String token);




}
