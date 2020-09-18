package com.karma.d2d_admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.karma.d2d_admin.AddItemActivity;
import com.karma.d2d_admin.AdminViewProductActivity;
import com.karma.d2d_admin.domains.Products;
import com.karma.d2d_admin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.ProductViewHolder> {


    List<Products> mProductsList;
    Context context;

    public RecentAdapter(Context context) {
        this.mProductsList = new ArrayList<>();
        this.context = context;
    }

    public void addAll(List<Products> newProducts){

        int initSize=newProducts.size();
        mProductsList.addAll(newProducts);
        notifyItemRangeChanged(initSize,newProducts.size());

    }

    public String getLastItemId(){
        return mProductsList.get(mProductsList.size()-1).getProductId();
    }


    @NonNull
    @Override
    public RecentAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.item_grid_layout,parent,false);

        return new RecentAdapter.ProductViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull RecentAdapter.ProductViewHolder holder, final int position) {

        holder.tvName.setText(mProductsList.get(position).getProductName());
        Picasso.get().load(mProductsList.get(position).getProductImage()).into(holder.tvImage);


        String price=mProductsList.get(position).getPrice();
        holder.tvPrice.setText(price+".00");
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdminViewProductActivity.class);
                intent.putExtra("REF_KEY", mProductsList.get(position).getProductId());
                intent.putExtra("isOffer", false);
                intent.putExtra("IsInstant", false);
                context.startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductsList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvPrice;
        ImageView tvImage,ivDownArrow;
        ConstraintLayout constraintLayout;
        DatabaseReference itemRef;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_product_name);
            tvImage=itemView.findViewById(R.id.iv_product_image);
            tvPrice=itemView.findViewById(R.id.price);
            ivDownArrow=itemView.findViewById(R.id.iv_down_arrow);

            constraintLayout=itemView.findViewById(R.id.con_layout);

        }


    }
}
