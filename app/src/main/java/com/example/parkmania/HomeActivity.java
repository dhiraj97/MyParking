package com.example.parkmania;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    NavigationView navView;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mToggle;
    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();


        navView = findViewById(R.id.nav_view);
        navView.setItemIconTintList(null);

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        //trans.replace(R.id.frame, new NearbyPatientsFragment());
        trans.commit();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mToolbar = findViewById(R.id.toolbar);

        //adds a toolbar to the activity
        setSupportActionBar(mToolbar);

        mDrawerLayout.addDrawerListener(mToggle);

        //synchronize  the  indicator  icon(Home)  with  the  state  of  the linked DrawerLayout
        mToggle.syncState();


        //Sets the hamburger icon in the actionbar to trigger state of the drawer layout
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setNavigationDrawer();
        
    }

    private void setNavigationDrawer() {
        //Initial item in navView is checked
        navView.getMenu().getItem(0).setChecked(true);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment frag = null;
                int itemId = menuItem.getItemId();
                //Indicates by highlighting which fragment we currently are in
                menuItem.setChecked(true);
                if (itemId == R.id.nav_add_parking) {
                    frag = new AddParkingFragment();
                } else if (itemId == R.id.nav_view_parking) {
                    frag = new ViewParkingFragment();
                } else if (itemId == R.id.nav_edit_parking) {
                    frag = new EditParkingFragment();
                } else if (itemId == R.id.nav_user_profile) {
                    frag = new UserProfileFragment();
                }
                Toast.makeText(getApplicationContext(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                if (frag != null) {
                    FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                    trans.replace(R.id.frame, frag);
                    trans.commit();
                    mDrawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.my_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        logoutUser();
    }

    private void logoutUser() {
        mAuth.signOut();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user == null) {
            startActivity(new Intent(HomeActivity.this, SignUpActivity.class));
        }
    }
}