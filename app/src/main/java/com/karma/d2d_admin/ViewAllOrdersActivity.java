package com.karma.d2d_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karma.d2d_admin.domains.Constants;
import com.karma.d2d_admin.domains.Orders;
import com.squareup.picasso.Picasso;

public class ViewAllOrdersActivity extends AppCompatActivity {

    RecyclerView rvOrders;
    DatabaseReference orderRef;
    FirebaseAuth cfAuth;
    String curUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_orders);

        cfAuth=FirebaseAuth.getInstance();
        curUserId=cfAuth.getCurrentUser().getUid();
        rvOrders=findViewById(R.id.rv_list_orders);
        orderRef= FirebaseDatabase.getInstance().getReference().child("Regions").child("Jaffna").child("Orders");
        rvOrders.setHasFixedSize(true);
        // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        // Set the layout manager to your recyclerview
        rvOrders.setLayoutManager(mLayoutManager);

        shoeAllOrders();

    }

    private void shoeAllOrders() {
        Query ordersQuery;
        curUserId=cfAuth.getCurrentUser().getUid();
        if (curUserId.equals(Constants.ADMIN_ID)){
            ordersQuery = orderRef
                    .orderByChild("Counter");
        }else {
            ordersQuery = orderRef
                    .orderByChild("Counter");
        }

        FirebaseRecyclerAdapter<Orders, OrderViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(
                        Orders.class,
                        R.layout.order_layout,
                        OrderViewHolder.class,
                        ordersQuery

                ) {
                    @Override
                    protected void populateViewHolder(OrderViewHolder postViewHolder, Orders model, int position) {
                        final String postKey = getRef(position).getKey();
                        postViewHolder.setProductName(postKey);
                       // postViewHolder.setProductImage(model.getProductImage());
                       // postViewHolder.setOrderStatus(model.getStatus());
                        postViewHolder.ivDownArrov.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatabaseReference ordersRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(postKey);
                                ordersRef.removeValue();
                                orderRef.child(postKey).removeValue();

                            }
                        });
                        postViewHolder.cfView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent=new Intent(ViewAllOrdersActivity.this,ManageOrdersActivity.class);
                                intent.putExtra("OrderId",postKey);
                                startActivity(intent);
                            }
                        });

                    }
                };

        rvOrders.setAdapter(firebaseRecyclerAdapter);
    }
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        View cfView;

        TextView tvCatName,tvUpdateImage,tvStatus;
        ImageView ivCatImage,ivDownArrov;
        FirebaseAuth cfAuth;
        String curUserId;
        DatabaseReference stateRef;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvCatName=cfView.findViewById(R.id.tv_product_name);
            ivCatImage=cfView.findViewById(R.id.iv_product_image);
            tvStatus=cfView.findViewById(R.id.tv_status);
            ivDownArrov=cfView.findViewById(R.id.iv_down_arrow);
            cfAuth=FirebaseAuth.getInstance();


            curUserId=cfAuth.getCurrentUser().getUid();

        }


        public void setProductName(String postKey) {
           // tvCatName.setText(catagoryName);
            DatabaseReference orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(postKey);
            orderRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        String name=dataSnapshot.child("ProductName").getValue().toString();
                        tvCatName.setText(name);
                        String status=dataSnapshot.child("Status").getValue().toString();
                        tvStatus.setText(status);
                        String img=dataSnapshot.child("ProductImage").getValue().toString();
                        Picasso.get().load(img).into(ivCatImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        public void setProductImage(String catagoryImage) {
            if (!TextUtils.isEmpty(catagoryImage)){
                Picasso.get().load(catagoryImage).into(ivCatImage);
            }
        }

        public void setOrderStatus(String status) {
            tvStatus.setText(status);
        }

        public void dropDownClicker(final String orderId, Context context) {

            final PopupMenu popup = new PopupMenu(context,ivDownArrov);
            popup.getMenuInflater().inflate(R.menu.menu_order_states, popup.getMenu());
            ivDownArrov.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

        private void updateState(String state, String orderId) {
            stateRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(orderId);
            stateRef.child("Status").setValue(state);
        }
    }
}
