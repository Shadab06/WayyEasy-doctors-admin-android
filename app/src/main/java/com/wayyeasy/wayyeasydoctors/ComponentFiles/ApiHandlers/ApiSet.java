package com.wayyeasy.wayyeasydoctors.ComponentFiles.ApiHandlers;

import com.wayyeasy.wayyeasydoctors.Models.verify_response_model;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiSet {
    /*
    @POST("user/signup")
    Call<Void> generateOTP(@Body HashMap<String, String> map);

    @POST("user/verifyOTP")
    Call<user_verify_response_model> login(@Body HashMap<String, String> map);

    @GET("user/getUser")
    Call<user_response_model> fetchUser(@Header("Authorization") String token);

    @PATCH("user/update")
    Call<user_response_model> updateProfile(@Header("Authorization") String token,
                             @Body HashMap<String, String> map);

    @GET("/hospital/userView")
    Call<List<hospital_response_model>> getFilteredHospital(@Header("Authorization") String token,
                                                            @Query("name") String name,
                                                            @Query("value") String value,
                                                            @Query("limit") int limit,
                                                            @Query("page") int page);

    @GET("/hospital/userView")
    Call<List<search_response_model>> searchHospitals(@Header("Authorization") String token,
                                                      @Query("name") String name,
                                                      @Query("limit") int limit,
                                                      @Query("page") int page);

    @GET("room/getRoomsByHospital/{hospitalId}")
    Call<List<beds_response_model>> getRoomsByHospital(@Header("Authorization") String token,
                                                       @Path("hospitalId") String hospitalId);

    @GET("doctor/getDoctorsByHospital/{hospitalId}")
    Call<List<doctors_response_model>> getDoctorsByHospital(@Header("Authorization") String token,
                                                            @Path("hospitalId") String hospitalId);

    @GET("ratings/getRatingsByHospital/{hospitalId}")
    Call<List<ratings_response_model>> getRatingsByHospital(@Header("Authorization") String token,
                                                            @Path("hospitalId") String hospitalId);

    //Physicians
    @GET("/hospital/userView")
    Call<List<physician_response_model>> getPhysicians(@Header("Authorization") String token,
                                                         @Query("limit") int limit,
                                                         @Query("page") int page);

    //Path Labs
    @GET("/pathLabs/viewAll")
    Call<List<path_lab_response_model>> getAllPathLabs(@Header("Authorization") String token);
         */

    @POST("/physicians/Signup")
    Call<verify_response_model> register(@Body HashMap<String, String> map);

    @POST("/physicians/login")
    Call<verify_response_model> login(@Body HashMap<String, String> map);
}
