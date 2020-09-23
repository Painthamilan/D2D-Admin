package com.karma.d2d_admin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.karma.d2d_admin.AddItemActivity;
import com.karma.d2d_admin.AddOffersActivity;
import com.karma.d2d_admin.AddSliderActivity;
import com.karma.d2d_admin.LoginActivity;
import com.karma.d2d_admin.R;
import com.karma.d2d_admin.ViewAllOrdersActivity;
import com.karma.d2d_admin.ViewSubCatsActivity;

public class ItemFragment extends Fragment {

    private FirebaseAuth cfAuth;
    TextView tvMyOrders,tvAddOffers,tvManageInstants,tvAddNewItems,tvAddSlider;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);

        cfAuth=FirebaseAuth.getInstance();
        tvMyOrders=root.findViewById(R.id.tv_my_orders);
        tvMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ViewAllOrdersActivity.class);
                startActivity(intent);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfAuth.signOut();
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        tvAddOffers=root.findViewById(R.id.tv_add_special);
        tvAddOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddOffersActivity.class);
                startActivity(intent);
            }
        });

        tvManageInstants=root.findViewById(R.id.tv_manage_instants);
        tvManageInstants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ViewSubCatsActivity.class);
                intent.putExtra("CAT_NAME","Instant");
                intent.putExtra("IsInstant",true);

                startActivity(intent);
            }
        });

        tvAddNewItems=root.findViewById(R.id.tv_add_new);
        tvAddNewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });

        tvAddSlider=root.findViewById(R.id.tv_add_slider);
        tvAddSlider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getContext(), AddSliderActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}