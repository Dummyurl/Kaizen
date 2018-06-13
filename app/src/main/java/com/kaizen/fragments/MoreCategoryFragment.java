package com.kaizen.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.kaizen.R;
import com.kaizen.activities.MainActivity;
import com.kaizen.adapters.NewsAdapter;
import com.kaizen.listeners.DateTimeSetListener;
import com.kaizen.models.ArticleResponse;
import com.kaizen.models.ListChildCategory;
import com.kaizen.models.RequestResponse;
import com.kaizen.models.User;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.DateTimeUtil;
import com.kaizen.utils.PreferenceUtil;
import com.kaizen.utils.ToastUtil;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoreCategoryFragment extends Fragment {

    public static final String CHILD_CATEGORY = "CHILD_CATEGORY";
    private ListChildCategory listChildCategory;

    public static MoreCategoryFragment newInstance(String value) {
        MoreCategoryFragment fragment = new MoreCategoryFragment();

        Bundle bundle = new Bundle();
        bundle.putString(CHILD_CATEGORY, value);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String value = getArguments().getString(CHILD_CATEGORY);

        listChildCategory = new Gson().fromJson(value, ListChildCategory.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView iv_child_category = view.findViewById(R.id.iv_child_category);
        RelativeLayout rl_content = view.findViewById(R.id.rl_content);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true);

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(APIUrls.CHILD_CATEGORY_IMAGE_URL + listChildCategory.getMainImage()).into(iv_child_category);

        rl_content.setVisibility(View.VISIBLE);

        RecyclerView rv_news = view.findViewById(R.id.rv_news);
        rv_news.setLayoutManager(new LinearLayoutManager(getContext()));
        final NewsAdapter newsAdapter = new NewsAdapter();
        rv_news.setAdapter(newsAdapter);

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(APIUrls.NEWS_URL)
                        .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        if (listChildCategory.getAliasName().equals("Saudi") || listChildCategory.getAliasName().equals("سعودي")) {
            retrofit.create(RetrofitService.class).getLocalNews().enqueue(new Callback<ArticleResponse>() {
                @Override
                public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        ArticleResponse articleResponse = response.body();

                        if (articleResponse.getStatus().equalsIgnoreCase("ok")) {
                            newsAdapter.addItems(response.body().getArticles());
                        } else {
                            if (articleResponse.getMessage() != null) {
                                ToastUtil.showError(getActivity(), articleResponse.getMessage());
                            } else {
                                ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                            }
                        }
                    } else {
                        ArticleResponse articleResponse = response.body();

                        if (articleResponse == null) {
                            ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                        } else {
                            if (articleResponse.getMessage() != null) {
                                ToastUtil.showError(getActivity(), articleResponse.getMessage());
                            } else {
                                ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArticleResponse> call, Throwable t) {
                    ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                }
            });
        } else {
            retrofit.create(RetrofitService.class).getGlobalNews().enqueue(new Callback<ArticleResponse>() {
                @Override
                public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        ArticleResponse articleResponse = response.body();

                        if (articleResponse.getStatus().equalsIgnoreCase("ok")) {
                            newsAdapter.addItems(response.body().getArticles());
                        } else {
                            if (articleResponse.getMessage() != null) {
                                ToastUtil.showError(getActivity(), articleResponse.getMessage());
                            } else {
                                ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                            }
                        }

                    } else {
                        ArticleResponse articleResponse = response.body();

                        if (articleResponse == null) {
                            ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                        } else {
                            if (articleResponse.getMessage() != null) {
                                ToastUtil.showError(getActivity(), articleResponse.getMessage());
                            } else {
                                ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ArticleResponse> call, Throwable t) {
                    ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                }
            });
        }

        if (listChildCategory.isEnableClick()) {
            iv_child_category.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    if (mainActivity != null && !mainActivity.isFinishing()) {
                        mainActivity.openChildMenu(listChildCategory.getMainCatId(), listChildCategory.getSubCatId(), listChildCategory.getCatId());
                    }
                }
            });
        } else {
            iv_child_category.setOnClickListener(null);
        }
    }
}
