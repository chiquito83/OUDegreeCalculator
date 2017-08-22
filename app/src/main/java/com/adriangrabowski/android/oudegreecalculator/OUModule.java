package com.adriangrabowski.android.oudegreecalculator;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Adrian on 14/08/2017.
 */

public class OUModule implements Serializable, Comparable<OUModule> {
    private String moduleCode;  // for example M250
    private String moduleName;
    private int numberOfCredits; // usually 30 or 60
    private int level; // 2 or 3
    private int gradeOfPass;  // 1 - Distinction, 2 - grade 2 pass, etc..

    public OUModule(String code, String name, int credits, int lvl, int grade) {
        moduleCode = code;
        moduleName = name;
        numberOfCredits = credits;
        level = lvl;
        gradeOfPass = grade;
    }

    public int getWeightedGradeCredits() {
        int multiplier = 1;

        if (level == 3) {
            multiplier = 2;
        }

        return numberOfCredits * multiplier * gradeOfPass;


    }

    private String getGradeOfPassStringRepresentation() {
        if (gradeOfPass == 1) {
            return "Distinction";
        } else if (gradeOfPass == 2) {
            return "Pass 2";
        } else if (gradeOfPass == 3) {
            return "Pass 3";
        } else {
            return "Pass 4";
        }
    }


    public String toString() {
        return moduleName + ", credits: " + numberOfCredits + ", grade of pass: " + getGradeOfPassStringRepresentation() + "\n";
    }


    @Override
    public int compareTo(@NonNull OUModule ouModule) {
        return getGradeOfPass() - ouModule.getGradeOfPass();
    }

    public int getNumberOfCredits() {
        return numberOfCredits;
    }

    public int getGradeOfPass() {
        return gradeOfPass;
    }
}
