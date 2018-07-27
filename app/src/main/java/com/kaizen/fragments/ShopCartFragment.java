package com.kaizen.fragments;

import android.annotation.SuppressLint;
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
import com.kaizen.adapters.ShopCartAdapter;
import com.kaizen.models.FoodItem;
import com.kaizen.models.RequestResponse;
import com.kaizen.models.ShopItem;
import com.kaizen.models.ShopOrder;
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

public class ShopCartFragment extends Fragment implements ShopCartAdapter.ICartActions {

    private TextView tv_count, tv_pay, tv_empty,tv_total;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_empty = view.findViewById(R.id.tv_empty);


        RecyclerView rv_cart = view.findViewById(R.id.rv_cart);
        rv_cart.setLayoutManager(new GridLayoutManager(getContext(), 4));

        final ShopCartAdapter shopcartAdapter = new ShopCartAdapter(this);
        rv_cart.setAdapter(shopcartAdapter);
        List<ShopItem> shopItems = ShopItem.listAll(ShopItem.class);

        if (shopItems.size() > 0) {
            shopcartAdapter.addItems(shopItems);
            tv_empty.setVisibility(View.GONE);
        } else {
            tv_empty.setVisibility(View.VISIBLE);
        }


        tv_pay = view.findViewById(R.id.tv_pay);
        tv_count = view.findViewById(R.id.tv_count);
        tv_total = view.findViewById(R.id.tv_total);

        updateCount();

        tv_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = PreferenceUtil.getUser(getContext());

                List<ShopItem> shopItems = ShopItem.listAll(ShopItem.class);

                if (shopItems.size() > 0) {
                    JSONArray passArray = new JSONArray();

                    for (ShopItem shopItem : shopItems) {
                        try {
                            JSONObject jObjP = new JSONObject();
                            jObjP.put("id", shopItem.getId());
                            jObjP.put("quantity", shopItem.getQuantity());
                            jObjP.put("productprice", shopItem.getShop_discount_price());
                            passArray.put(jObjP);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
                    service.shoporderItem(PreferenceUtil.getLanguage(getContext()), user.getRoomno(), passArray.toString()).enqueue(new Callback<ShopOrder>() {
                        @Override
                        public void onResponse(Call<ShopOrder> call, Response<ShopOrder> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                ShopOrder shopOrder = response.body();

                                if (shopOrder.isResponce()) {
                                    ShopItem.deleteAll(ShopItem.class);
                                    shopcartAdapter.resetItems();
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
                                    ToastUtil.showError(getActivity(), shopOrder.getError());
                                }
                            } else {
                                ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                            }
                        }

                        @Override
                        public void onFailure(Call<ShopOrder> call, Throwable t) {
                            ToastUtil.showError(getActivity(), R.string.something_went_wrong);
                        }
                    });
                }
            }
        });
    }

    @SuppressLint("StringFormatInvalid")
    private void updateCount() {
        List<ShopItem> shopItems = ShopItem.listAll(ShopItem.class);

        if (shopItems.size() == 0) {
            tv_empty.setVisibility(View.VISIBLE);
        }

        long count = ShopItem.count(ShopItem.class);
        int total = 0;


        for (ShopItem shopItem : shopItems) {
            total = total + (Integer.valueOf(shopItem.getShop_discount_price()) * shopItem.getQuantity());
        }

        int language = PreferenceUtil.getLanguage(getContext());

        if (language == 1) {
            tv_empty.setText(R.string.no_items_available);
            tv_count.setText(String.format(getString(R.string.count), String.valueOf(count)));
            tv_total.setText(String.format(getString(R.string.total), String.valueOf(total)));
            tv_pay.setText(String.format(getString(R.string.pay), String.valueOf(total)));
        } else {
            tv_empty.setText(R.string.no_items_available_arabic);
            tv_count.setText(String.format(getString(R.string.count_arabic), String.valueOf(count)));
            tv_pay.setText(String.format(getString(R.string.pay_arabic), String.valueOf(total)));
        }
    }

    @Override
    public void onCartUpdated() {
        updateCount();
    }
}
