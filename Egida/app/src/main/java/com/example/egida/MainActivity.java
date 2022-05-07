package com.example.egida;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    // bottom navigation var
    MeowBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);

        // bottom navigation set icons and buttons
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_delete_forever));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_dataset));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_add_circle));

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId()){
                    case 1:
                        fragment = new AddFragment();
                        break;
                    case 2:
                        fragment = new LauncherFragment();
                        break;
                    case 3:
                        fragment = new DeleteFragment();
                }

                loadFragment(fragment);
            }
        });

        // set default selected icon/button
        bottomNavigation.show(2, true);

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) { }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) { }
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