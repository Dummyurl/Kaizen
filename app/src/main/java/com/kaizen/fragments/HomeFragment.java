package com.kaizen.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.kaizen.R;
import com.kaizen.adapters.ChildCategoryPager;
import com.kaizen.listeners.DateTimeSetListener;
import com.kaizen.models.Banner;
import com.kaizen.models.BannerResponse;
import com.kaizen.models.Category;
import com.kaizen.models.ChildCategory;
import com.kaizen.models.ListChildCategory;
import com.kaizen.models.ListChildCategoryResponse;
import com.kaizen.models.RequestResponse;
import com.kaizen.models.Subcategory;
import com.kaizen.models.User;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.DateTimeUtil;
import com.kaizen.utils.PreferenceUtil;
import com.kaizen.utils.ToastUtil;

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

public class HomeFragment extends Fragment implements YahooWeatherInfoListener, View.OnClickListener {

    public final static int REQUEST_LOCATION = 199;
    public static final int RC_LOCATION = 4586;
    private static final String CATEGORY = "CATEGORY";
    private TextView tv_time, tv_location, tv_temperature, tv_high_temperature, tv_low_temperature,
            tv_type, tv_tomorrow_high_temperature, tv_tomorrow_low_temperature;

    private ImageView iv_temperature, iv_tomorrow_temperature;
    private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, true);
    private ImageView iv_category;
    private RequestOptions requestOptions;
    private ViewPager view_pager;
    private GoogleApiClient googleApiClient;
    private RetrofitService service;
    private Category category;

    public static HomeFragment newInstance(Category category) {
        HomeFragment homeFragment = new HomeFragment();

        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, new Gson().toJson(category));
        homeFragment.setArguments(bundle);

        return homeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String value = getArguments().getString(CATEGORY);
        category = new Gson().fromJson(value, Category.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view_pager = view.findViewById(R.id.view_pager);
        TabLayout tab_layout = view.findViewById(R.id.tab_layout);
        tab_layout.setupWithViewPager(view_pager);

        iv_category = view.findViewById(R.id.iv_category);
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        tv_type = view.findViewById(R.id.tv_type);
        tv_time = view.findViewById(R.id.tv_time);
        tv_location = view.findViewById(R.id.tv_location);
        tv_temperature = view.findViewById(R.id.tv_temperature);
        tv_high_temperature = view.findViewById(R.id.tv_high_temperature);
        tv_low_temperature = view.findViewById(R.id.tv_low_temperature);
        tv_tomorrow_high_temperature = view.findViewById(R.id.tv_tomorrow_high_temperature);
        tv_tomorrow_low_temperature = view.findViewById(R.id.tv_tomorrow_low_temperature);
        iv_temperature = view.findViewById(R.id.iv_temperature);
        iv_tomorrow_temperature = view.findViewById(R.id.iv_tomorrow_temperature);

        final Handler someHandler = new Handler(getActivity().getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_time.setText(new SimpleDateFormat("hh:mm aa", Locale.getDefault()).format(new Date()));
                someHandler.postDelayed(this, 1000);
            }
        }, 10);

        view.findViewById(R.id.tv_feed_back).setOnClickListener(this);
        view.findViewById(R.id.tv_check_out).setOnClickListener(this);
        view.findViewById(R.id.tv_internet).setOnClickListener(this);
        view.findViewById(R.id.tv_collect_tray).setOnClickListener(this);


        view.findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_pager.setCurrentItem(getItem(-1), true);
            }
        });

        view.findViewById(R.id.iv_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_pager.setCurrentItem(getItem(+1), true);
            }
        });

        final LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getContext())) {
            getWeather();
        }

        if (!hasGPSDevice(getContext())) {
            ToastUtil.showError(getActivity(), "Gps not Supported");
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(getContext())) {
            ToastUtil.showError(getActivity(), "Gps not enabled");
            enableLoc();
        }

        service = RetrofitInstance.createService(RetrofitService.class);
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
                        listChildCategory.setEnquiry(banner.isEnquiry());
                        listChildCategory.setEnableClick(true);
                        listChildCategory.setCatId(banner.getCatId());
                        listChildCategory.setMainCatId(banner.getMainCatId());
                        listChildCategory.setSubCatId(banner.getSubCatId());
                        listChildCategorys.add(listChildCategory);
                    }

                    if (getActivity() != null && !getActivity().isFinishing()) {
                        ChildCategoryPager childCategoryPager = new ChildCategoryPager(getChildFragmentManager(), listChildCategorys);
                        view_pager.setAdapter(childCategoryPager);
                    }
                } else {
                    ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                }
            }

            @Override
            public void onFailure(Call<BannerResponse> call, Throwable t) {
                ToastUtil.showError(getActivity(), R.string.something_went_wrong);
            }
        });

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(APIUrls.CATEGORY_IMAGE_URL + category.getCategory_image()).into(iv_category);

        setupAutoPager();
    }

    public void searchByGPS() {
        mYahooWeather.setNeedDownloadIcons(true);
        mYahooWeather.setUnit(YahooWeather.UNIT.CELSIUS);
        mYahooWeather.setSearchMode(YahooWeather.SEARCH_MODE.GPS);
        mYahooWeather.queryYahooWeatherByGPS(getContext(), this);
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
        final User user = PreferenceUtil.getUser(getContext());

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

    private void getWeather() {
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            searchByGPS();
        } else {
            EasyPermissions.requestPermissions(getActivity(), getString(R.string.location_rationale), RC_LOCATION, perms);
        }
    }

    private void sendFeedBack(final User user) {
        View feedBackView = getLayoutInflater().inflate(R.layout.dialog_feedback, null);
        final EditText et_name = feedBackView.findViewById(R.id.et_name);
        final EditText et_description = feedBackView.findViewById(R.id.et_description);

        AlertDialog.Builder feedBackBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.feedback)
                .setView(feedBackView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();
                        String description = et_description.getText().toString().trim();

                        if (name.isEmpty()) {
                            ToastUtil.showError(getActivity(), R.string.enter_name);
                        } else if (description.isEmpty()) {
                            ToastUtil.showError(getActivity(), R.string.enter_description);
                        } else {
                            service.sendFeedBack(user.getRoomno(), name, description).enqueue(new Callback<RequestResponse>() {
                                @Override
                                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        RequestResponse requestResponse = response.body();

                                        if (requestResponse.isResponce()) {
                                            ToastUtil.showSuccess(getActivity(), requestResponse.getMessage());
                                            dialog.dismiss();
                                        } else {
                                            ToastUtil.showError(getActivity(), requestResponse.getError());
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

    private void checkout(final User user) {
        View checkoutView = getLayoutInflater().inflate(R.layout.dialog_checkout, null);
        final EditText et_name = checkoutView.findViewById(R.id.et_name);
        final EditText et_date_time = checkoutView.findViewById(R.id.et_date_time);
        et_date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DateTimeUtil().datePicker(getContext(), new DateTimeSetListener() {
                    @Override
                    public void onDateTimeSet(String date) {
                        et_date_time.setText(date);
                    }
                });
            }
        });

        AlertDialog.Builder checkoutBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.check_out)
                .setView(checkoutView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();
                        String dateTime = et_date_time.getText().toString().trim();

                        if (name.isEmpty()) {
                            ToastUtil.showError(getActivity(), R.string.enter_name);
                        } else if (dateTime.isEmpty()) {
                            ToastUtil.showError(getActivity(), R.string.enter_date_time);
                        } else {
                            service.checkoutTime(user.getRoomno(), name, dateTime).enqueue(new Callback<RequestResponse>() {
                                @Override
                                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        RequestResponse requestResponse = response.body();

                                        if (requestResponse.isResponce()) {
                                            ToastUtil.showSuccess(getActivity(), requestResponse.getMessage());
                                            dialog.dismiss();
                                        } else {
                                            ToastUtil.showError(getActivity(), requestResponse.getError());
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

        AlertDialog.Builder internetBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.internet)
                .setView(internetView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();

                        if (name.isEmpty()) {
                            ToastUtil.showError(getActivity(), R.string.enter_name);
                        } else {
                            service.askForInternet(user.getRoomno(), name).enqueue(new Callback<RequestResponse>() {
                                @Override
                                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        RequestResponse requestResponse = response.body();

                                        if (requestResponse.isResponce()) {
                                            ToastUtil.showSuccess(getActivity(), requestResponse.getMessage());
                                            dialog.dismiss();
                                        } else {
                                            ToastUtil.showError(getActivity(), requestResponse.getError());
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

        AlertDialog.Builder collectTrayBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.collect_tray)
                .setView(collectTrayView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();
                        String timing = (String) spinner_collect_tray.getSelectedItem();

                        if (name.isEmpty()) {
                            ToastUtil.showError(getActivity(), R.string.enter_name);
                        } else {
                            service.collectTray(user.getRoomno(), name, timing).enqueue(new Callback<RequestResponse>() {
                                @Override
                                public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        RequestResponse requestResponse = response.body();

                                        if (requestResponse.isResponce()) {
                                            ToastUtil.showSuccess(getActivity(), requestResponse.getMessage());
                                            dialog.dismiss();
                                        } else {
                                            ToastUtil.showError(getActivity(), requestResponse.getError());
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
            googleApiClient = new GoogleApiClient.Builder(getContext())
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
                                status.startResolutionForResult(getActivity(), REQUEST_LOCATION);

                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    private int getItem(int i) {
        return view_pager.getCurrentItem() + i;
    }
}