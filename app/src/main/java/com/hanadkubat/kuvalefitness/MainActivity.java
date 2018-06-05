package com.hanadkubat.kuvalefitness;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hanadkubat.kuvalefitness.Database.Entities.Workout;
import com.hanadkubat.kuvalefitness.Fragments.BMIFragment;
import com.hanadkubat.kuvalefitness.Fragments.EditWorkoutFragment;
import com.hanadkubat.kuvalefitness.Fragments.WelcomeFragment;
import com.hanadkubat.kuvalefitness.Fragments.WorkoutLoggerFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FusedLocationProviderClient locationProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        locationProvider = LocationServices.getFusedLocationProviderClient(this);

        showWelcomeFragment();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            drawerLayout.openDrawer(Gravity.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.itemWelcome)
            showWelcomeFragment();
        else if(item.getItemId() == R.id.itemBmi)
            showBmiFragment();
        else if(item.getItemId() == R.id.itemWorkoutLogger)
            showWorkoutLoggerFragment();
        else if(item.getItemId() == R.id.itemFindGym)
            findGym();

        item.setChecked(true);
        drawerLayout.closeDrawers();

        return true;
    }


    private void showWelcomeFragment()
    {
        Fragment fragment = new WelcomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }


    private void showBmiFragment()
    {
        Fragment fragment = new BMIFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }


    private void showWorkoutLoggerFragment()
    {
        Fragment fragment = new WorkoutLoggerFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }


    public void showEditWorkoutFragment(Workout workout)
    {
        EditWorkoutFragment fragment = new EditWorkoutFragment();
        fragment.setWorkout(workout);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .addToBackStack(null)
                .commit();
    }


    private void findGym()
    {
        int perm = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if(perm != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION }, 0);
        }
        else
        {
            locationProvider.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>()
                    {
                        @Override
                        public void onSuccess(Location location)
                        {
                            if(location != null)
                            {
                                String uriString = String.format("geo:%f,%f?q=gym", location.getLatitude(), location.getLongitude());
                                Uri intentUri = Uri.parse(uriString);

                                Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
                                intent.setPackage("com.google.android.apps.maps");

                                if(intent.resolveActivity(getPackageManager()) != null)
                                    startActivity(intent);
                                else
                                    showMessage(R.string.error_no_maps);
                            }
                            else
                            {
                                showMessage(R.string.error_cant_locate);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            showMessage(R.string.error_cant_locate);
                        }
                    });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            findGym();
    }

    private void showMessage(@StringRes int messageId)
    {
        new AlertDialog.Builder(this)
                .setMessage(messageId)
                .setPositiveButton(R.string.button_ok, null)
                .show();
    }
}
