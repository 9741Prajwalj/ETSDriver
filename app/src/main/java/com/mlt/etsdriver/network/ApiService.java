package com.mlt.etsdriver.network;

import static com.mlt.etsdriver.network.RetrofitClient.BASE_URL;
import static com.mlt.etsdriver.network.RetrofitClient.retrofit;

import com.mlt.etsdriver.models.DriverLocationRequest;
import com.mlt.etsdriver.models.DriverStatusRequest;
import com.mlt.etsdriver.models.EarningsResponse;
import com.mlt.etsdriver.models.RegistrationRequest;
import com.mlt.etsdriver.models.RouteResponse;
import com.mlt.etsdriver.models.User;

import org.json.JSONObject;
import com.mlt.etsdriver.models.TripDetails;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;


public interface ApiService {

    @POST("api/driver-registration")
    Call<ResponseBody> registerUser(@Body RegistrationRequest request);

    // Define the endpoint for user login
    @POST("api/user-login")
    Call<ResponseBody> loginUser(@Body RequestBody requestBody);

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("user/updateProfile")
    Call<Void> updateUserProfile(
            @Field("mobile_no") String mobile,
            @Field("email") String email,
            @Field("address") String address
    );
    @FormUrlEncoded
    @POST("update_profile")
    Call<Void> updateProfile(@Field("username") String username,
                             @Field("email") String email,
                             @Field("phone") String phone,
                             @Field("address") String address, Callback<Void> failedToUpdateProfile);

    @GET("/api/earnings") // Replace with the actual endpoint path for fetching earnings data
    Call<EarningsResponse> getEarningsData();

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // For JSON conversion
                    .build();
        }
        return retrofit;
    }
    public static Retrofit getInstances() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    @GET("user/profile")
    Call<User> getUserProfile();

    @GET("path/to/your/endpoint")  // Specify your endpoint here
    Call<TripDetails> getTripDetails();

    @POST("/api/cancel")
    Call<Void> sendCancellationReason(@Body String reasonData);

    // Endpoint to update driver status (online/offline)
    @POST("/api/change-availability")
    Call<Void> updateDriverStatus(
            @Header("Authorization") String token,
            @Body DriverStatusRequest statusRequest
    );

    // Endpoint to update driver location
    @POST("/api/change-availability")
    Call<Void> updateDriverLocation(
            @Header("Authorization") String token,
            @Body DriverLocationRequest locationRequest
    );

    @GET("com/mlt/etsdriver/activities/LoginActivity.java") // Replace with your actual endpoint
    Call<JSONObject> getUserInfo();
    @POST("/api/change-availability")
    Call<ResponseBody> updateStatus(@Field("user_id") int userId, @Field("status") int status);

    // Update location to backend
    @POST("updateLocation")
    Call<Void> updateLocation(@Query("latitude") double latitude, @Query("longitude") double longitude);

    // Get route from source to destination using Google Directions API
    @GET("directions/json")
    Call<RouteResponse> getRoute(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String apiKey
    );

}
