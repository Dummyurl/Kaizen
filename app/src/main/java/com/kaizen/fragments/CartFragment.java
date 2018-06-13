package com.kaizen.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.kaizen.R;
import com.kaizen.adapters.CartAdapter;
import com.kaizen.models.FoodItem;
import com.kaizen.models.RequestResponse;
import com.kaizen.models.User;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.PreferenceUtil;
import com.kaizen.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartFragment extends Fragment implements CartAdapter.ICartActions {

    private TextView tv_count, tv_pay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final TextView tv_empty = view.findViewById(R.id.tv_empty);
        RecyclerView rv_cart = view.findViewById(R.id.rv_cart);
        rv_cart.setLayoutManager(new GridLayoutManager(getContext(), 4));

        final CartAdapter cartAdapter = new CartAdapter(this);
        rv_cart.setAdapter(cartAdapter);
        List<FoodItem> foodItems = FoodItem.listAll(FoodItem.class);

        if (foodItems.size() > 0) {
            cartAdapter.addItems(foodItems);
            tv_empty.setVisibility(View.GONE);
        } else {
            tv_empty.setVisibility(View.VISIBLE);
        }

        tv_pay = view.findViewById(R.id.tv_pay);
        tv_count = view.findViewById(R.id.tv_count);

        updateCount();

        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = PreferenceUtil.getUser(getContext());

                List<FoodItem> foodItems = FoodItem.listAll(FoodItem.class);

                if (foodItems.size() > 0) {
                    JSONArray passArray = new JSONArray();

                    for (FoodItem foodItem : foodItems) {
                        try {
                            JSONObject jObjP = new JSONObject();
                            jObjP.put("id", foodItem.getId());
                            jObjP.put("quantity", foodItem.getQuantity());
                            jObjP.put("productprice", foodItem.getFood_discount_price());
                            passArray.put(jObjP);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
                    service.orderItem(PreferenceUtil.getLanguage(getContext()),user.getRoomno(), passArray.toString()).enqueue(new Callback<RequestResponse>() {
                        @Override
                        public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                RequestResponse requestResponse = response.body();

                                if (requestResponse.isResponce()) {
                                    FoodItem.deleteAll(FoodItem.class);
                                    cartAdapter.resetItems();
                                    tv_empty.setVisibility(View.VISIBLE);
                                    updateCount();

                                    View thanksView = getLayoutInflater().inflate(R.layout.dialog_thanks, null);
                                    TextView tv_thanks = thanksView.findViewById(R.id.tv_thanks);
                                    tv_thanks.setText(R.string.success);

                                    TextView tv_description = thanksView.findViewById(R.id.tv_description);
                                    tv_description.setText(R.string.success_desc);

                                    final Dialog thanksDialog = new Dialog(getContext());
                                    thanksDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    thanksDialog.setContentView(thanksView);
                                    thanksView.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            thanksDialog.dismiss();
                                        }
                                    });

                                    thanksDialog.show();
                                } else {
                                    ToastUtil.showError(getActivity(), requestResponse.getError());
                                }
                            } else {
                                ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestResponse> call, Throwable t) {
                            ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                        }
                    });
                }
            }
        });
    }

    private void updateCount() {
        List<FoodItem> foodItems = FoodItem.listAll(FoodItem.class);

        long count = FoodItem.count(FoodItem.class);
        tv_count.setText(String.format("Count : %s", count));

        int total = 0;


        for (FoodItem foodItem : foodItems) {
            total = total + (Integer.valueOf(foodItem.getFood_discount_price()) * foodItem.getQuantity());
        }

        tv_pay.setText(String.format("PAY (%s)", total));
    }

    @Override
    public void onCartUpdated() {
        updateCount();
    }
}
