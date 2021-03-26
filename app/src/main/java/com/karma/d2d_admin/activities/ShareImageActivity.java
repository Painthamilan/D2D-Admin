package com.karma.d2d_admin.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karma.d2d_admin.R;
import com.karma.d2d_admin.utilities.Shareable;
import com.karma.d2d_admin.utilities.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareImageActivity extends AppCompatActivity {

    ImageView ivImage;
    TextView tvHeading,tvDetails,tvSelect,tvUpload;
    EditText etHeading,etDetails;
    private static final int GalleryPick = 1;
    Uri ImageUri;
    String heading,details;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_image);
        etDetails=findViewById(R.id.et_specifications);
        etHeading=findViewById(R.id.et_product_name);
        tvSelect=findViewById(R.id.tv_select_image);
        tvUpload=findViewById(R.id.tv_upload);
        ivImage=findViewById(R.id.iv_product_image);


        details=etDetails.getText().toString().trim();
        heading=etHeading.getText().toString().trim();

        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallary();
            }
        });

        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ImageUri == null&& TextUtils.isEmpty(details)&&TextUtils.isEmpty(heading)) {
                    Toast.makeText(ShareImageActivity.this, "Add All text and image", Toast.LENGTH_SHORT).show();
                }else {
                    openPopup();
                }
            }
        });

    }

    private void openPopup() {
        final View rowView = LayoutInflater.from(ShareImageActivity.this).inflate(R.layout.popup_share, null);
        final AlertDialog dialog = Utils.configDialog(ShareImageActivity.this, rowView);
        tvDetails = rowView.findViewById(R.id.tv_details);
        tvHeading = rowView.findViewById(R.id.tv_heading);
        TextView tvShare = rowView.findViewById(R.id.done);
        TextView tvCancel = rowView.findViewById(R.id.clear_all);
        final ImageView ivImage=rowView.findViewById(R.id.iv_share_image);
        ivImage.setImageURI(ImageUri);

        final ConstraintLayout shareLayout = rowView.findViewById(R.id.constraintLayout);
        //  tvTitle.setText("Delete Comment");
        //  tvMessage.setText("Are you sure you want to delete this comment?");

        details=etDetails.getText().toString().trim();
        heading=etHeading.getText().toString().trim();
        tvHeading.setText(heading);
        tvDetails.setText(details);

        tvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                shareLayout.setDrawingCacheEnabled(true);
                shareLayout.buildDrawingCache();
                Bitmap bm = shareLayout.getDrawingCache();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                Uri uri = getBitmapUri(bm);
                ivImage.setImageURI(uri);
                try {
                    shareImageToSocialMedia(ivImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ShareImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    private Uri getBitmapUri(Bitmap mergeBitmap) {

        int quality = 100;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        mergeBitmap.compress(Bitmap.CompressFormat.PNG, quality, os);
        String path = MediaStore.Images.Media.insertImage(ShareImageActivity.this.getContentResolver(), mergeBitmap, "d2d", null);
        return Uri.parse(path);
    }
    public  void shareImageToSocialMedia(ImageView ivImage) throws IOException {
        BitmapDrawable drawable = (BitmapDrawable) ivImage.getDrawable();

        Context context=ShareImageActivity.this;
        // Bitmap bitmap = ivPostImage.getDrawingCache();


        if (drawable != null) {
            Bitmap bitmap = drawable.getBitmap();
            //   Toast.makeText(context, "hghjhj", Toast.LENGTH_SHORT).show();
            File sdCard = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File dir = new File(sdCard.getAbsolutePath() + "/D2D");
            dir.mkdir();
            String fname = Utils.getRandomId();
            File outFile = new File(dir, fname);
            // File file = new File(myDir, sdCard);

            FileOutputStream out = new FileOutputStream(outFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            out.flush();
            out.close();


          //  Bitmap appIconBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.launcher_transparant);

           // Bitmap resizedAppIconBitmap = Bitmap.createScaledBitmap(appIconBitmap, 125, 125, false);
          //  Bitmap resizedScreenBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, false);

            //Bitmap mergeBitmap = mergeTwoBitmap(resizedScreenBitmap, context);
            Uri mergeUri = getBitmapUri(bitmap, context, Utils.getRandomId());
            Shareable shareInstance = new Shareable.Builder(context)
                    .message(heading+"\n"+details+"\n")
                    .socialChannel(Shareable.Builder.ANY)
                    .image(mergeUri)
                    .url("https://play.google.com/store/apps/details?id=com.doordelivery.karma")
                    .build();
            shareInstance.share();
        }else {
            Shareable shareInstance = new Shareable.Builder(context)
                    .message(heading+"\n"+details+"\n")
                    .socialChannel(Shareable.Builder.ANY)
                    .url("https://play.google.com/store/apps/details?id=com.doordelivery.karma")
                    .build();
            shareInstance.share();
        }
    }

    private static Bitmap mergeTwoBitmap(Bitmap resizedScreenBitmap, Context context) {
        int resizedScreenWidth = resizedScreenBitmap.getWidth();
        int resizedScreenHeight = resizedScreenBitmap.getHeight();
        int left = 5;
        int top = 5;
        Bitmap mergeBitmap = Bitmap.createBitmap(resizedScreenWidth, resizedScreenHeight, resizedScreenBitmap.getConfig());
        Canvas canvas = new Canvas(mergeBitmap);
        canvas.drawBitmap(resizedScreenBitmap, new Matrix(), null);
       // canvas.drawBitmap(resizedAppIconBitmap, left, top, null);
        return mergeBitmap;
    }

    private static Uri getBitmapUri(Bitmap mergeBitmap, Context context, String postId) {
        int quality = 100;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        mergeBitmap.compress(Bitmap.CompressFormat.JPEG, quality, os);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), mergeBitmap, Utils.getRandomId(), null);
        return Uri.parse(path);
    }


    private void openGallary() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            ivImage.setImageURI(ImageUri);
        }

    }
}