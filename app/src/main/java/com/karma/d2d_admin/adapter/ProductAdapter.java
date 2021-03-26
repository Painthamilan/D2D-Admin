package com.karma.d2d_admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karma.d2d_admin.activities.AdminViewProductActivity;
import com.karma.d2d_admin.domains.Products;
import com.karma.d2d_admin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.karma.d2d_admin.utilities.Utils.RELEASE_TYPE;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    List<Products> mProductsList;
    Context context;

    public ProductAdapter(Context context) {
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
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.item_grid_layout,parent,false);

        return new ProductAdapter.ProductViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final ProductAdapter.ProductViewHolder holder, final int position) {

        holder.getItemDetails(mProductsList.get(position).getProductId());
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

        holder.ivDownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.manageDropdown(position);

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
        String rank,sort;
        PopupMenu popup;
        DatabaseReference topRef;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_product_name);
            tvImage=itemView.findViewById(R.id.iv_product_image);
            tvPrice=itemView.findViewById(R.id.price);
            ivDownArrow=itemView.findViewById(R.id.iv_down_arrow);

            constraintLayout=itemView.findViewById(R.id.con_layout);
            popup = new PopupMenu(context,ivDownArrow);
            popup.getMenuInflater().inflate(R.menu.top_selector, popup.getMenu());

        }
        public void getItemDetails(String productId) {
            itemRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Products").child(productId);
            itemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String name=dataSnapshot.child("ProductName").getValue().toString();
                        tvName.setText(name);
                        String image=dataSnapshot.child("ProductImage").getValue().toString();
                        Picasso.get().load(image).into(tvImage);
                        String price=dataSnapshot.child("Price").getValue().toString();
                        tvPrice.setText(price+".00");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void manageDropdown(final int position) {
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.rank_first:
                            rank="First";
                            sort="a";
                            break;
                        case R.id.rank_second:
                            rank="Second";
                            sort="b";
                            break;
                        case R.id.rank_thirt:
                            rank="Thirt";
                            sort="c";
                            break;
                        case R.id.rank_forth:
                            rank="Fourth";
                            sort="d";
                            break;
                    }

                    topRef=FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("TopItems")
                            .child(rank);
                    FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Products").child(mProductsList.get(position).getProductId())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("ItemId", mProductsList.get(position).getProductId());
                                        hashMap.put("ItemImage", dataSnapshot.child("ProductImage").getValue().toString());
                                        hashMap.put("ItemName", dataSnapshot.child("ProductName").getValue().toString());
                                        hashMap.put("ItemPrice", dataSnapshot.child("Price").getValue().toString());
                                        hashMap.put("Percentage", dataSnapshot.child("Percentage").getValue().toString());
                                        hashMap.put("Rank", sort);
                                        topRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                    return true;
                }
            });
            popup.show();
        }
    }
}
