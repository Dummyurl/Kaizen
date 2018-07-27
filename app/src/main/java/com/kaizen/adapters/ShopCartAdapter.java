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
import com.kaizen.models.ShopItem;
import com.kaizen.models.User;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.kaizen.utils.PreferenceUtil;
import com.kaizen.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopCartAdapter extends CommonRecyclerAdapter<ShopItem> {
    private Context context;
    private ICartActions iCartActions;

    public ShopCartAdapter(ICartActions iCartActions) {
        this.iCartActions = iCartActions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_shop_cart, parent, false);

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
        }

        private void bindData(int position) {
            ShopItem shopItem = getItem(position);
            tv_food.setText(shopItem.getAliasName());

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true);

            Glide.with(context).setDefaultRequestOptions(requestOptions).load(APIUrls.SHOP_IMAGE_URL + shopItem.getBannerImg()).into(iv_food);

            if (TextUtils.isEmpty(shopItem.getShop_discount_price())) {
                tv_discount_price.setText(String.format("SR %s", shopItem.getShop_discount_price()));
                tv_price.setVisibility(View.GONE);
            } else {
                tv_price.setVisibility(View.VISIBLE);
                tv_price.setText(String.format("(SR %s)", shopItem.getShop_price()));
                tv_discount_price.setText(String.format("SR %s", shopItem.getShop_discount_price()));
            }

            tv_content.setText(String.valueOf(shopItem.getQuantity()));
        }

        @Override
        public void onClick(View view) {
            String value = tv_content.getText().toString();
            final int position = getAdapterPosition();
            final int content = Integer.parseInt(value);
            final ShopItem shopItem = getItem(position);

            switch (view.getId()) {
                case R.id.iv_add:

                    int increment = content + 1;
                    tv_content.setText(String.valueOf(increment));
                    shopItem.setQuantity(increment);
                    shopItem.save();
                    iCartActions.onCartUpdated();
                    break;
                case R.id.iv_remove:
                    int decrement = content - 1;

                    if (decrement == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage(R.string.remove_from_cart);
                        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                shopItem.delete();
                                removeItem(position);
                                iCartActions.onCartUpdated();
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
                        shopItem.setQuantity(decrement);
                        shopItem.save();
                        iCartActions.onCartUpdated();
                    }
                    break;
            }
        }
    }

    public interface ICartActions {
        void onCartUpdated();
    }
}
