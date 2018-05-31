package com.kaizen.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.kaizen.R;
import com.kaizen.models.ListChildCategory;
import com.kaizen.models.RequestResponse;
import com.kaizen.models.User;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.PreferenceUtil;
import com.kaizen.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildCategoryFragment extends Fragment {

    public static final String CHILD_CATEGORY = "CHILD_CATEGORY";
    private ListChildCategory listChildCategory;

    public static ChildCategoryFragment newInstance(String value) {
        ChildCategoryFragment fragment = new ChildCategoryFragment();

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
        View view = inflater.inflate(R.layout.fragment_child_category, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv_title = view.findViewById(R.id.tv_title);
        TextView tv_description = view.findViewById(R.id.tv_description);
        ImageView iv_child_category = view.findViewById(R.id.iv_child_category);
        RelativeLayout rl_content = view.findViewById(R.id.rl_content);
        TextView tv_send_request = view.findViewById(R.id.tv_send_request);

        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true);

        Glide.with(this).setDefaultRequestOptions(requestOptions).load(APIUrls.CHILD_CATEGORY_IMAGE_URL + listChildCategory.getMainImage()).into(iv_child_category);

        if (listChildCategory.getDescription().trim().isEmpty()) {
            rl_content.setVisibility(View.GONE);
        } else {
            rl_content.setVisibility(View.VISIBLE);
        }

        tv_title.setText(Html.fromHtml(listChildCategory.getBrandName()));
        tv_description.setText(Html.fromHtml(listChildCategory.getDescription()));

        if (listChildCategory.getEnquiry()) {
            tv_send_request.setVisibility(View.VISIBLE);
        } else {
            tv_send_request.setVisibility(View.GONE);
        }

        tv_send_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendQuery();
            }
        });
    }

    private void sendQuery() {
        final User user = PreferenceUtil.getUser(getContext());
        View collectTrayView = getLayoutInflater().inflate(R.layout.dialog_collect_tray, null);
        final EditText et_name = collectTrayView.findViewById(R.id.et_name);
        final AppCompatSpinner spinner_collect_tray = collectTrayView.findViewById(R.id.spinner_collect_tray);

        AlertDialog.Builder collectTrayBuilder = new AlertDialog.Builder(getContext())
                .setTitle(R.string.send_request)
                .setView(collectTrayView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        String name = et_name.getText().toString().trim();
                        String timing = (String) spinner_collect_tray.getSelectedItem();

                        if (name.isEmpty()) {
                            ToastUtil.showError(getActivity(), R.string.enter_name);
                        } else {
                            RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
                            service.sendQuery(user.getRoomno(), listChildCategory.getCatId(), name, timing).enqueue(new Callback<RequestResponse>() {
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
                                    ToastUtil.showError(getActivity(), t.getMessage());
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
