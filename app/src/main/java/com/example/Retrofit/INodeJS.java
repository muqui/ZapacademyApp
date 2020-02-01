package com.example.Retrofit;



import com.example.model.Beneficiary;
import com.example.model.Event;
import com.example.model.Token;
import com.example.model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface INodeJS {
    @POST("register")
    @FormUrlEncoded
    Observable<String> registrerUser(@Field("email") String email, @Field("name") String name, @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Observable<String> loginUser(@Field("email") String email, @Field("password") String password);

    @POST("login")
    @FormUrlEncoded
    Call<Token> getUser(@Field("email") String email, @Field("password") String password);




    @GET("beneficiario/{curp}")
    Observable<String> getBeneficiary(@Path("curp") String curp);



    @GET("beneficiario/{curp}")
    Call<Beneficiary> getOneBeneficiary(@Path("curp") String curp, @Header("Authorization") String token);


    @GET("beneficiario/{curp}/{evento}")
    Call<Beneficiary> getOneBeneficiary(@Path("curp") String curp,@Path("evento") String evento, @Header("Authorization") String token);


    @GET("beneficiario/lista/{filtro}")
    Call<List<Beneficiary>> getListBeneficiary(@Path("filtro") String filtro , @Header("Authorization") String token);

    @GET("beneficiario/lista/{filtro}/{evento}")
    Call<List<Beneficiary>> getListBeneficiary(@Path("filtro") String filtro ,@Path("evento") String evento , @Header("Authorization") String token);



    @GET("evento/eventos")
    Call<List<Event>> getListEvent(@Header("Authorization") String token);

}
