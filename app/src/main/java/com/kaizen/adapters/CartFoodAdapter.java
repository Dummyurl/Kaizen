package com.kaizen.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import com.kaizen.reterofit.APIUrls;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartFoodAdapter extends RecyclerView.Adapter<CartFoodAdapter.MyViewHolder>
{
    TextView tv_food1, tv_content1, tv_price1, tv_discount_price1;
    ImageView iv_food1;
    List<FoodItem> foodList;
    Context context;
    private CartAdapter.ICartActions iCartActions;
    static int position1;
    public CartFoodAdapter(Context context, List<FoodItem> foodList)
    {
       this.foodList=foodList;
       this.context=context;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent,false);
        return new MyViewHolder(view) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {

         position1=position;
        tv_food1.setText(foodList.get(position).getAliasName());
        tv_content1.setText(String.valueOf(foodList.get(position).getQuantity()));
        tv_price1.setText(String.format("(SR %s)", foodList.get(position).getFood_price()));
        tv_discount_price1.setText(String.format("SR %s", foodList.get(position).getFood_discount_price()));
       // Picasso.get().load(foodList.get(position).getBannerImg()).into(iv_food1);
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true);

        Glide.with(context).setDefaultRequestOptions(requestOptions).load(APIUrls.FOOD_IMAGE_URL +foodList.get(position).getBannerImg()).into(iv_food1);

    }

    @Override
    public int getItemCount()
    {
        return foodList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public MyViewHolder(View view)
        {
            super(view);
            tv_food1=view.findViewById(R.id.tv_food1);
            tv_content1=view.findViewById(R.id.tv_content1);
            tv_price1=view.findViewById(R.id.tv_price1);
            tv_discount_price1=view.findViewById(R.id.tv_discount_price1);
            iv_food1=view.findViewById(R.id.iv_food1);
        }
    }

    @Override
    public void onClick(View view) {
        String value = tv_content1.getText().toString();
       //final int position1 = getAdapterPosition();
        final int content = Integer.parseInt(value);
        final FoodItem foodItem = getItem(position1);

        switch (view.getId()) {
            case R.id.iv_add:

                int increment = content + 1;
                tv_content1.setText(String.valueOf(increment));
                foodItem.setQuantity(increment);
                foodItem.save();
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
                            foodItem.delete();
                            removeItem(position1);
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
                    tv_content1.setText(String.valueOf(decrement));
                    foodItem.setQuantity(decrement);
                    foodItem.save();
                    iCartActions.onCartUpdated();
                }
                break;
        }

    }

    public interface ICartActions {
        void onCartUpdated();
    }
}



