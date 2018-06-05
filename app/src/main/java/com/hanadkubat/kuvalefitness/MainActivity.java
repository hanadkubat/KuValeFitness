package com.hanadkubat.kuvalefitness;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;

import com.hanadkubat.kuvalefitness.Database.Database;
import com.hanadkubat.kuvalefitness.Database.Entities.Exercise;
import com.hanadkubat.kuvalefitness.Database.Entities.Workout;
import com.hanadkubat.kuvalefitness.Fragments.BMIFragment;
import com.hanadkubat.kuvalefitness.Fragments.EditWorkoutFragment;
import com.hanadkubat.kuvalefitness.Fragments.WorkoutLoggerFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

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

        showBmiFragment();

        List<Exercise> e = Database.getDatabase(this).exerciseDao().getAll();
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
        if(item.getItemId() == R.id.itemBmi)
            showBmiFragment();
        else if(item.getItemId() == R.id.itemWorkoutLogger)
            showWorkoutLoggerFragment();

        item.setChecked(true);
        drawerLayout.closeDrawers();

        return true;
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
}
