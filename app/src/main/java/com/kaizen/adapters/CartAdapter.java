package com.kaizen.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kaizen.R;
import com.kaizen.models.FoodItem;
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

public class CartAdapter extends CommonRecyclerAdapter<FoodItem> {
    private Context context;

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        CartViewHolder cartViewHolder = (CartViewHolder) holder;
        cartViewHolder.bindData(position);
    }

    private class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_food, tv_content, tv_price, tv_discount_price;
        ImageView iv_food;

        private CartViewHolder(View view) {
            super(view);
            tv_food = view.findViewById(R.id.tv_food);
            iv_food = view.findViewById(R.id.iv_food);
            tv_content = view.findViewById(R.id.tv_content);
            tv_discount_price = view.findViewById(R.id.tv_discount_price);
            tv_price = view.findViewById(R.id.tv_price);
            tv_price.setPaintFlags(tv_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            view.findViewById(R.id.iv_add).setOnClickListener(this);
            view.findViewById(R.id.iv_remove).setOnClickListener(this);
            view.findViewById(R.id.btn_order).setOnClickListener(this);
        }

        private void bindData(int position) {
            FoodItem foodItem = getItem(position);
            tv_food.setText(foodItem.getAliasName());

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true);

            Glide.with(context).setDefaultRequestOptions(requestOptions).load(APIUrls.FOOD_IMAGE_URL + foodItem.getBannerImg()).into(iv_food);

            if (TextUtils.isEmpty(foodItem.getFood_discount_price())) {
                tv_discount_price.setText(String.format("\u20B9 %s", foodItem.getFood_discount_price()));
                tv_price.setVisibility(View.GONE);
            } else {
                tv_price.setVisibility(View.VISIBLE);
                tv_price.setText(String.format("(\u20B9 %s)", foodItem.getFood_price()));
                tv_discount_price.setText(String.format("\u20B9 %s", foodItem.getFood_discount_price()));
            }

        }

        @Override
        public void onClick(View view) {
            String value = tv_content.getText().toString();

            final int content = Integer.parseInt(value);

            switch (view.getId()) {
                case R.id.iv_add:

                    int increment = content + 1;
                    tv_content.setText(String.valueOf(increment));

                    break;
                case R.id.iv_remove:
                    int decrement = content - 1;

                    if (decrement == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(R.string.remove_from_cart);
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FoodItem foodItem = getItem(getAdapterPosition());
                                foodItem.delete();
                                dialog.dismiss();
                            }
                        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    } else {
                        tv_content.setText(String.valueOf(decrement));
                    }

                    break;
                case R.id.btn_order:
                    final User user = PreferenceUtil.getUser(context);
                    FoodItem foodItem = getItem(getAdapterPosition());

                    RetrofitService service = RetrofitInstance.createService(RetrofitService.class);
                    service.orderItem(user.getRoomno(), String.valueOf(foodItem.getId()), value).enqueue(new Callback<RequestResponse>() {
                        @Override
                        public void onResponse(Call<RequestResponse> call, Response<RequestResponse> response) {
                            if (response.body() != null && response.isSuccessful()) {
                                RequestResponse requestResponse = response.body();

                                if (requestResponse.isResponce()) {
                                    FoodItem foodItem = getItem(getAdapterPosition());
                                    foodItem.delete();
                                    ToastUtil.showSuccess((Activity) context, requestResponse.getMessage());
                                } else {
                                    ToastUtil.showError((Activity) context, requestResponse.getError());
                                }
                            } else {
                                ToastUtil.showError((Activity) context, R.string.something_went_wrong);
                            }
                        }

                        @Override
                        public void onFailure(Call<RequestResponse> call, Throwable t) {
                            ToastUtil.showError((Activity) context, R.string.something_went_wrong);
                        }
                    });
                    break;

            }
        }
    }
}
