package com.kaizen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kaizen.activities.BaseActivity;
import com.kaizen.adapters.CategoryAdapter;
import com.kaizen.listeners.ISetOnCategoryClickListener;
import com.kaizen.models.Category;
import com.kaizen.models.CategoryResponse;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements ISetOnCategoryClickListener {

    private ImageView iv_image;
    private RequestOptions requestOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iv_image = findViewById(R.id.iv_image);
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true);

        TextView tv_date = findViewById(R.id.tv_date);
        DateFormat df = new SimpleDateFormat("EEEE, MMM dd yyyy", Locale.getDefault());
        String date = df.format(new Date().getTime());
        tv_date.setText(date);

        RecyclerView rv_category = findViewById(R.id.rv_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_category.setLayoutManager(layoutManager);
        final CategoryAdapter categoryAdapter = new CategoryAdapter(this);
        rv_category.setAdapter(categoryAdapter);

        RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
        service.getCategories().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    categoryAdapter.addItems(response.body().getCategory());
                } else {
                    showErrorToast(R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                showErrorToast(R.string.something_went_wrong);
            }
        });
    }

    @Override
    public void onCategoryClick(Category category) {
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(APIUrls.IMAGE_URL + category.getCategory_image()).into(iv_image);
    }
}
