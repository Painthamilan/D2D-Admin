package com.karma.d2d_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karma.d2d_admin.domains.Applications;
import com.karma.d2d_admin.domains.Catagories;
import com.squareup.picasso.Picasso;

public class ViewApplicationsActivity extends AppCompatActivity {

    RecyclerView rvApplications;
    DatabaseReference appRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_applications);

        rvApplications=findViewById(R.id.rv_list_applications);



        appRef= FirebaseDatabase.getInstance().getReference().child("Applications");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(ViewApplicationsActivity.this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvApplications.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(rvApplications.getContext(),mLayoutManager.getOrientation());
        rvApplications.addItemDecoration(dividerItemDecoration);

        Query query=appRef.orderByKey();

        FirebaseRecyclerAdapter<Applications, AppsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Applications, AppsViewHolder>(
                        Applications.class,
                        R.layout.layout_applications,
                        AppsViewHolder.class,
                        appRef

                ) {
                    @Override
                    protected void populateViewHolder(AppsViewHolder postViewHolder,  Applications model, int position) {
                        final String postKey = getRef(position).getKey();
                        postViewHolder.setCourseName(model.getCourseName());
                        postViewHolder.setFullName(model.getFullName());
                       // postViewHolder.setCourseImage(model.getCourseName());

                        postViewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent=new Intent(ViewApplicationsActivity.this,ViewSingleApplicationActivity.class);
                                intent.putExtra("REF_KEY",postKey);
                                startActivity(intent);
                            }
                        });
                    }
                };

        rvApplications.setAdapter(firebaseRecyclerAdapter);

    }

    public static class AppsViewHolder extends RecyclerView.ViewHolder {
        View cfView;

        TextView tvCourseName,tvFullName;
        ImageView ivCourseImage;
        ConstraintLayout constraintLayout;
        public AppsViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvCourseName=cfView.findViewById(R.id.tv_course_name);
            tvFullName=cfView.findViewById(R.id.tv_full_name);
            ivCourseImage=cfView.findViewById(R.id.iv_course_image);
            constraintLayout=cfView.findViewById(R.id.con_layout);
        }


        public void setCourseName(String catagoryName) {
            tvCourseName.setText(catagoryName);

        }


        public void setFullName(String fullName) {
            tvFullName.setText(fullName);
        }

        public void setCourseImage(String courseName) {
           Query query= FirebaseDatabase.getInstance().getReference().child("Online Courses")
                   .orderByChild("CourseName")
                   .equalTo(courseName);
           query.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()){
                       String img=dataSnapshot.child("CourseImage").getValue().toString();
                       Picasso.get().load(img).into(ivCourseImage);
                   }
               }

               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) {

               }
           });

        }
    }
}
