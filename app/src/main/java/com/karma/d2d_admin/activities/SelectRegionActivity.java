package com.karma.d2d_admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karma.d2d_admin.R;

import static com.karma.d2d_admin.utilities.Utils.RELEASE_TYPE;

public class SelectRegionActivity extends AppCompatActivity {

    String productId, origin,seletedCatagory,selectedSubCatagory,region;
    ListView rvRegion;
    ArrayAdapter aAdapter;
    DatabaseReference regRef,productRef;
    TextView tvSelectOrigin,tvFinish;
    boolean hasSubCat;

    ProgressDialog progressdialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_region);

        productId = getIntent().getStringExtra("PRODUCT_ID");
        hasSubCat=getIntent().getBooleanExtra("HAS_SUBCAT",false);
        seletedCatagory=getIntent().getStringExtra("CATAGORY");
        selectedSubCatagory=getIntent().getStringExtra("SUB_CATAGORY");

        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);

        tvSelectOrigin = findViewById(R.id.tv_select_origin);
        tvFinish=findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SelectRegionActivity.this,BottomBarMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        rvRegion = findViewById(R.id.rv_regions);
        String[] regs = getResources().getStringArray(R.array.regions);
        regRef = FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Regions");
        productRef=FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Products").child(productId);

        aAdapter = new ArrayAdapter(this, R.layout.region_selector_layout, R.id.tv_region_name, regs);
        rvRegion.setAdapter(aAdapter);

        rvRegion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                progressdialog.show();
                TextView textView = view.findViewById(R.id.tv_region_name);
                TextView add=view.findViewById(R.id.tv_add_region);
                String string = textView.getText().toString();
                regRef.child(string).child("RegionName").setValue(string);
                if (hasSubCat){
                    regRef.child(string).child("Catagories").child(seletedCatagory).child("CatagoryName").setValue(selectedSubCatagory);
                    regRef.child(string).child("Catagories").child(seletedCatagory).child("SubCatagories").child(selectedSubCatagory).child("SubCatagoryName").setValue(selectedSubCatagory);
                    regRef.child(string).child("Catagories").child(seletedCatagory).child("SubCatagories").child(selectedSubCatagory).child("Products").child(productId).child("ProductId").setValue(productId);
                    regRef.child(string).child("Catagories").child(seletedCatagory).child("HasSub").setValue(true);
                }else {
                    regRef.child(string).child("Catagories").child(seletedCatagory).child("CatagoryName").setValue(seletedCatagory);
                    regRef.child(string).child("Catagories").child(seletedCatagory).child("HasSub").setValue(false);
                    regRef.child(string).child("Catagories").child(seletedCatagory).child("Products").child(productId).child("ProductId").setValue(productId);


                }
               Toast.makeText(SelectRegionActivity.this, "Added to " + string, Toast.LENGTH_SHORT).show();
                progressdialog.dismiss();
            }
        });

        final PopupMenu popup = new PopupMenu(this, tvSelectOrigin);

        popup.getMenuInflater().inflate(R.menu.region_menu, popup.getMenu());

        SharedPreferences  preferences=getSharedPreferences("REGION_SELECTOR",MODE_PRIVATE);
        region=preferences.getString("REGION","");

        if (!region.equals("Main")){
            tvSelectOrigin.setVisibility(View.INVISIBLE);
            productRef.child("Origin").setValue(region);
        }

        tvSelectOrigin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.region_colombo:
                                origin = "Colombo";
                                break;
                            case R.id.region_kandy:
                                origin = "Kandy";
                                break;
                            case R.id.region_jaffna:
                                origin = "Jaffna";
                                break;
                            case R.id.region_matale:
                                origin = "Matale";
                                break;
                            case R.id.region_kilinochi:
                                origin = "Kilinochi";
                                break;
                            case R.id.region_mannar:
                                origin = "Mannar";
                                break;
                            case R.id.region_mullai:
                                origin = "Mullaitivu";
                                break;
                            case R.id.region_nuwara_eliya:
                                origin = "NuwaraEliya";
                                break;
                            case R.id.region_ampara:
                                origin = "Ampara";
                                break;
                            case R.id.region_anuradhapura:
                                origin = "Anuradhapura";
                                break;
                            case R.id.region_badulla:
                                origin = "Badulla";
                                break;
                            case R.id.region_batticaloa:
                                origin = "Batticaloa";
                                break;
                            case R.id.region_galle:
                                origin = "Galle";
                                break;
                            case R.id.region_gampaha:
                                origin = "Gampaha";
                                break;
                            case R.id.region_hambantota:
                                origin = "Hambantota";
                                break;
                            case R.id.region_kalutara:
                                origin = "Kalutara";
                                break;
                            case R.id.region_kegalle:
                                origin = "Kegalle";
                                break;
                            case R.id.region_kurunegala:
                                origin = "Kurunegala";
                                break;
                            case R.id.region_matara:
                                origin = "Matara";
                                break;
                            case R.id.region_monaragala:
                                origin = "Monaragala";
                                break;
                            case R.id.region_polonnaruwa:
                                origin = "Polannaruwa";
                                break;
                            case R.id.region_puttalam:
                                origin = "Puttalam";
                                break;
                            case R.id.region_ratnapura:
                                origin = "Ratnapura";
                                break;
                            case R.id.region_trincomalee:
                                origin = "Trincomalee";
                                break;
                            case R.id.region_vavuniya:
                                origin = "Vavuniya";
                                break;

                        }
                        tvSelectOrigin.setText(origin);
                        productRef.child("Origin").setValue(origin);
                        return true;
                    }
                });
                popup.show();

            }
        });


    }
}
