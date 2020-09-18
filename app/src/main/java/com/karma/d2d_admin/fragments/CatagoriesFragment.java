package com.karma.d2d_admin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karma.d2d_admin.CatsDetailActivity;
import com.karma.d2d_admin.R;
import com.karma.d2d_admin.domains.Catagories;
import com.squareup.picasso.Picasso;

public class CatagoriesFragment extends Fragment {

    private FirebaseAuth cfAuth;
    private String curUserId;
    private RecyclerView rvCats;
    private DatabaseReference cfPostRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_notifications, container, false);
        rvCats=root.findViewById(R.id.rv_list_cats);
        rvCats.setHasFixedSize(true);
        // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        // Set the layout manager to your recyclerview
        rvCats.setLayoutManager(mLayoutManager);
        showAllCats();

        return root;
    }

    private void showAllCats() {
        cfPostRef = FirebaseDatabase.getInstance().getReference().child("Catagories");
        FirebaseRecyclerAdapter<Catagories, CatsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Catagories, CatsViewHolder>(
                        Catagories.class,
                        R.layout.list_cats_layout,
                        CatsViewHolder.class,
                        cfPostRef

                ) {
                    @Override
                    protected void populateViewHolder(CatsViewHolder postViewHolder, final Catagories model, int position) {
                        final String postKey = getRef(position).getKey();
                        postViewHolder.setCatagoryName(model.getCatagoryName());
                        postViewHolder.setCatagoryImage(model.getCatagoryImage());

                        postViewHolder.tvUpdateImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(), CatsDetailActivity.class);
                                intent.putExtra("REF_KEY",postKey);
                                intent.putExtra("CAT_NAME",model.getCatagoryName());
                                intent.putExtra("CAT_IMAGE",model.getCatagoryImage());
                                intent.putExtra("isSub",false);
                                startActivity(intent);
                            }
                        });
                    }
                };

        rvCats.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CatsViewHolder extends RecyclerView.ViewHolder {
        View cfView;

        TextView tvCatName,tvUpdateImage;
        ImageView ivCatImage;
        public CatsViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvCatName=cfView.findViewById(R.id.tv_cat_name);
            ivCatImage=cfView.findViewById(R.id.iv_cats_image);
            tvUpdateImage=cfView.findViewById(R.id.tv_update_image);
        }


        public void setCatagoryName(String catagoryName) {
            tvCatName.setText(catagoryName);

        }

        public void setCatagoryImage(String catagoryImage) {
            if (!TextUtils.isEmpty(catagoryImage)){
                Picasso.get().load(catagoryImage).into(ivCatImage);
            }
        }
    }


}