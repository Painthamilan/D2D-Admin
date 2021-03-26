package com.karma.d2d_admin.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.karma.d2d_admin.activities.AdminViewProductActivity;
import com.karma.d2d_admin.activities.AllCatActivity;
import com.karma.d2d_admin.activities.OnlineCourseActivity;
import com.karma.d2d_admin.R;
import com.karma.d2d_admin.activities.RecentItemsActivity;
import com.karma.d2d_admin.activities.SearchActivity;
import com.karma.d2d_admin.domains.Top;
import com.karma.d2d_admin.utilities.Utils;
import com.squareup.picasso.Picasso;

import static com.karma.d2d_admin.utilities.Utils.RELEASE_TYPE;

public class HomeFragment extends Fragment {

    private FirebaseAuth cfAuth;
    private String curUserId;
    TextView etSearch,tvContact,tvOnlineCourses;
    private RecyclerView rvProducts,rvTopFragments,rvInstants;
    private DatabaseReference cfPostRef,topRef,instantsRef;
    private TextView ivInstant,tvMobile;
    TextView tvCats,tvRecent;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        tvCats=root.findViewById(R.id.tv_cats);
      //  rvTopFragments=root.findViewById(R.id.rv_topItems);
        tvRecent=root.findViewById(R.id.recent);
        tvContact=root.findViewById(R.id.tv_mobile);
        tvContact.setSelected(true);
        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
     //           Intent intent=new Intent(getContext(), ContactUs.class);
      //          startActivity(intent);
            }
        });

        tvRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showrecentItems(getContext());
            }
        });

        tvContact.setSelected(true);
        etSearch=root.findViewById(R.id.et_search_bar);
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        tvOnlineCourses=root.findViewById(R.id.tv_online_courses);
        tvOnlineCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), OnlineCourseActivity.class);
                startActivity(intent);
            }
        });

        tvCats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AllCatActivity.class);
                startActivity(intent);
            }
        });




        //rvTopFragments.setHasFixedSize(true);
        //LinearLayoutManager horizontalYalayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
       // horizontalYalayoutManager.setStackFromEnd(true);
        //rvTopFragments.setLayoutManager(horizontalYalayoutManager);
        //showTop();

        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("loading");
        pd.show();
// Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }
        };

        pd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 2000);

        return root;
    }

    private void showrecentItems(Context context) {
        Intent intent=new Intent(context, RecentItemsActivity.class);
        startActivity(intent);
    }


    private void showTop() {
        topRef = FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("TopItems");
        Query searchPeopleAndFriendsQuery = topRef.orderByChild("Rank");
        FirebaseRecyclerAdapter<Top, TopViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Top, TopViewHolder>(
                        Top.class,
                        R.layout.item_layout,
                        TopViewHolder.class,
                        searchPeopleAndFriendsQuery

                ) {
                    @Override
                    protected void populateViewHolder(TopViewHolder postViewHolder, final Top model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setPrice(model.getItemPrice(),model.getPercentage());
                        postViewHolder.setProductImage(model.getItemImage());
                        postViewHolder.setProductName(model.getItemName());
                        postViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               Intent intent=new Intent(getActivity(), AdminViewProductActivity.class);
                                intent.putExtra("REF_KEY",model.getItemId());
                                intent.putExtra("isOffer",false);
                                startActivity(intent);

                            }
                        });
                    }
                };
        rvTopFragments.setAdapter(firebaseRecyclerAdapter);


    }


    public static class TopViewHolder extends RecyclerView.ViewHolder {
        View cfView;
        FirebaseAuth cfAuth=FirebaseAuth.getInstance();
        String userId;
        TextView tvProductName,tvPrice;
        ImageView ivproductImage,ivDropArrow;
        DatabaseReference topRef;
        public TopViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvPrice=cfView.findViewById(R.id.price);
            tvProductName=cfView.findViewById(R.id.tv_product_name);
            tvProductName.setSelected(true);
            ivproductImage=cfView.findViewById(R.id.iv_product_image);
            if (cfAuth.getCurrentUser() != null) {
                userId=cfAuth.getCurrentUser().getUid();
            }


            topRef=FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("TopItems");



        }

        public void setPrice(String price, String percentage) {
            tvPrice.setText(Utils.getActualPrice(price,percentage) +".00");

        }

        public void setProductName(String productName) {
            tvProductName.setText(productName);
        }

        public void setProductImage(String productImage) {
            Picasso.get().load(productImage).fit().into(ivproductImage);
        }


    }

}