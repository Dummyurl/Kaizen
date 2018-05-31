package com.kaizen;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kaizen.activities.BaseActivity;
import com.kaizen.adapters.CategoryAdapter;
import com.kaizen.adapters.ChildCategoryPager;
import com.kaizen.adapters.SubcategoryAdapter;
import com.kaizen.listeners.DateTimeSetListener;
import com.kaizen.listeners.ISetOnCategoryClickListener;
import com.kaizen.listeners.ISetOnChildClickListener;
import com.kaizen.models.Banner;
import com.kaizen.models.BannerResponse;
import com.kaizen.models.Category;
import com.kaizen.models.CategoryResponse;
import com.kaizen.models.ChildCategory;
import com.kaizen.models.ListChildCategory;
import com.kaizen.models.ListChildCategoryResponse;
import com.kaizen.models.RequestResponse;
import com.kaizen.models.Subcategory;
import com.kaizen.models.SubcategoryResponse;
import com.kaizen.models.User;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.DateTimeUtil;
import com.kaizen.utils.PreferenceUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zh.wang.android.yweathergetter4a.WeatherInfo;
import zh.wang.android.yweathergetter4a.YahooWeather;
import zh.wang.android.yweathergetter4a.YahooWeatherInfoListener;

public class MainActivity extends BaseActivity implements ISetOnCategoryClickListener, ISetOnChildClickListener, YahooWeatherInfoListener, View.OnClickListener {

    public static final int RC_LOCATION = 4586;
    final static int REQUEST_LOCATION = 199;
    private ImageView iv_category;
    private RequestOptions requestOptions;
    private RetrofitService service;
    private ViewPager view_pager;
    private View ll_weather, ll_buttons;
    private TextView tv_time, tv_location, tv_temperature, tv_high_temperature, tv_low_temperature,
            tv_type, tv_tomorrow_high_temperature, tv_tomorrow_low_temperature;

