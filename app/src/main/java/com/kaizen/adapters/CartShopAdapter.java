package com.kaizen.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kaizen.R;
import com.kaizen.activities.BaseActivity;
import com.kaizen.models.CustomFields;
import com.kaizen.models.FoodItem;
import com.kaizen.models.ShopItem;
import com.kaizen.models.ShopSubCategory;
import com.kaizen.reterofit.APIUrls;
import com.kaizen.reterofit.RetrofitInstance;
import com.kaizen.reterofit.RetrofitService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartShopAdapter extends CommonRecyclerAdapter<ShopItem> {

    private Context context;

    @Override
    public CartShopViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new CartShopViewHolder(view);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        CartShopViewHolder viewHolder = (CartShopViewHolder) holder;
        viewHolder.bindData(position);
    }


    public class CartShopViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_food1, tv_content1, tv_price1, tv_discount_price1;
        ImageView iv_food1;
        Spinner drop_size, drop_color;

        public CartShopViewHolder(View view) {
            super(view);
            tv_food1 = view.findViewById(R.id.tv_food1);
            tv_content1 = view.findViewById(R.id.tv_content);
            tv_price1 = view.findViewById(R.id.tv_price1);
            tv_price1.setPaintFlags(tv_price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_discount_price1 = view.findViewById(R.id.tv_discount_price1);
            iv_food1 = view.findViewById(R.id.iv_food);
            drop_size = view.findViewById(R.id.drop_size);
            drop_color = view.findViewById(R.id.drop_color);
            view.findViewById(R.id.iv_add1).setOnClickListener(this);
            view.findViewById(R.id.iv_remove1).setOnClickListener(this);


        }

        public void bindData(int position) {
            final ShopItem shopItem = getItem(position);

            tv_food1.setText(shopItem.getAliasName());
            tv_content1.setText(String.valueOf(shopItem.getQuantity()));
            tv_price1.setText(String.format("(SR %s)", shopItem.getShop_price()));
            tv_discount_price1.setText(String.format("SR %s", shopItem.getShop_discount_price()));

            // Picasso.get().load(ShopList.get(position).getBannerImg()).into(iv_food1);
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true);

            Glide.with(context).setDefaultRequestOptions(requestOptions).load(APIUrls.SHOP_IMAGE_URL + shopItem.getBannerImg()).into(iv_food1);

                final SizeSpinnerAdapter sizeSpinnerAdapter = new SizeSpinnerAdapter(context, shopItem.getCustom_fields());
                drop_size.setAdapter(sizeSpinnerAdapter);
            final ColorSpinnerAdapter colorSpinnerAdapter = new ColorSpinnerAdapter(context, shopItem.getCustom_fields());
            drop_color.setAdapter(colorSpinnerAdapter);


            drop_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    CustomFields customFields = sizeSpinnerAdapter.getItem(position);
                    CustomFields customFields1 = colorSpinnerAdapter.getItem(position);


                    if (TextUtils.isEmpty(customFields.getStrikeprice())) {
                        tv_discount_price1.setText(String.format("\u20B9 %s", customFields.getPrice_val()));
                        tv_price1.setText(String.format("(\u20B9 %s)", customFields1.getStrikeprice()));

                    } else {

                   //     tv_price1.setVisibility(View.VISIBLE);
                        tv_price1.setText(String.format("(\u20B9 %s)", customFields.getStrikeprice()));
                        tv_discount_price1.setText(String.format("\u20B9 %s", customFields.getPrice_val()));
                    }


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }

        @Override
        public void onClick(View view) {
            String value = tv_content1.getText().toString();
            final int position = getAdapterPosition();
            final int content = Integer.parseInt(value);
            final ShopItem shopItem = getItem(position);

            switch (view.getId()) {

                case R.id.iv_add1:

                    shopItem.setQuantity(1);
                    //  Toast toast= Toast.makeText(context,
                    //          "Item Added To Shop Cart", Toast.LENGTH_SHORT);
                    //  toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    //  toast.show();
                    Toast toast = Toast.makeText(context, "Item Added To ShopCart", Toast.LENGTH_SHORT);
                    View toastView = toast.getView(); // This'll return the default View of the Toast.

                    /* And now you can get the TextView of the default View of the Toast. */
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(20);
                    toastMessage.setTextColor(Color.WHITE);
                    toast.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toastMessage.setCompoundDrawablePadding(20);
                    toastView.setBackgroundColor(Color.GRAY);
                    toast.show();
                   shopItem.save();
                    break;
                case R.id.iv_remove1:
                    int decrement = content - 1;

                    if (decrement == -1) {
                        //  Toast.makeText(context, "Please click + to add item to Shopcart", Toast.LENGTH_SHORT).show();
                        Toast toast1 = Toast.makeText(context, "Please click + to add item to ShopCart", Toast.LENGTH_SHORT);
                        View toastView1 = toast1.getView(); // This'll return the default View of the Toast.

                        /* And now you can get the TextView of the default View of the Toast. */
                        TextView toastMessage1 = (TextView) toastView1.findViewById(android.R.id.message);
                        toastMessage1.setTextSize(20);
                        toastMessage1.setTextColor(Color.WHITE);
                        toastMessage1.setGravity(Gravity.CENTER);
                        toastMessage1.setCompoundDrawablePadding(20);
                        toastView1.setBackgroundColor(Color.GRAY);
                        toast1.show();
             //           shopItem.save();
                    } else {
                        tv_content1.setText(String.valueOf(decrement));
                       shopItem.setQuantity(decrement);
             //           shopItem.save();
                    }

                    break;

            }
        }
    }
}



