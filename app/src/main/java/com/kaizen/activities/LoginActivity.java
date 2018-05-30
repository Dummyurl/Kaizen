package com.kaizen.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.kaizen.MainActivity;
import com.kaizen.R;
import com.kaizen.models.UserResponse;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.PreferenceUtil;
import com.kaizen.utils.ToastUtil;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    public static final int RC_LOCATION = 4586;
    private RetrofitService service;
    private EditText et_room_no, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_room_no = findViewById(R.id.et_room_no);
        et_password = findViewById(R.id.et_password);
        service = RetrofitInstance.createService(RetrofitService.class);
        AppCompatButton btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_LOCATION)
    private void login() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            String roomNo = et_room_no.getText().toString().trim();
            String password = et_password.getText().toString().trim();

            if (roomNo.isEmpty()) {
                showErrorToast(R.string.room_number_empty);
            } else if (password.isEmpty()) {
                showErrorToast(R.string.password_empty);
            } else {
                service.login(roomNo, password).enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            UserResponse userResponse = response.body();

                            if (userResponse.getData() != null) {
                                PreferenceUtil.setUser(LoginActivity.this, userResponse.getData());
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            } else {
                                showErrorToast(userResponse.getError());
                            }
                        } else {
                            showErrorToast(R.string.something_went_wrong);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        showErrorToast(R.string.something_went_wrong);
                    }
                });
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale), RC_LOCATION, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
