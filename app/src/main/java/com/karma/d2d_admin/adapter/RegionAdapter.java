package com.karma.d2d_admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.karma.d2d_admin.R;
import com.karma.d2d_admin.domains.Region;

import java.util.List;

import static com.karma.d2d_admin.utilities.Utils.RELEASE_TYPE;

public class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.RegionViewHolder> {
    List<Region> mRegionList;
    Context context;
    String productId;
    DatabaseReference regionRef;

    public RegionAdapter(Context context, String productId) {
        this.context = context;
        this.productId = productId;
    }

    public void addAll(List<Region> newRegions){

        int initSize=newRegions.size();
        mRegionList.addAll(newRegions);
        notifyItemRangeChanged(initSize,newRegions.size());

    }

    @NonNull
    @Override
    public RegionAdapter.RegionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.region_selector_layout,parent,false);

        return new RegionAdapter.RegionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RegionAdapter.RegionViewHolder holder, final int position) {

        holder.getItemDetails(mRegionList.get(position).getRegionName());
       holder.tvAddRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reg=mRegionList.get(position).getRegionName();

                regionRef=FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Regions").child(reg);
                regionRef.child("RegionName").setValue(reg);
                regionRef.child("ProductId").setValue(productId);
                    Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

            }
        });




    }

    @Override
    public int getItemCount() {
        return mRegionList.size();
    }

    public class RegionViewHolder extends RecyclerView.ViewHolder {

        TextView tvRegionName,tvAddRegion;
        DatabaseReference regionRef;

        public RegionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddRegion=itemView.findViewById(R.id.tv_add_region);
            tvRegionName=itemView.findViewById(R.id.tv_region_name);

        }
        public void getItemDetails(String regionName) {
            // regionRef= FirebaseDatabase.getInstance().getReference().child("Regions").child(regionName);

            tvRegionName.setText(regionName);
        }

    }

}