    private ImageView iv_temperature, iv_tomorrow_temperature;
    private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, true);
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view_pager = findViewById(R.id.view_pager);
        TabLayout tab_layout = findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);

        iv_category = findViewById(R.id.iv_category);
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        TextView tv_date = findViewById(R.id.tv_date);
        DateFormat df = new SimpleDateFormat("EEEE, MMM dd yyyy", Locale.getDefault());
        String date = df.format(new Date().getTime());
        tv_date.setText(date);

        RecyclerView rv_category = findViewById(R.id.rv_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_category.setLayoutManager(layoutManager);
        final CategoryAdapter categoryAdapter = new CategoryAdapter(this);
        rv_category.setAdapter(categoryAdapter);

        service = RetrofitInstance.createService(RetrofitService.class);
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

        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_pager.setCurrentItem(getItem(-1), true);
            }
        });

        findViewById(R.id.iv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_pager.setCurrentItem(getItem(+1), true);
            }
        });


        ll_weather = findViewById(R.id.ll_weather);
        ll_buttons = findViewById(R.id.ll_buttons);

        tv_type = findViewById(R.id.tv_type);
        tv_time = findViewById(R.id.tv_time);
        tv_location = findViewById(R.id.tv_location);
        tv_temperature = findViewById(R.id.tv_temperature);
        tv_high_temperature = findViewById(R.id.tv_high_temperature);
        tv_low_temperature = findViewById(R.id.tv_low_temperature);
        tv_tomorrow_high_temperature = findViewById(R.id.tv_tomorrow_high_temperature);
        tv_tomorrow_low_temperature = findViewById(R.id.tv_tomorrow_low_temperature);
        iv_temperature = findViewById(R.id.iv_temperature);
        iv_tomorrow_temperature = findViewById(R.id.iv_tomorrow_temperature);

        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_time.setText(new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);

        findViewById(R.id.tv_feed_back).setOnClickListener(this);
        findViewById(R.id.tv_check_out).setOnClickListener(this);
        findViewById(R.id.tv_internet).setOnClickListener(this);
        findViewById(R.id.tv_collect_tray).setOnClickListener(this);

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            getWeather();
        }

        if (!hasGPSDevice(this)) {
            showInfoToast("Gps not Supported");
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            showInfoToast("Gps not enabled");
            enableLoc();
        }

        setupAutoPager();
    }

    private void getWeather() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(this, perms)) {
            searchByGPS();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.location_rationale), RC_LOCATION, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_LOCATION) {
            getWeather();
        }
    }

    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        if (providers == null)
            return false;
        return providers.contains(LocationManager.GPS_PROVIDER);
    }

    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {
                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(MainActivity.this, REQUEST_LOCATION);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    private void searchByGPS() {
        mYahooWeather.setNeedDownloadIcons(true);
        mYahooWeather.setUnit(YahooWeather.UNIT.CELSIUS);
        mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.GPS);
        mYahooWeather.queryYahooWeatherByGPS(getApplicationContext(), this);
    }

    private int getItem(int i) {
        return view_pager.getCurrentItem() + i;
    }

    @Override
    public void onCategoryClick(Category category) {

        service.getBanners(category.getId()).enqueue(new Callback<BannerResponse>() {
            @Override
            public void onResponse(Call<BannerResponse> call, Response<BannerResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    List<ListChildCategory> listChildCategorys = new ArrayList<>();

                    for (Banner banner : response.body().getReports()) {
                        ListChildCategory listChildCategory = new ListChildCategory();
                        listChildCategory.setBrandName(banner.getMainTitle());
                        listChildCategory.setDescription(banner.getSubTitle());
                        listChildCategory.setMainImage(banner.getBannerImg());
                        listChildCategory.setId(banner.getId());
                        listChildCategorys.add(listChildCategory);
                    }

                    ChildCategoryPager childCategoryPager = new ChildCategoryPager(getSupportFragmentManager(), listChildCategorys);
                    view_pager.setAdapter(childCategoryPager);
                } else {
                    showErrorToast(R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                showErrorToast(R.string.something_went_wrong);
            }
        });

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(APIUrls.CATEGORY_IMAGE_URL + category.getCategory_image()).into(iv_category);

        RecyclerView rv_sub_category = findViewById(R.id.rv_sub_category);

        if (category.getMainCategoryTitle().equalsIgnoreCase("home")) {
            ll_weather.setVisibility(View.VISIBLE);
            ll_buttons.setVisibility(View.VISIBLE);
            rv_sub_category.setVisibility(View.GONE);
        } else {
            ll_weather.setVisibility(View.GONE);
            ll_buttons.setVisibility(View.GONE);
            rv_sub_category.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rv_sub_category.setLayoutManager(layoutManager);
        final SubcategoryAdapter subcategoryAdapter = new SubcategoryAdapter(category, this);
        rv_sub_category.setAdapter(subcategoryAdapter);

        service.getSubCategories(category.getId()).enqueue(new Callback<SubcategoryResponse>() {
            @Override
            public void onResponse(Call<SubcategoryResponse> call, Response<SubcategoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    subcategoryAdapter.addItems(response.body().getSubcategory());
                } else {
                    showErrorToast(R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<SubcategoryResponse> call, Throwable t) {
                showErrorToast(R.string.something_went_wrong);
            }
        });
    }

    @Override
    public void onChildCategoryClick(Category category, Subcategory subcategory, final ChildCategory childCategory) {
        service.getListChildCategory(category.getId(), subcategory.getId(), childCategory.getId()).enqueue(new Callback<ListChildCategoryResponse>() {
            @Override
            public void onResponse(Call<ListChildCategoryResponse> call, Response<ListChildCategoryResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    ChildCategoryPager childCategoryPager = new ChildCategoryPager(getSupportFragmentManager(), response.body().getListchildcategory());
                    view_pager.setAdapter(childCategoryPager);
                } else {
                    showErrorToast(R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<ListChildCategoryResponse> call, Throwable t) {
                showErrorToast(R.string.something_went_wrong);
            }
        });
    }

    @Override
    public void gotWeatherInfo(WeatherInfo weatherInfo, YahooWeather.ErrorType errorType) {
        if (weatherInfo != null) {
            tv_type.setText(weatherInfo.getCurrentText());
            tv_location.setText(weatherInfo.getLocationCity());
            tv_temperature.setText(String.format("%s\u00B0 C", weatherInfo.getCurrentTemp()));
            tv_high_temperature.setText(String.format("%s\u00B0 C", weatherInfo.getForecastInfo1().getForecastTempHigh()));
            tv_low_temperature.setText(String.format("%s\u00B0 C", weatherInfo.getForecastInfo1().getForecastTempLow()));
            tv_tomorrow_high_temperature.setText(String.format("%s\u00B0 C", weatherInfo.getForecastInfo2().getForecastTempLow()));
            tv_tomorrow_low_temperature.setText(String.format("%s\u00B0 C", weatherInfo.getForecastInfo2().getForecastTempLow()));

            iv_temperature.setImageBitmap(weatherInfo.getForecastInfo1().getForecastConditionIcon());
            iv_tomorrow_temperature.setImageBitmap(weatherInfo.getForecastInfo2().getForecastConditionIcon());
        }
    }

    @Override
    public void onClick(View v) {
        final User user = PreferenceUtil.getUser(MainActivity.this);

        switch (v.getId()) {
            case R.id.tv_feed_back:
                sendFeedBack(user);
                break;
            case R.id.tv_check_out:
                checkout(user);
                break;
            case R.id.tv_internet:
                askForInternet(user);
                break;
            case R.id.tv_collect_tray:
                collectTray(user);
                break;
        }
    }

    private void sendFeedBack(final User user) {
        View feedBackView = getLayoutInflater().inflate(R.layout.dialog_feedback, null);
        final EditText et_name = feedBackView.findViewById(R.id.et_name);
        final EditText et_description = feedBackView.findViewById(R.id.et_description);

        AlertDialog.Builder feedBackBuilder = new AlertDialog.Builder(this)
                .setTitle(R.string.feedback)
                .setView(feedBackView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();
                        String description = et_description.getText().toString().trim();

                        if (name.isEmpty()) {
                            showErrorToast(R.string.enter_name);
                        } else if (description.isEmpty()) {
                            showErrorToast(R.string.enter_description);
                        } else {
                            service.sendFeedBack(user.getRoomno(), name, description).enqueue(new Callback<RequestResponse>() {
                                @Override
                                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        RequestResponse requestResponse = response.body();

                                        if (requestResponse.isResponce()) {
                                            showSuccessToast(requestResponse.getMessage());
                                            dialog.dismiss();
                                        } else {
                                            showErrorToast(requestResponse.getError());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<RequestResponse> call, Throwable t) {

                                }
                            });
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        feedBackBuilder.create().show();
    }

    private void setupAutoPager() {
        final Handler handler = new Handler();

        final Runnable update = new Runnable() {
            public void run() {

                if (view_pager != null) {
                    int currentPage = view_pager.getCurrentItem();

                    int count = 0;

                    if (view_pager.getAdapter() != null) {
                        count = view_pager.getAdapter().getCount();
                    }

                    if (currentPage == count - 1) {
                        currentPage = 0;
                    } else {
                        currentPage++;
                    }

                    view_pager.setCurrentItem(currentPage);
                }
            }
        };


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 5000, 5000);
    }

    private void checkout(final User user) {
        View checkoutView = getLayoutInflater().inflate(R.layout.dialog_checkout, null);
        final EditText et_name = checkoutView.findViewById(R.id.et_name);
        final EditText et_date_time = checkoutView.findViewById(R.id.et_date_time);
        et_date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimeUtil().datePicker(MainActivity.this, new DateTimeSetListener() {
                    @Override
                    public void onDateTimeSet(String date) {
                        et_date_time.setText(date);
                    }
                });
            }
        });

        AlertDialog.Builder checkoutBuilder = new AlertDialog.Builder(this)
                .setTitle(R.string.check_out)
                .setView(checkoutView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();
                        String dateTime = et_date_time.getText().toString().trim();

                        if (name.isEmpty()) {
                            showErrorToast(R.string.enter_name);
                        } else if (dateTime.isEmpty()) {
                            showErrorToast(R.string.enter_date_time);
                        } else {
                            service.checkoutTime(user.getRoomno(), name, dateTime).enqueue(new Callback<RequestResponse>() {
                                @Override
                                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        RequestResponse requestResponse = response.body();

                                        if (requestResponse.isResponce()) {
                                            showSuccessToast(requestResponse.getMessage());
                                            dialog.dismiss();
                                        } else {
                                            showErrorToast(requestResponse.getError());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<RequestResponse> call, Throwable t) {

                                }
                            });
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        checkoutBuilder.create().show();
    }

    private void askForInternet(final User user) {
        View internetView = getLayoutInflater().inflate(R.layout.dialog_internet, null);
        final EditText et_name = internetView.findViewById(R.id.et_name);

        AlertDialog.Builder internetBuilder = new AlertDialog.Builder(this)
                .setTitle(R.string.internet)
                .setView(internetView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();

                        if (name.isEmpty()) {
                            showErrorToast(R.string.enter_name);
                        } else {
                            service.askForInternet(user.getRoomno(), name).enqueue(new Callback<RequestResponse>() {
                                @Override
                                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        RequestResponse requestResponse = response.body();

                                        if (requestResponse.isResponce()) {
                                            showSuccessToast(requestResponse.getMessage());
                                            dialog.dismiss();
                                        } else {
                                            showErrorToast(requestResponse.getError());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<RequestResponse> call, Throwable t) {

                                }
                            });
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        internetBuilder.create().show();
    }

    private void collectTray(final User user) {
        View collectTrayView = getLayoutInflater().inflate(R.layout.dialog_collect_tray, null);
        final EditText et_name = collectTrayView.findViewById(R.id.et_name);
        final AppCompatSpinner spinner_collect_tray = collectTrayView.findViewById(R.id.spinner_collect_tray);

        AlertDialog.Builder collectTrayBuilder = new AlertDialog.Builder(this)
                .setTitle(R.string.collect_tray)
                .setView(collectTrayView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();
                        String timing = (String) spinner_collect_tray.getSelectedItem();

                        if (name.isEmpty()) {
                            showErrorToast(R.string.enter_name);
                        } else {
                            service.collectTray(user.getRoomno(), name, timing).enqueue(new Callback<RequestResponse>() {
                                @Override
                                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        RequestResponse requestResponse = response.body();

                                        if (requestResponse.isResponce()) {
                                            showSuccessToast(requestResponse.getMessage());
                                            dialog.dismiss();
                                        } else {
                                            showErrorToast(requestResponse.getError());
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<RequestResponse> call, Throwable t) {

                                }
                            });
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        collectTrayBuilder.create().show();
    }
}
