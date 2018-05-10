package com.hanadkubat.kuvalefitness.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

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

    private Map<BMICalculator.BMIClass, Integer> bmiImageMap;

    public BMIFragment()
    {
        bmiImageMap = new HashMap<>();
        bmiImageMap.put(BMICalculator.BMIClass.Underweight, R.drawable.underweight);
        bmiImageMap.put(BMICalculator.BMIClass.Normal, R.drawable.normal);
        bmiImageMap.put(BMICalculator.BMIClass.Overweight, R.drawable.overweight);
        bmiImageMap.put(BMICalculator.BMIClass.ObeseClass1, R.drawable.obese1);
        bmiImageMap.put(BMICalculator.BMIClass.ObeseClass2, R.drawable.obese2);
        bmiImageMap.put(BMICalculator.BMIClass.ObeseClass3, R.drawable.obese3);
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

        sbWeight.setOnSeekBarChangeListener(this);
        sbHeigth.setOnSeekBarChangeListener(this);

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
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        updateScreen();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {

    }
}
