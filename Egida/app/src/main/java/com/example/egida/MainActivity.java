package com.example.egida;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private MeowBottomNavigation bottomNavigation;

    private ImageView menuIcon;
    private DrawerLayout navigationDrawerLayout;
    private NavigationView navigationView;

    private TextView title;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        navigationDrawerLayout = findViewById(R.id.navigation_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);

        title = findViewById(R.id.title_of_appBar);

        Check.mainDirectories();

        // устанавливаем цвет системной навигации, чтобы он сливался с navbar-ом
        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        // bottom navigation set icons and buttons
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_delete_forever));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_cloud_download));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_dataset));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_add_circle));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_nearby_share));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId()){
                    case 1:
                        fragment = DeleteFragment.newInstance(Check.encryptedFilesPath);
                        break;
                    case 2:
                        fragment = new DownloadFragment();
                        break;
                    case 3:
                        fragment = LauncherFragment.newInstance(Check.encryptedFilesPath);
                        break;
                    case 4:
                        fragment = new AddFragment();
                        break;
                    case 5:
                        fragment = ShareFragment.newInstance(Check.encryptedFilesPath);
                        break;
                }

                loadFragment(fragment);
            }
        });

        // set default selected fragment and icon
        bottomNavigation.show(3, true);

        // bottom navigation actions
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) { }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) { }
        });

        // navigation drawer actions
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                navigationDrawerLayout.openDrawer(Gravity.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.about:
                        Toast.makeText(getApplicationContext(), "About", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_for_fragments, fragment)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop(){
        super.onStop();

        try {
            Check.clearDecryptedFilesDir();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}