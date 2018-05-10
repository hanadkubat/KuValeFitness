package com.hanadkubat.kuvalefitness;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hanadkubat.kuvalefitness.Fragments.BMIFragment;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment bmiFragment = new BMIFragment();
        getFragmentManager().beginTransaction().add(R.id.fragmentContainer, bmiFragment).commit();
    }
}
