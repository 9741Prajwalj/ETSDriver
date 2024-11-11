package com.mlt.etsdriver.utills;

import static com.mlt.etsdriver.manager.UserStatusManager.updateStatus;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesManager {

    private static final String PREF_NAME = "user_prefs";

    // Login Data Keys
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_USER_ID = "user_id";
    private static final String KEY_API_TOKEN = "api_token";
    private static final String KEY_USER_NAME = "user_name";

    // User Profile Keys
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SharedPreferencesManager(Context context) {
        if (context != null) {
            // Initialize SharedPreferences using the context
            this.sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
        } else {
            throw new IllegalArgumentException("Context cannot be null");
        }
    }

        public boolean isLoggedIn() {
            if (sharedPreferences != null) {
                return sharedPreferences.getBoolean("isLoggedIn", false);
            }
            return false;
        }


    // Save login data
    public void saveLoginData(int userId, String username, String apiToken, boolean isLoggedIn) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USER_NAME, username);
        editor.putString(KEY_API_TOKEN, apiToken);
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.apply();
    }
    public static void saveUserData(Context context, int userId, String apiToken, String userName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_API_TOKEN, apiToken);
        editor.putString(KEY_USER_NAME, userName);
        editor.apply();
    }

    public static String getUserId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_id", null);
    }

    // Clear login data
    public void clearLoginData() {
        if (sharedPreferences != null) {
            // Set the user status to offline (0) before clearing data
            int status = 0;
            int userId = 0;
            updateStatus(userId,status);

            // Clear login-related data
            editor.remove(KEY_USER_ID);
            editor.remove(KEY_USER_NAME);
            editor.remove(KEY_API_TOKEN);
            editor.remove(KEY_IS_LOGGED_IN);
            editor.apply();

            Log.d("SharedPreferencesManager", "User logged out and data cleared");
        } else {
            Log.e("SharedPreferencesManager", "SharedPreferences is null");
        }
    }



    // Get login data
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getApiToken() {
        return sharedPreferences.getString(KEY_API_TOKEN, null);
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    // Save user profile data
    public void saveUserProfile(String username, String email, String phone, String address) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_ADDRESS, address);
        editor.apply();
    }

    // Get user profile data
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, "");
    }

    public String getPhone() {
        return sharedPreferences.getString(KEY_PHONE, "");
    }

    public String getAddress() {
        return sharedPreferences.getString(KEY_ADDRESS, "");
    }

    public void saveApiToken(String token) {
        editor.putString(KEY_API_TOKEN, token).apply();
    }

    public void saveUserId(int userId) {
        editor.putInt(KEY_USER_ID, userId).apply();
    }

    public void saveUsername(String username) {
        editor.putString(KEY_USER_NAME, username).apply();
    }
    // Clear all stored data
    public void clearAllData() {
        editor.clear();
        editor.apply();
    }
}

