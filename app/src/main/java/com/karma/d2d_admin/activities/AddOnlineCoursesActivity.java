package com.karma.d2d_admin.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karma.d2d_admin.R;
import com.karma.d2d_admin.utilities.Utils;

import java.util.HashMap;

import static com.karma.d2d_admin.utilities.Utils.RELEASE_TYPE;

public class AddOnlineCoursesActivity extends AppCompatActivity {
    private EditText etCourseName,etCourseFee,etDetails,etRequirements,etDuration,etTeacherName;
    private ImageView ivCourseImage;
    private TextView tvSelect,tvUpload;
    private StorageReference courseStorageRef;
    private DatabaseReference courseRef,catRef;
    private long countPosts;
    private static final int GalleryPick = 1;
    Uri imageUri;
    String randomId,downloadUrl,courseName,courseFee,courseDetails,courseRequrements,courseDuration;

    boolean hasSubCat;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_online_courses);

        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);

        etCourseName=findViewById(R.id.et_course_name);
        etCourseFee=findViewById(R.id.et_course_fee);
        etDetails=findViewById(R.id.et_course_description);
        etDuration=findViewById(R.id.et_duration);
        etRequirements=findViewById(R.id.et_requirements);
        etTeacherName=findViewById(R.id.et_teacher);
        tvSelect=findViewById(R.id.tv_select);
        tvUpload=findViewById(R.id.tv_upload);
        ivCourseImage=findViewById(R.id.iv_course_image);

        courseStorageRef= FirebaseStorage.getInstance().getReference().child(RELEASE_TYPE).child("CourseImages");
        courseRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Online Courses");

        catRef=FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Catagories");

        hasSubCat=false;
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(etCourseName.getText().toString())){
                    Toast.makeText(AddOnlineCoursesActivity.this, "Type course name", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(etCourseFee.getText().toString())){
                    Toast.makeText(AddOnlineCoursesActivity.this, "Type course fee", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(etDetails.getText().toString())){
                    Toast.makeText(AddOnlineCoursesActivity.this, "Type details", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(etDuration.getText().toString())){
                    Toast.makeText(AddOnlineCoursesActivity.this, "Type duration", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(etRequirements.getText().toString())){
                    Toast.makeText(AddOnlineCoursesActivity.this, "Type requirements", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(etTeacherName.getText().toString())){
                    Toast.makeText(AddOnlineCoursesActivity.this, "Type teacher name", Toast.LENGTH_SHORT).show();
                }else {
                    progressdialog.show();
                    storeImage();
                }
            }
        });

    }

    private void storeImage() {

        randomId= Utils.getRandomId();

        if (imageUri != null && !etCourseName.getText().toString().isEmpty() && !etCourseFee.getText().toString().isEmpty()) {
            final StorageReference filepath = courseStorageRef.child(imageUri.getLastPathSegment() + randomId + "jpg");
            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddOnlineCoursesActivity.this, "File Uploaded..", Toast.LENGTH_SHORT).show();

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                downloadUrl = uri.toString();
                                savePostInformation();

                            }
                        });
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(AddOnlineCoursesActivity.this, "UPLOAD ERROR" + message, Toast.LENGTH_SHORT).show();

                    }
                }
            });


        } else {
            Toast.makeText(AddOnlineCoursesActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }
    private void savePostInformation() {
        saveCats(hasSubCat,"Online Courses");
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    countPosts = dataSnapshot.getChildrenCount();
                } else {
                    countPosts = 0;
                }

                HashMap postMap = new HashMap();
                postMap.put("ProductId",randomId);
                postMap.put("ProductName", etCourseName.getText().toString());
                postMap.put("CourseId",randomId);
                postMap.put("CourseName", etCourseName.getText().toString());
                postMap.put("ProductCatagory", "Online Courses");
                postMap.put("CourseFee",etCourseFee.getText().toString());
                postMap.put("ProductImage",downloadUrl);
                postMap.put("CourseImage",downloadUrl);
                postMap.put("Details",etDetails.getText().toString());
                postMap.put("Requirements",etRequirements.getText().toString());
                postMap.put("Teacher",etTeacherName.getText().toString());
                postMap.put("Duration",etDuration.getText().toString());
                postMap.put("Counter", countPosts);

                courseRef.child(randomId).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            progressdialog.setMessage("Done");

                            progressdialog.dismiss();

                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void saveCats(boolean hasCats, String seletedCatagory){
        catRef.child("Online Courses").child("CatagoryName").setValue("Online Courses");
        catRef.child("Online Courses").child("Products").child(randomId).child("ProductId").setValue(randomId);


        catRef.child("Online Courses").child("HasSub").setValue(hasCats);
        // catRef.child(seletedCatagory).child(selectedSubCatagory).child("ProductId").setValue(randomid);

    }


    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ivCourseImage.setImageURI(imageUri);
        }

    }
}
