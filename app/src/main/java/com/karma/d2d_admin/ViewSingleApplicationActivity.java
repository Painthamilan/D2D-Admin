package com.karma.d2d_admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewSingleApplicationActivity extends AppCompatActivity {

    TextView tvCourseName,tvFullName,tvAddress,tvPhone,tvEmail,tvQualification,tvLanguage,tvComments;
    String applicationId,courseName,fullName,address,phone,email,qualification,language,comments;
    DatabaseReference appref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_application);

        tvCourseName=findViewById(R.id.tv_course_name);
        tvFullName=findViewById(R.id.tv_full_name);
        tvAddress=findViewById(R.id.tv_address);
        tvPhone=findViewById(R.id.tv_phone_number);
        tvEmail=findViewById(R.id.tv_email);
        tvQualification=findViewById(R.id.tv_qualification);
        tvLanguage=findViewById(R.id.tv_language);
        tvComments=findViewById(R.id.tv_comments);
        applicationId=getIntent().getStringExtra("REF_KEY");
        appref= FirebaseDatabase.getInstance().getReference().child("Applications").child(applicationId);

        appref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    courseName=dataSnapshot.child("CourseName").getValue().toString();
                    tvCourseName.setText(courseName);
                    fullName=dataSnapshot.child("FullName").getValue().toString();
                    tvFullName.setText(fullName);
                    address=dataSnapshot.child("Address").getValue().toString();
                    tvAddress.setText(address);
                    email=dataSnapshot.child("Email").getValue().toString();
                    tvEmail.setText(email);
                    phone=dataSnapshot.child("Mobile").getValue().toString();
                    tvPhone.setText(phone);
                    qualification=dataSnapshot.child("Qualification").getValue().toString();
                    tvQualification.setText(qualification);
                    language=dataSnapshot.child("Language").getValue().toString();
                    tvLanguage.setText(language);
                    if (dataSnapshot.hasChild("Comments")) {
                        comments = dataSnapshot.child("Comments").getValue().toString();
                        tvComments.setText(comments);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
