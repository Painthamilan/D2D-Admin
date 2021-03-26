package com.karma.d2d_admin.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karma.d2d_admin.activities.AddItemActivity;
import com.karma.d2d_admin.activities.AddMobilesActivity;
import com.karma.d2d_admin.activities.AddOffersActivity;
import com.karma.d2d_admin.activities.AddOnlineCoursesActivity;
import com.karma.d2d_admin.activities.AddSliderActivity;
import com.karma.d2d_admin.activities.LoginActivity;
import com.karma.d2d_admin.R;
import com.karma.d2d_admin.activities.ShareImageActivity;
import com.karma.d2d_admin.activities.ViewAllOrdersActivity;
import com.karma.d2d_admin.activities.ViewApplicationsActivity;
import com.karma.d2d_admin.utilities.Utils;

import static com.karma.d2d_admin.utilities.Utils.RELEASE_TYPE;

public class ItemFragment extends Fragment {

    private FirebaseAuth cfAuth;
    TextView tvMyOrders,tvAddOffers, tvAddCatagory,tvAddNewItems,tvAddSlider,tvAddOnlineCourses,
            tvApplications,tvShareImage,tvAddMobile;

    String catName;
    DatabaseReference catRef;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        catRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Catagories");
        cfAuth=FirebaseAuth.getInstance();


        tvAddMobile=root.findViewById(R.id.tv_add_mobiles);
        tvAddMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), AddMobilesActivity.class);
                startActivity(intent);
            }
        });
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

        tvAddOnlineCourses=root.findViewById(R.id.tv_add_online_courses);
        tvAddOnlineCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getContext(), AddOnlineCoursesActivity.class);
                startActivity(intent);
            }
        });


        tvAddCatagory =root.findViewById(R.id.tv_add_catagory);
        tvAddCatagory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ItemFragment.this.getContext(), R.style.AlertDialogTheme).setCancelable(true);
                View rowView = LayoutInflater.from(ItemFragment.this.getContext()).inflate(R.layout.layout_add_cat, null);
                dialogBuilder.setView(rowView);
                final AlertDialog dialog = dialogBuilder.create();
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }

                TextView tvDone=rowView.findViewById(R.id.tv_cat_name);
                final EditText etCatName=rowView.findViewById(R.id.et_cat_name);

                tvDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       catName=etCatName.getText().toString();
                       if (TextUtils.isEmpty(catName)){
                           Toast.makeText(ItemFragment.this.getContext(), "Please enter catagory name!", Toast.LENGTH_SHORT).show();
                       }else {
                           catRef.child(catName).child("CatagoryName").setValue(catName);
                           catRef.child(catName).child("HasSub").setValue(false);
                           dialog.dismiss();
                       }
                    }
                });
                dialog.show();
            }
        });

        tvApplications=root.findViewById(R.id.tv_view_applications);
        tvApplications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ViewApplicationsActivity.class);
                startActivity(intent);
            }
        });

        tvShareImage=root.findViewById(R.id.tv_share_image);
        tvShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), ShareImageActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}