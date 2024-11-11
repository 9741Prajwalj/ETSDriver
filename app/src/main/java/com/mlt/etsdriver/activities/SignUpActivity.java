//package com.mlt.etsdriver.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.mlt.etsdriver.R;
//import com.mlt.etsdriver.utills.MyEditText;
//
//public class SignUpActivity extends AppCompatActivity {
//
//    private MyEditText etMNum, etName, etPassword, etEmail;
//    private ImageView btnSignUp;
//    private TextView txtSignIn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_up); // Ensure this matches your XML layout file
//
//        // Initialize UI components
//        etMNum = findViewById(R.id.etMNum);
//        etName = findViewById(R.id.etName);
//        etPassword = findViewById(R.id.etPassword);
//        etEmail = findViewById(R.id.etEmail);
//        btnSignUp = findViewById(R.id.btnSignUp);
//        txtSignIn = findViewById(R.id.txtSignIn);
//
//        // Set up click listeners for buttons
//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                validateAndSignUp();
//            }
//        });
//
//        txtSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                navigateToLogin();
//            }
//        });
//    }
//
//    private void validateAndSignUp() {
//        // Retrieve values from EditText fields
//        String phone = etMNum.getText().toString().trim();
//        String user_name = etName.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//        String emailid = etEmail.getText().toString().trim();
//
//        // Validation checks
//        if (TextUtils.isEmpty(phone)) {
//            etMNum.setError("Mobile Number is required");
//            etMNum.requestFocus();
//            return;
//        }
//
//        if (TextUtils.isEmpty(user_name)) {
//            etName.setError("Name is required");
//            etName.requestFocus();
//            return;
//        }
//
//        if (TextUtils.isEmpty(password)) {
//            etPassword.setError("Password is required");
//            etPassword.requestFocus();
//            return;
//        }
//
//        if (TextUtils.isEmpty(emailid)) {
//            etEmail.setError("Email is required");
//            etEmail.requestFocus();
//            return;
//        }
//
//        // Show a success message on valid input
//        Toast.makeText(SignUpActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
//
//        // Navigate to another screen (e.g., HomeActivity) upon successful registration
//        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//        startActivity(intent);
//    }
//
//    private void navigateToLogin() {
//        // Navigate to LoginActivity when the "Login" TextView is clicked
//        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
//        startActivity(intent);
//        finish();
//    }
//}
