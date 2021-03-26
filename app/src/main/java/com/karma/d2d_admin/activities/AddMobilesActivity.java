package com.karma.d2d_admin.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import static com.karma.d2d_admin.utilities.Utils.RELEASE_TYPE;

public class AddMobilesActivity extends AppCompatActivity {
    private EditText etProductName,etPrice,etSpecifications,etPercentage;
    private ImageView ivProductImage;
    private TextView tvSelectcatagory,tvUpload,tvSelectImage, tvSelectBrand;
    private String seletedCatagory="",curDate,curTime,randomid,downloadUrl,  selectedBrand,specifications;
    private StorageReference itemStorageRef;
    private DatabaseReference itemsRef,catRef,instantRef;
    private long countPosts;
    private boolean hasSubCat;
    private static final int GalleryPick = 1;
    Uri imageUri;
    DatabaseReference cfPostRef;

    ProgressDialog progressdialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);
        etProductName=findViewById(R.id.et_product_name);
        etPrice=findViewById(R.id.et_price);
        etSpecifications=findViewById(R.id.et_specifications);
        etPercentage=findViewById(R.id.et_percentage);
        ivProductImage=findViewById(R.id.iv_product_image);
        tvSelectcatagory=findViewById(R.id.tv_select_catogary);
        tvSelectImage=findViewById(R.id.tv_select_image);
        tvUpload=findViewById(R.id.tv_upload);
        tvSelectBrand =findViewById(R.id.tv_select_sub_catogary);

        itemStorageRef= FirebaseStorage.getInstance().getReference().child(RELEASE_TYPE).child("ProductImages");
        itemsRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Products");
        catRef=FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Catagories");
        hasSubCat=false;
        final PopupMenu popup = new PopupMenu(this,tvSelectcatagory);

        tvSelectBrand.setVisibility(View.VISIBLE);
        popup.getMenuInflater().inflate(R.menu.main_catagory, popup.getMenu());
      tvSelectcatagory.setVisibility(View.INVISIBLE);
      seletedCatagory="Mobiles";

        tvSelectBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSubmenus();
            }
        });
        tvSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressdialog.show();
                storeImage();
            }
        });
    }
    private void validateSubmenus() {
        PopupMenu subMenuEducation=new PopupMenu(this,tvSelectcatagory);
        subMenuEducation.getMenuInflater().inflate(R.menu.menu_brands, subMenuEducation.getMenu());


        subMenuEducation.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.brand_apple:
                        selectedBrand ="Apple";
                        break;
                    case R.id.brand_asus:
                        selectedBrand ="Asus";
                        break;
                    case R.id.brand_google:
                        selectedBrand ="Google Pixel";
                        break;
                    case R.id.brand_htc:
                        selectedBrand ="HTC";
                        break;
                    case R.id.brand_huawei:
                        selectedBrand ="Huawei";
                        break;
                    case R.id.brand_lenovo:
                        selectedBrand ="Lenovo";
                        break;
                    case R.id.brand_lg:
                        selectedBrand ="LG";
                        break;
                    case R.id.brand_motorola:
                        selectedBrand ="Motorola";
                        break;
                    case R.id.brand_nokia:
                        selectedBrand ="Nokia";
                        break;
                    case R.id.brand_onleplus:
                        selectedBrand ="OnePlus";
                        break;
                    case R.id.brand_oppo:
                        selectedBrand ="Oppo";
                        break;
                    case R.id.brand_panasonic:
                        selectedBrand ="Panasonic";
                        break;
                    case R.id.brand_samsung:
                        selectedBrand ="Samsung";
                        break;
                    case R.id.brand_sony:
                        selectedBrand ="Sony";
                        break;
                    case R.id.brand_vivo:
                        selectedBrand ="Vivo";
                        break;
                    case R.id.brand_xiaomi:
                        selectedBrand ="Xiaomi";
                        break;
                    case R.id.brand_zte:
                        selectedBrand ="ZTE";
                        break;
                }
                tvSelectBrand.setText(selectedBrand);
                return true;
            }
        });
        subMenuEducation.show();
    }


    private void storeImage() {

        if(!seletedCatagory.equals("")) {

            randomid = Utils.getRandomId();

            if (imageUri != null && !etPrice.getText().toString().isEmpty() && !etProductName.getText().toString().isEmpty()) {
                final StorageReference filepath = itemStorageRef.child(imageUri.getLastPathSegment() + randomid + "jpg");
                filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(AddMobilesActivity.this, "File Uploaded..", Toast.LENGTH_SHORT).show();

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    downloadUrl = uri.toString();
                                    savePostInformation();

                                }
                            });
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(AddMobilesActivity.this, "UPLOAD ERROR" + message, Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            } else {
                Toast.makeText(AddMobilesActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Please SELECT CATAGORY", Toast.LENGTH_LONG).show();
            progressdialog.dismiss();
        }

    }

    private void savePostInformation() {
        itemsRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Products");
        saveCats(hasSubCat,seletedCatagory);
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    countPosts = dataSnapshot.getChildrenCount();
                } else {
                    countPosts = 0;
                }

                HashMap postMap = new HashMap();
                postMap.put("ProductId",randomid);
                postMap.put("ProductName", etProductName.getText().toString());
                postMap.put("ProductCatagory", seletedCatagory);
                postMap.put("Brand", selectedBrand);
                postMap.put("Price",etPrice.getText().toString());
                postMap.put("Percentage",etPercentage.getText().toString());
                postMap.put("ProductImage",downloadUrl);
                postMap.put("Specifications",etSpecifications.getText().toString());
                postMap.put("Counter", countPosts);

                itemsRef.child(randomid).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            progressdialog.setMessage("Done");

                            progressdialog.dismiss();

                            Intent intent=new Intent(AddMobilesActivity.this,SelectRegionActivity.class);
                            intent.putExtra("PRODUCT_ID",randomid);
                            intent.putExtra("HAS_SUBCAT",hasSubCat);
                            intent.putExtra("CATAGORY",seletedCatagory);
                            if (hasSubCat)
                                intent.putExtra("Brand", selectedBrand);

                            //  startActivity(intent);
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
        catRef.child(seletedCatagory).child("CatagoryName").setValue(this.seletedCatagory);
        if (hasCats){
            catRef.child(seletedCatagory).child("Brand").child(selectedBrand).child("BrandName").setValue(selectedBrand);
            catRef.child(seletedCatagory).child("Brand").child(selectedBrand).child("Products").child(randomid).child("ProductId").setValue(randomid);
        }else{
            catRef.child(seletedCatagory).child("Products").child(randomid).child("ProductId").setValue(randomid);
        }

        catRef.child(this.seletedCatagory).child("HasSub").setValue(hasCats);
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
            ivProductImage.setImageURI(imageUri);
        }

    }
}
