package com.hanadkubat.kuvalefitness;

public class BMICalculator
{
    public static enum BMIClass
    {
        Underweight,
        Normal,
        Overweight,
        ObeseClass1,
        ObeseClass2,
        ObeseClass3
    }

    public static class BMIData
    {
        private int weightKG;
        private int hieghtCm;
        private double bmiIndex;
        private BMIClass bmiClass;

        BMIData()
        {
        }

        public double getBmiIndex()
        {
            return bmiIndex;
        }

        public BMIClass getBmiClass()
        {
            return bmiClass;
        }

        public int getWeightKG()
        {
            return weightKG;
        }

        public int getHieghtCm()
        {
            return hieghtCm;
        }
    }



    public static BMIData calculate(int weightKG, int heightCM)
    {
        BMIData bmiData = new BMIData();
        bmiData.weightKG = weightKG;
        bmiData.hieghtCm = heightCM;
        bmiData.bmiIndex = weightKG / (heightCM / 100.0 * heightCM / 100.0);

        if(bmiData.bmiIndex < 18.5)
            bmiData.bmiClass = BMIClass.Underweight;
        else if(bmiData.bmiIndex < 25.0)
            bmiData.bmiClass = BMIClass.Normal;
        else if(bmiData.bmiIndex < 30.0)
            bmiData.bmiClass = BMIClass.Overweight;
        else if(bmiData.bmiIndex < 35.0)
            bmiData.bmiClass = BMIClass.ObeseClass1;
        else if(bmiData.bmiIndex < 40.0)
            bmiData.bmiClass = BMIClass.ObeseClass2;
        else
            bmiData.bmiClass = BMIClass.ObeseClass3;

        return bmiData;
    }
}
