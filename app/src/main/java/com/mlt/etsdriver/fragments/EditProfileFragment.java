package com.mlt.etsdriver.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.mlt.etsdriver.R;
import com.mlt.etsdriver.helper.SharedPreferencesManager;
import de.hdodenhof.circleimageview.CircleImageView;
public class EditProfileFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 1;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int PICK_IMAGE_REQUEST = 3;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 4;
    private CircleImageView profileImage;
    private TextView txtUserName, txtEmail, txtPhone, txtAddress;
    private SharedPreferencesManager sharedPreferencesManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        profileImage = view.findViewById(R.id.profileImage);
        // Initialize the TextViews using the root view of the fragment
        txtUserName = view.findViewById(R.id.txtUserName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtAddress = view.findViewById(R.id.txtAddress);
        LinearLayout card2 = view.findViewById(R.id.card2);

        txtUserName.setVisibility(View.VISIBLE);
        txtEmail.setVisibility(View.VISIBLE);
        txtPhone.setVisibility(View.VISIBLE);
        txtAddress.setVisibility(View.VISIBLE);

        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show Toast message when card2 is clicked
                Toast.makeText(getContext(), "If you want to Update Details contact to Company Registration", Toast.LENGTH_SHORT).show();
            }
        });

        /// Initialize sharedPreferencesManager
        if (sharedPreferencesManager == null) {
            sharedPreferencesManager = new SharedPreferencesManager(getContext());
        }


        txtUserName.setText(sharedPreferencesManager.getUserName());
        txtEmail.setText(sharedPreferencesManager.getEmail());
        txtPhone.setText(sharedPreferencesManager.getPhone());
        txtAddress.setText(sharedPreferencesManager.getAddress());


        // Handle Image Upload (Camera/Gallery)
        view.findViewById(R.id.btnUploadimg).setOnClickListener(v -> showImagePickerDialog());

        return view;
    }


    // Show Image Picker Dialog (Camera or Gallery)
    private void showImagePickerDialog() {
        String[] options = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                // Open Camera
                checkCameraPermission();
            } else {
                // Open Gallery
                checkGalleryPermission();
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
            } else if (requestCode == GALLERY_REQUEST_CODE && data != null) {
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

        // Check Gallery Permission (READ_EXTERNAL_STORAGE for Android versions below Android 11)
        public void checkGalleryPermission() {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request permission
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2); // 2 is the request code
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // Permission is granted, open gallery
                    openGallery();
                } else {
                    // Request permission to manage all files
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, STORAGE_PERMISSION_REQUEST_CODE);
                }
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

    // Open Gallery
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE); // GALLERY_REQUEST_CODE is the request code
    }

    public EditProfileFragment() {
        // Required empty public constructor
    }

}

