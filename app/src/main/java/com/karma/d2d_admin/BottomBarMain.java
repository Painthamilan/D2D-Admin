package com.karma.d2d_admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karma.d2d_admin.fragments.HomeFragment;
import com.karma.d2d_admin.fragments.ItemFragment;
import com.karma.d2d_admin.fragments.CatagoriesFragment;
import com.karma.d2d_admin.utilities.Utils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class BottomBarMain extends AppCompatActivity {

    private Fragment home;
    private Fragment manage;
    private Fragment catagories;
    private BottomNavigationView navigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar_navigation);
       /* BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        */

        home = new HomeFragment();
        catagories = new ItemFragment();
        manage = new CatagoriesFragment();
        //  myinfo = new MyInfoFragment();
        // notification = new NotificationsFragment();
        navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    @Override
    protected void onResume() {
        super.onResume();
        navigation.getMenu().findItem(Utils.CURRENT_NAVIGATION_BAR).setChecked(true);
        selectFragment();

    }
    private void selectFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            transaction.hide(fragment);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Utils.CURRENT_NAVIGATION_BAR + "");
        if (fragment != null) {
            transaction.show(fragment);
        }

        if (R.id.navigation_home == Utils.CURRENT_NAVIGATION_BAR) {
            if (fragment == null) {
                transaction.add(R.id.nav_host_fragment, home, Utils.CURRENT_NAVIGATION_BAR + "").show(home);
            }
        } else if (R.id.navigation_dashboard == Utils.CURRENT_NAVIGATION_BAR) {
            if (fragment == null) {
                transaction.add(R.id.nav_host_fragment, catagories, Utils.CURRENT_NAVIGATION_BAR + "").show(catagories);
            }
        } else if (R.id.navigation_notifications == Utils.CURRENT_NAVIGATION_BAR) {
            if (fragment == null) {
                transaction.add(R.id.nav_host_fragment, manage, Utils.CURRENT_NAVIGATION_BAR + "").show(manage);
            }
        }
        else {
            if (fragment == null) {
                transaction.add(R.id.nav_host_fragment, home, Utils.CURRENT_NAVIGATION_BAR + "").show(home);
            }
        }

        transaction.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);
        transaction.commitAllowingStateLoss();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.isChecked()) {
                Fragment fragment = BottomBarMain.this.getSupportFragmentManager().findFragmentByTag(Utils.CURRENT_NAVIGATION_BAR + "");
                if (fragment instanceof RefreshableFragment) {
                    ((RefreshableFragment) fragment).refresh();
                    return true;
                }
            }
            Utils.CURRENT_NAVIGATION_BAR = item.getItemId();
            item.setChecked(true);
            BottomBarMain.this.selectFragment();
            return true;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        System.exit(1);


    }

}
