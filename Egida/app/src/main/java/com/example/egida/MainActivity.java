package com.example.egida;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.biometric.BiometricPrompt;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    // bottom navigation var
    private MeowBottomNavigation bottomNavigation;

    // navigation drawer
    private ImageView menuIcon;
    private DrawerLayout navigationDrawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        navigationDrawerLayout = findViewById(R.id.navigation_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menuIcon = findViewById(R.id.menu_icon);

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
                        fragment = new DeleteFragment();
                        break;
                    case 2:
                        fragment = new DownloadFragment();
                        break;
                    case 3:
                        fragment = new LauncherFragment();
                        break;
                    case 4:
                        fragment = new AddFragment();
                        break;
                    case 5:
                        fragment = new ShareFragment();
                        break;
                }

                loadFragment(fragment);
            }
        });

        // set default selected icon
        bottomNavigation.show(3, true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) { }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) { }
        });

        // navigation drawer
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                navigationDrawerLayout.openDrawer(Gravity.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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

    // method for load fragments on activity
    private void loadFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_for_fragments, fragment)
                .commit();
    }
}