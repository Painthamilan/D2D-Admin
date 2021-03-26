package com.karma.d2d_admin.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karma.d2d_admin.R;
import com.karma.d2d_admin.utilities.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static com.karma.d2d_admin.utilities.Utils.RELEASE_TYPE;

public class CourseDetailsActivity extends AppCompatActivity {

    EditText tvCourseName, tvRequirements, tvDuration, tvFee, tvTeacher, tvDetails;
    TextView tvApply;
    ImageView ivCourseImage;
    DatabaseReference courseRef;
    String courseId,courseName,requirements,duration,fee,teacher,details,courseImage;
    ProgressDialog progressdialog;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        Utils.setTopBar(getWindow(),getResources());

        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);

        tvCourseName = findViewById(R.id.tv_course_name);
        tvRequirements = findViewById(R.id.tv_requirements);
        tvDuration = findViewById(R.id.tv_duration);
        tvFee = findViewById(R.id.tv_fee);
        tvTeacher = findViewById(R.id.tv_teacher);
        tvDetails = findViewById(R.id.tv_details);
        tvApply = findViewById(R.id.tv_apply);
        ivCourseImage = findViewById(R.id.iv_course_image);

        courseId=getIntent().getStringExtra("REF_KEY");
        courseRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Online Courses").child(courseId);




        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    courseName=snapshot.child("CourseName").getValue().toString();
                    requirements=snapshot.child("Requirements").getValue().toString();
                    duration=snapshot.child("Duration").getValue().toString();
                    fee=snapshot.child("CourseFee").getValue().toString();
                    teacher=snapshot.child("Teacher").getValue().toString();
                    details=snapshot.child("Details").getValue().toString();
                    courseImage=snapshot.child("CourseImage").getValue().toString();

                    Picasso.get().load(courseImage).into(ivCourseImage);
                    tvCourseName.setText(courseName);
                    tvRequirements.setText(requirements);
                    tvDuration.setText(duration);
                    tvFee.setText(fee);
                    tvTeacher.setText(teacher);
                    tvDetails.setText(details);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                courseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        HashMap postMap = new HashMap();
                        postMap.put("ProductName", tvCourseName.getText().toString());
                        postMap.put("CourseName", tvCourseName.getText().toString());
                        postMap.put("ProductCatagory", "Online Courses");
                        postMap.put("CourseFee",tvFee.getText().toString());
                        postMap.put("Details",tvDetails.getText().toString());
                        postMap.put("Requirements",tvRequirements.getText().toString());
                        postMap.put("Teacher",tvTeacher.getText().toString());
                        postMap.put("Duration",tvDuration.getText().toString());

                        courseRef.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    progressdialog.setMessage("Done");

                                    progressdialog.dismiss();
                                    Intent intent=new Intent(CourseDetailsActivity.this,BottomBarMain.class);
                                    startActivity(intent);

                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });



    }
}

