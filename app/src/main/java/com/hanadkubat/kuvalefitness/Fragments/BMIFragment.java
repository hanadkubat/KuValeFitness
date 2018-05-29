package com.hanadkubat.kuvalefitness.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hanadkubat.kuvalefitness.BMICalculator;
import com.hanadkubat.kuvalefitness.R;

import java.util.HashMap;
import java.util.Map;

public class BMIFragment extends Fragment implements SeekBar.OnSeekBarChangeListener
{
    private ImageView imgBMI;
    private SeekBar sbWeight;
    private SeekBar sbHeigth;
    private TextView txtWeight;
    private TextView txtHeight;
    private TextView txtBMIIndex;

    private Map<BMICalculator.BMIClass, Integer> bmiImageMap;
    private Map<BMICalculator.BMIClass, Integer> bmiColorMap;

    public BMIFragment()
    {
        bmiImageMap = new HashMap<>();
        bmiImageMap.put(BMICalculator.BMIClass.Underweight, R.drawable.underweight);
        bmiImageMap.put(BMICalculator.BMIClass.Normal, R.drawable.normal);
        bmiImageMap.put(BMICalculator.BMIClass.Overweight, R.drawable.overweight);
        bmiImageMap.put(BMICalculator.BMIClass.ObeseClass1, R.drawable.obese1);
        bmiImageMap.put(BMICalculator.BMIClass.ObeseClass2, R.drawable.obese2);
        bmiImageMap.put(BMICalculator.BMIClass.ObeseClass3, R.drawable.obese3);

        bmiColorMap = new HashMap<>();
        bmiColorMap.put(BMICalculator.BMIClass.Underweight, R.color.colorBMIUnderweight);
        bmiColorMap.put(BMICalculator.BMIClass.Normal, R.color.colorBMINormal);
        bmiColorMap.put(BMICalculator.BMIClass.Overweight, R.color.colorBMIOverweight);
        bmiColorMap.put(BMICalculator.BMIClass.ObeseClass1, R.color.colorBMIObese1);
        bmiColorMap.put(BMICalculator.BMIClass.ObeseClass2, R.color.colorBMIObese2);
        bmiColorMap.put(BMICalculator.BMIClass.ObeseClass3, R.color.colorBMIObese3);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_bmi, null);

        sbWeight = view.findViewById(R.id.sbWeight);
        sbHeigth = view.findViewById(R.id.sbHeigth);
        txtWeight = view.findViewById(R.id.txtWeight);
        txtHeight = view.findViewById(R.id.txtHeight);
        imgBMI = view.findViewById(R.id.imgBMI);
        txtBMIIndex = view.findViewById(R.id.txtBmiIndex);

        sbWeight.setOnSeekBarChangeListener(this);
        sbHeigth.setOnSeekBarChangeListener(this);

        loadData();
        updateScreen();

        return view;
    }


    private void updateScreen()
    {
        int weightKG = sbWeight.getProgress();
        int heigthCM = sbHeigth.getProgress();

        BMICalculator.BMIData bmiData = BMICalculator.calculate(weightKG, heigthCM);

        String weight = getActivity().getString(R.string.label_weight);
        weight = String.format(weight, String.valueOf(bmiData.getWeightKG()));
        txtWeight.setText(weight);

        String height = getActivity().getString(R.string.label_height);
        height = String.format(height, String.valueOf(bmiData.getHieghtCm()));
        txtHeight.setText(height);

        imgBMI.setImageResource(bmiImageMap.get(bmiData.getBmiClass()));

        int backgroundColorResId = bmiColorMap.get(bmiData.getBmiClass());
        int backgroundColor = ContextCompat.getColor(getActivity(), backgroundColorResId);
        imgBMI.setBackgroundColor(backgroundColor);

        if(!Double.isNaN(bmiData.getBmiIndex()) && bmiData.getBmiIndex() < 1000)
            txtBMIIndex.setText(String.format("%.2f", bmiData.getBmiIndex()));
        else
            txtBMIIndex.setText("---");
    }


    private void saveData()
    {
        try
        {
            int weightKG = sbWeight.getProgress();
            int heightCM = sbHeigth.getProgress();

            BMICalculator.BMIData bmiData = BMICalculator.calculate(weightKG, heightCM);
            Gson gson = new Gson();
            String bmiJson = gson.toJson(bmiData);

            SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("bmiData", bmiJson);
            editor.commit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private void loadData()
    {
        SharedPreferences preferences = getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        String bmiJson = preferences.getString("bmiData", null);
        if(bmiJson != null)
        {
            Gson gson = new Gson();
            BMICalculator.BMIData bmiData = gson.fromJson(bmiJson, BMICalculator.BMIData.class);

            sbHeigth.setProgress(bmiData.getHieghtCm());
            sbWeight.setProgress(bmiData.getWeightKG());
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        if(fromUser)
            updateScreen();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        saveData();
    }
}
