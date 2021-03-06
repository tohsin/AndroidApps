package com.leadway.remoteportalapp;

import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.Button;

import com.leadway.remoteportalapp.ui.LogOutDialogFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NewStaffActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static Boolean isSupervisor = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_staff);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        if (isSupervisor){
            navigationView.getMenu().setGroupVisible(R.id.StaffHamburgerMenu,false);
            navigationView.getMenu().setGroupVisible(R.id.SupervisorHamburgerMenu,true);
            //navigationView.inflateMenu(R.id.supervisorRequestListFragment);
        }else {
            navigationView.getMenu().setGroupVisible(R.id.StaffHamburgerMenu,true);
            navigationView.getMenu().setGroupVisible(R.id.SupervisorHamburgerMenu,false);
            //navigationView.inflateMenu(R.id.n);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.remoteSessions, R.id.supervisorRequestListFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        this.getSupportActionBar().hide();

        if (isSupervisor){
            navController.navigate(R.id.supervisorRequestListFragment);
        }

        Button btnLogOut = findViewById(R.id.LogOut);
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOutDialogFragment alert = new LogOutDialogFragment();
                alert.show(getSupportFragmentManager(), "logOut");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_staff, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


}
