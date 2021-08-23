package com.darwinbark.yahochat.shubhcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.darwinbark.yahochat.R;
import com.darwinbark.yahochat.activities.ChatActivity;
import com.darwinbark.yahochat.databinding.ActivityHome2Binding;
import com.darwinbark.yahochat.fragments.HomeMainFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity2 extends AppCompatActivity implements View.OnClickListener {

    private ActivityHome2Binding mBinding;
    Toolbar toolbar;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    NavigationView navigationView;

    private Menu mToolbarMenu;


    public void setToolbarMenu(Menu toolbarMenu) {
        mToolbarMenu = toolbarMenu;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Toast.makeText(HomeActivity2.this, "I am Home", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.myCalenderNav:
                        // to calenderactivity
                        startActivity(new Intent(HomeActivity2.this, HomeActivity2.class));
                        break;

                    case R.id.chatNav:
                        Toast.makeText(HomeActivity2.this, "I am Chat", Toast.LENGTH_SHORT).show();
                        // to chatactivity
                        startActivity(new Intent(HomeActivity2.this, ChatActivity.class));
                        return true;

                }
                return false;
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_1);
        drawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_toolbar_menu, menu);
        setToolbarMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void clickOnHome() {
        mBinding.textViewGenerate.setTextColor(
                ContextCompat.getColor(this, R.color.white));

        mBinding.textViewScan.setTextColor(
                ContextCompat.getColor(this, R.color.shadow));

        mBinding.textViewHistory.setTextColor(
                ContextCompat.getColor(this, R.color.shadow));

        mBinding.imageViewGenerate.setVisibility(View.INVISIBLE);
        mBinding.imageViewGenerateActive.setVisibility(View.VISIBLE);

        mBinding.imageViewScan.setVisibility(View.VISIBLE);
        mBinding.imageViewScanActive.setVisibility(View.INVISIBLE);

        mBinding.imageViewHistory.setVisibility(View.VISIBLE);
        mBinding.imageViewHistoryActive.setVisibility(View.INVISIBLE);

        setToolbarTitle(getString(R.string.toolbar_title_Home));
        showFragment(new HomeMainFragment());
    }

    private void clickOnCalender() {
        mBinding.textViewScan.setTextColor(
                ContextCompat.getColor(this, R.color.white));

        mBinding.textViewGenerate.setTextColor(
                ContextCompat.getColor(this, R.color.shadow));

        mBinding.textViewHistory.setTextColor(
                ContextCompat.getColor(this, R.color.shadow));

        mBinding.imageViewGenerate.setVisibility(View.VISIBLE);
        mBinding.imageViewGenerateActive.setVisibility(View.INVISIBLE);

        mBinding.imageViewScan.setVisibility(View.INVISIBLE);
        mBinding.imageViewScanActive.setVisibility(View.VISIBLE);

        mBinding.imageViewHistory.setVisibility(View.VISIBLE);
        mBinding.imageViewHistoryActive.setVisibility(View.INVISIBLE);

        setToolbarTitle("Calender");
        // showFragment(new profileFragment());

    }

    private void clickOnWallet() {
        mBinding.textViewHistory.setTextColor(
                ContextCompat.getColor(this, R.color.white));

        mBinding.textViewGenerate.setTextColor(
                ContextCompat.getColor(this, R.color.shadow));

        mBinding.textViewScan.setTextColor(
                ContextCompat.getColor(this, R.color.shadow));



        mBinding.imageViewGenerate.setVisibility(View.VISIBLE);
        mBinding.imageViewGenerateActive.setVisibility(View.INVISIBLE);

        mBinding.imageViewScan.setVisibility(View.VISIBLE);
        mBinding.imageViewScanActive.setVisibility(View.INVISIBLE);

        mBinding.imageViewHistory.setVisibility(View.INVISIBLE);
        mBinding.imageViewHistoryActive.setVisibility(View.VISIBLE);

        setToolbarTitle(getString(R.string.toolbar_title_wallet));
        // showFragment(new WalletFragment());

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // calender
            case R.id.image_view_generate:
            case R.id.text_view_generate:
            case R.id.constraint_layout_generate_container:
                clickOnHome();
                break;

            // home
            case R.id.image_view_scan:
            case R.id.text_view_scan:
            case R.id.constraint_layout_scan_container:
                clickOnCalender();
                break;

            // wallet
            case R.id.image_view_history:
            case R.id.text_view_history:
            case R.id.constraint_layout_history_container:
                clickOnWallet();
                break;
        }
    }

    private void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.coordinator_layout_fragment_container, fragment,
                fragment.getClass().getSimpleName());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        if (mBinding.imageViewGenerateActive.getVisibility() != View.VISIBLE) {

            clickOnHome();
        } else {
            finish();
            finishAffinity();

        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

}