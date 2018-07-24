package com.kaizen.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kaizen.R;
import com.kaizen.models.FoodItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartFoodAdapter extends RecyclerView.Adapter<CartFoodAdapter.MyViewHolder>
{
    TextView tv_food1, tv_content1, tv_price1, tv_discount_price1;
    ImageView iv_food1;
    List<FoodItem> foodList;

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
        tv_food1.setText(foodList.get(position).getAliasName());
        tv_content1.setText(String.valueOf(foodList.get(position).getQuantity()));
        tv_price1.setText(String.format("(SR %s)", foodList.get(position).getFood_price()));
        tv_discount_price1.setText(String.format("SR %s", foodList.get(position).getFood_discount_price()));
        Picasso.get().load(foodList.get(position).getBannerImg()).into(iv_food1);

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
}
