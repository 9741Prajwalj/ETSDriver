package com.mlt.etsdriver.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mlt.etsdriver.network.ApiClient;
import com.mlt.etsdriver.network.ApiService;
import com.mlt.etsdriver.R;
import com.mlt.etsdriver.models.RegistrationRequest;
import com.mlt.etsdriver.utills.MyEditText;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private MyEditText etMNum, etName, etEmail, etPassword;
    private ImageView btnSignUp;

    private TextView txtSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etMNum = findViewById(R.id.etMNum);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtSignIn = findViewById(R.id.txtSignIn);

        txtSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
        btnSignUp.setOnClickListener(v -> {
            validateAndSignUp(); // Implement your validation and sign-up logic
        });
    }

    private void validateAndSignUp() {
        String user_name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String emailid = etEmail.getText().toString().trim();
        String phone = etMNum.getText().toString().trim();

        if (user_name.isEmpty() || password.isEmpty() || emailid.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        registerUser(phone, user_name, emailid, password);
    }

    private void registerUser(String phone, String user_name, String emailid, String password) {
        JSONObject signUpRequest = new JSONObject();
        try {
            signUpRequest.put("phone", phone);
            signUpRequest.put("user_name", user_name);
            signUpRequest.put("password", password);
            signUpRequest.put("emailid", emailid);
        } catch (JSONException e) {
            Log.e("SignUpError", "JSON Error: " + e.getMessage());
            Toast.makeText(this, "Error creating JSON request", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create and populate RegistrationRequest object
        RegistrationRequest registrationRequest = new RegistrationRequest(phone, user_name, emailid, password);

        // Get the API service instance
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        Call<ResponseBody> call = apiService.registerUser(registrationRequest);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBodyString = response.body().string();
                        Log.d("SignUpActivity Response", responseBodyString);

                        JSONObject jsonResponse = new JSONObject(responseBodyString);
                        int success = jsonResponse.optInt("success", 0);

                        if (success == 1) {
                            String message = jsonResponse.optString("message", "Registration successful!");
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                            resetFields();
                            navigateToHome();
                        } else {
                            String message = jsonResponse.optString("message", "Registration failed!");
                            Toast.makeText(RegisterActivity.this, "Registration Failed: " + message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("SignUpError", "JSON Parsing Error: " + e.getMessage());
                        Toast.makeText(RegisterActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("SignUpResponse", "Error: " + response.code() + " - " + response.message());
                    Toast.makeText(RegisterActivity.this, "Sign-Up Failed", Toast.LENGTH_SHORT).show();
                }
                resetFields();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("SignUpError", "Failure: " + t.getMessage());
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                resetFields();
            }
        });
    }

    private void resetFields() {
        etName.setText("");
        etPassword.setText("");
        etEmail.setText("");
        etMNum.setText("");
    }


    private void navigateToHome() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
