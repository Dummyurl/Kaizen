package com.kaizen.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import com.kaizen.MainActivity;
import com.kaizen.R;
import com.kaizen.models.UserResponse;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.PreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText et_room_no = findViewById(R.id.et_room_no);
        final EditText et_password = findViewById(R.id.et_password);
        final AppCompatButton btn_login = findViewById(R.id.btn_login);
        final RetrofitService service = RetrofitInstance.createService(RetrofitService.class);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                PreferenceUtil.setUser(LoginActivity.this, userResponse.getData());
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
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
            }
        });
    }
}
