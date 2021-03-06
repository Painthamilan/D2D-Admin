package com.karma.d2d_admin.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.karma.d2d_admin.R;
import com.karma.d2d_admin.adapter.CatagoryAdapter;
import com.karma.d2d_admin.domains.Catagories;
import com.karma.d2d_admin.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.karma.d2d_admin.utilities.Utils.RELEASE_TYPE;

public class AllCatActivity extends AppCompatActivity {

    RecyclerView rvCats;
    DatabaseReference listCatsRef;

    final int ITEM_LOAD_COUNT = 5;
    int tota_item = 0, last_visible_item;

    CatagoryAdapter catagoryAdapter;
    boolean isLoading = false, isMaxData = false;
    String last_node = "", last_key = "";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cat);
        Utils.setTopBar(getWindow(),getResources());

        final TextView textView = findViewById(R.id.text_dashboard);
        rvCats = findViewById(R.id.rv_list_cats);
        // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));

        getLastItem();

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        // Set the layout manager to your recyclerview
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCats.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(rvCats.getContext(),mLayoutManager.getOrientation());
        rvCats.addItemDecoration(dividerItemDecoration);



        catagoryAdapter = new CatagoryAdapter(this);
        rvCats.setAdapter(catagoryAdapter);
        getCats();

        rvCats.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                tota_item = mLayoutManager.getItemCount();
                last_visible_item = mLayoutManager.findLastCompletelyVisibleItemPosition();

                if (!isLoading && tota_item <= ((last_visible_item + ITEM_LOAD_COUNT))) {
                    getCats();
                    isLoading = true;
                }

            }
        });

        catagoryAdapter.notifyDataSetChanged();

    }

    private void getCats() {

        if (!isMaxData) {
            Query query;
            if (TextUtils.isEmpty(last_node))

                query = FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Catagories")
                        .orderByKey()
                        .limitToFirst(ITEM_LOAD_COUNT);

            else

                query = FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Catagories")
                        .orderByKey()
                        .startAt(last_node)
                        .limitToFirst(ITEM_LOAD_COUNT);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        List<Catagories> newCats = new ArrayList<>();
                        for (DataSnapshot catSnapShot : snapshot.getChildren()) {
                            newCats.add(catSnapShot.getValue(Catagories.class));
                        }
                        last_node = newCats.get(newCats.size() - 1).getCatagoryName();

                        if (!last_node.equals(last_key))
                            newCats.remove(newCats.size() - 1);
                        else
                            last_node = "end";

                        catagoryAdapter.addAll(newCats);
                        isLoading = false;

                    } else {
                        isLoading = false;
                        isMaxData = true;
                        Toast.makeText(AllCatActivity.this, "No more", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    isLoading = false;

                }
            });
        }
    }

    private void getLastItem() {

        Query getLastKey = FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE)
                .child("Catagories")
                .orderByKey()
                .limitToLast(1);
        getLastKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot lastKey : snapshot.getChildren())
                    last_key = lastKey.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(AllCatActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
