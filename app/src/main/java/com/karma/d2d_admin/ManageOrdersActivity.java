package com.karma.d2d_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karma.d2d_admin.utilities.Utils;
import com.squareup.picasso.Picasso;

public class ManageOrdersActivity extends AppCompatActivity {

    ImageView ivProductImage;
    TextView tvProductName,tvPhoneNumber,tvAddress,tvPrice,tvSelect,tvQuantity,tvCustomerName;
    DatabaseReference orderRef;
    String orderId,state,email,userId,productImage,customerName;
    DatabaseReference userRef;
    long countPosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);
        ivProductImage=findViewById(R.id.iv_product_image);
        tvProductName=findViewById(R.id.tv_product_name);
        tvPhoneNumber=findViewById(R.id.tv_phone_number);
        tvAddress=findViewById(R.id.tv_address);
        tvPrice=findViewById(R.id.tv_price);
        tvSelect=findViewById(R.id.tv_select);
        tvQuantity=findViewById(R.id.tv_quantity);
        tvCustomerName=findViewById(R.id.tv_customer_name);
        orderId=getIntent().getStringExtra("OrderId");

        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(orderId);
        userRef=FirebaseDatabase.getInstance().getReference().child("User");

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    productImage=dataSnapshot.child("ProductImage").getValue().toString();
                    Picasso.get().load(productImage).into(ivProductImage);
                    String productName=dataSnapshot.child("ProductName").getValue().toString();
                    tvProductName.setText(productName);
                    String price=dataSnapshot.child("Price").getValue().toString();
                    tvPrice.setText(price);
                    String phone=dataSnapshot.child("PhoneNumber").getValue().toString();
                    tvPhoneNumber.setText(phone);
                    String address=dataSnapshot.child("Address").getValue().toString();
                    tvAddress.setText(address);
                    String quantity=dataSnapshot.child("Quantity").getValue().toString();
                    tvQuantity.setText(quantity);
                    email=dataSnapshot.child("Email").getValue().toString();
                    userId=dataSnapshot.child("UserId").getValue().toString();
                    customerName=dataSnapshot.child("CustomerName").getValue().toString();
                    tvCustomerName.setText(customerName);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final PopupMenu popup = new PopupMenu(this,tvSelect);
        popup.getMenuInflater().inflate(R.menu.menu_order_states, popup.getMenu());
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.state_confirm_order:
                                state = "Confirmed";
                                updateState("Confirmed", orderId);
                                showPopup(state);
                                break;
                            case R.id.state_pack_order:
                                state = "Packed";
                                updateState("Packed", orderId);
                                showPopup(state);
                                break;
                            case R.id.state_carry_order:
                                state = "On the way";
                                updateState("On the way", orderId);
                                showPopup(state);
                                break;
                            case R.id.state_deliver_order:
                                state = "Delivered";
                                updateState("Delivered", orderId);
                                showPopup(state);
                                break;
                            case R.id.state_return_order:
                                state = "Returned";
                                updateState("Returned", orderId);
                                showPopup(state);
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

    }

    private void showPopup(String state) {
        switch (state) {
            case "Confirmed":
                sendMessage("Order Confirmed","Your oeder is confirmed. You can see more details by D2D App ==> Profile ==> My Orders.  \n" +
                        "Thank you foe using D2D");
                break;
            case "Packed":
                sendMessage("Order Packed","Your oeder is packed. You can see more details by D2D App ==> Profile ==> My Orders.  \n" +
                        "Thank you foe using D2D");
                break;
            case "On the way":
                sendMessage("Order on the way","Your oeder is on the way. You can see more details by D2D App ==> Profile ==> My Orders.  \n" +
                        "Thank you foe using D2D");
                break;
            case "Delivered":
                sendMessage("Order Delivered","Your oeder is delivered. You can see more details by D2D App ==> Profile ==> My Orders.  \n" +
                        "Thank you foe using D2D");
                break;
            case "Returned":
                sendMessage("Return accepted","Your oeder is returned. You can see more details by D2D App ==> Profile ==> My Orders.  \n" +
                        "Thank you foe yousing D2D");
                break;
        }
         /*Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"painthamilan29@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ViewProductActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }*/

    }

    private void updateState(String state, String orderId) {
        orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(orderId);
        orderRef.child("Status").setValue(state);
    }
    private void sendMessage(final String title, final String message) {
        final String id= Utils.createRandomId();
        userRef.child(userId).child("Notifications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    countPosts = dataSnapshot.getChildrenCount();
                } else {
                    countPosts = 0;
                }
                userRef.child(userId).child("Notifications").child(id).child("Message").setValue(message);
                userRef.child(userId).child("Notifications").child(id).child("Title").setValue(title);
                userRef.child(userId).child("Notifications").child(id).child("NotificationImage").setValue(productImage);
                userRef.child(userId).child("Notifications").child(id).child("Counter").setValue(countPosts);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
        i.putExtra(Intent.EXTRA_SUBJECT, title);
        i.putExtra(Intent.EXTRA_TEXT   , message);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ManageOrdersActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

}
