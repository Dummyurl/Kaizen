package com.kaizen.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kaizen.R;
import com.kaizen.activities.MainActivity;
import com.kaizen.fragments.CartFragment;
import com.kaizen.fragments.FoodCategoryFragment;
import com.kaizen.models.Category;
import com.kaizen.models.FoodItem;
import com.kaizen.reterofit.APIUrls;

public class CartFoodAdapter extends CommonRecyclerAdapter<FoodItem> {
    private MainActivity activity;

    private Context context;

    @Override
    public CartFoodViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new CartFoodViewHolder(view);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int position) {
        CartFoodViewHolder viewHolder = (CartFoodViewHolder) holder;
        viewHolder.bindData(position);
    }


    public class CartFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_food1, tv_content1, tv_price1, tv_discount_price1;
        ImageView iv_food1;

        public CartFoodViewHolder(View view) {
            super(view);
            tv_food1 = view.findViewById(R.id.tv_food1);
            tv_content1 = view.findViewById(R.id.tv_content);
            tv_price1 = view.findViewById(R.id.tv_price1);
            tv_discount_price1 = view.findViewById(R.id.tv_discount_price1);
            iv_food1 = view.findViewById(R.id.iv_food);
            view.findViewById(R.id.iv_add1).setOnClickListener(this);
            view.findViewById(R.id.iv_remove1).setOnClickListener(this);
        }

        public void bindData(int position) {
            FoodItem foodItem = getItem(position);

            tv_food1.setText(foodItem.getAliasName());
            tv_content1.setText(String.valueOf(foodItem.getQuantity()));
            tv_price1.setText(String.format("(SR %s)", foodItem.getFood_price()));
            tv_price1.setPaintFlags(tv_price1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tv_discount_price1.setText(String.format("SR %s", foodItem.getFood_discount_price()));
            // Picasso.get().load(foodList.get(position).getBannerImg()).into(iv_food1);
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_place_holder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true);

            Glide.with(context).setDefaultRequestOptions(requestOptions).load(APIUrls.FOOD_IMAGE_URL + foodItem.getBannerImg()).into(iv_food1);

        }

        @SuppressLint("ResourceType")
        @Override
        public void onClick(View view) {
            String value = tv_content1.getText().toString();
            final int position = getAdapterPosition();
            final int content = Integer.parseInt(value);
            final FoodItem foodItem = getItem(position);

            switch (view.getId()) {

                case R.id.iv_add1:
                    int increment = content + 1;
                    tv_content1.setText(String.valueOf(increment));
 //                   foodItem.setQuantity(increment);
                  // Toast toast= Toast.makeText(context,
                   //         "Item Added To Food Cart", Toast.LENGTH_SHORT);
                   // toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                   // toast.show();
                    Toast toast = Toast.makeText(context, "Item Added To FoodCart", Toast.LENGTH_SHORT);
                    View toastView = toast.getView(); // This'll return the default View of the Toast.

                    /* And now you can get the TextView of the default View of the Toast. */
                    TextView toastMessage = (TextView) toastView.findViewById(android.R.id.message);
                    toastMessage.setTextSize(20);
                    toastMessage.setTextColor(Color.WHITE);
                    toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toastMessage.setCompoundDrawablePadding(20);
                    toastView.setBackgroundColor(Color.GRAY);
                    toast.show();
  //                  foodItem.save();
                    break;
                case R.id.iv_remove1:
                    int decrement = content - 1;

                    if (decrement == -1) {
                       // Toast.makeText(context, "Please click + to add item to Shopcart", Toast.LENGTH_SHORT).show();
                        Toast toast1 = Toast.makeText(context, "Please click + to add item to FoodCart", Toast.LENGTH_SHORT);
                        View toastView1 = toast1.getView(); // This'll return the default View of the Toast.

                        /* And now you can get the TextView of the default View of the Toast. */
                        TextView toastMessage1 = (TextView) toastView1.findViewById(android.R.id.message);
                        toastMessage1.setTextSize(20);
                        toastMessage1.setTextColor(Color.WHITE);
                        toastMessage1.setGravity(Gravity.CENTER);
                        toastMessage1.setCompoundDrawablePadding(20);
                        toastView1.setBackgroundColor(Color.GRAY);
                        toast1.show();
 //                       foodItem.save();
                    } else {
                        tv_content1.setText(String.valueOf(decrement));
                        foodItem.setQuantity(decrement);
 //                       foodItem.save();
                    }

                    break;
                case  R.id.btn_review:


            }


            }
        }
    }




