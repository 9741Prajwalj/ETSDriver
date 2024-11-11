package com.mlt.etsdriver.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.mlt.etsdriver.R;
import com.mlt.etsdriver.models.DriverLocationRequest;
import com.mlt.etsdriver.models.DriverStatusRequest;
import com.mlt.etsdriver.models.EarningsResponse;
import com.mlt.etsdriver.models.RegistrationRequest;
import com.mlt.etsdriver.models.RouteResponse;
import com.mlt.etsdriver.models.TripDetails;
import com.mlt.etsdriver.models.User;
import com.mlt.etsdriver.network.ApiService;
import com.mlt.etsdriver.network.LoginResponse;
import com.mlt.etsdriver.utills.SharedPreferencesManager;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;

    private CircleImageView profileImage;
    private EditText edtUsername, edtEmail, edtPhone, edtAddress;
    private ImageButton editUsername, editEmail, editPhone, editAddress;
    private MaterialButton btnSave, btnUploadimg;

    private ApiService apiService;
    private SharedPreferencesManager sharedPreferencesManager;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        profileImage = view.findViewById(R.id.profileImage);
        edtUsername = view.findViewById(R.id.edtUsername);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtPhone = view.findViewById(R.id.edtPhone);
        edtAddress = view.findViewById(R.id.edtAddress);
        editUsername = view.findViewById(R.id.editUsername);
        editEmail = view.findViewById(R.id.editEmail);
        editPhone = view.findViewById(R.id.editPhone);
        editAddress = view.findViewById(R.id.editAddress);
        btnSave = view.findViewById(R.id.btnSave);

        apiService = new ApiService() {
            @Override
            public Call<ResponseBody> registerUser(RegistrationRequest request) {
                return null;
            }

            @Override
            public Call<ResponseBody> loginUser(RequestBody requestBody) {
                return null;
            }

            @Override
            public Call<LoginResponse> loginUser(String email, String password) {
                return null;
            }

            @Override
            public Call<Void> updateUserProfile(String mobile, String email, String address) {
                return null;
            }

            @Override
            public Call<Void> updateProfile(String username, String email, String phone, String address, Callback<Void> failedToUpdateProfile) {
                return null;
            }

            @Override
            public Call<EarningsResponse> getEarningsData() {
                return null;
            }

            @Override
            public Call<User> getUserProfile() {
                return null;
            }

            @Override
            public Call<TripDetails> getTripDetails() {
                return null;
            }

            @Override
            public Call<Void> sendCancellationReason(String reasonData) {
                return null;
            }

            @Override
            public Call<Void> updateDriverStatus(String token, DriverStatusRequest statusRequest) {
                return null;
            }

            @Override
            public Call<Void> updateDriverLocation(String token, DriverLocationRequest locationRequest) {
                return null;
            }

            @Override
            public Call<ResponseBody> updateStatus(int userId, int status) {
                return null;
            }

            @Override
            public Call<Void> updateLocation(double latitude, double longitude) {
                return null;
            }

            @Override
            public Call<RouteResponse> getRoute(String origin, String destination, String apiKey) {
                return null;
            }

//            @Override
//            public Call<ResponseBody> updateStatus(RequestBody requestBody) {
//                return null;
//            }
        };
        sharedPreferencesManager = new SharedPreferencesManager(getContext());

        // Handle Image Upload (Camera/Gallery)
        view.findViewById(R.id.btnUploadimg).setOnClickListener(v -> showImagePickerDialog());

        // Handle Edit Profile Fields
        editUsername.setOnClickListener(v -> edtUsername.setEnabled(true));
        editEmail.setOnClickListener(v -> edtEmail.setEnabled(true));
        editPhone.setOnClickListener(v -> edtPhone.setEnabled(true));
        editAddress.setOnClickListener(v -> edtAddress.setEnabled(true));

        // Handle Save Button
        btnSave.setOnClickListener(v -> saveData());

        return view;
    }

    // Show Image Picker Dialog (Camera or Gallery)
    private void showImagePickerDialog() {
        String[] options = {"Camera", "Gallery"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Open Camera
                checkCameraPermission();
            } else {
                // Open Gallery
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });
        builder.show();
    }

    // Handle Image Picker Results (Camera or Gallery)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Uri imageUri = data.getData();
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                profileImage.setImageBitmap(bitmap);
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                    profileImage.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Check Camera Permission
    public void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request permission
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1); // 1 is the request code
        } else {
            // Permission already granted, proceed with camera intent
            openCamera();
        }
    }

    // Handle the result of permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with camera intent
                openCamera();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Open Camera
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE); // CAMERA_REQUEST_CODE is the request code
    }

    // Validate and Save Data
    private void saveData() {
        String username = edtUsername.getText().toString();
        String email = edtEmail.getText().toString();
        String phone = edtPhone.getText().toString();
        String address = edtAddress.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save Data Locally (SharedPreferences)
        sharedPreferencesManager.saveUserProfile(username, email, phone, address);

        // Send Data to Backend (API call)
        apiService.updateProfile(username, email, phone, address, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

