package com.adriangrabowski.android.oudegreecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    DegreeCalculator dg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DegreeCalculator d = (DegreeCalculator) getIntent().getSerializableExtra("dg");

        if (d == null) {

            dg = new DegreeCalculator();

        } else {
            dg = d;
        }

        displayListOfModules2();
        displayListOfModules3();
        displayResult();
        updateCreditCounter();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void clearAll(View view) {
        dg.getLevel2modules().clear();
        dg.getLevel3modules().clear();
        recreate();
    }

    private void updateCreditCounter() {

        int level2Credits = dg.getTotalCreditsLevel2();
        int level3Credits = dg.getTotalCreditsLevel3();

        String messageToDisplayLevel2 = level2Credits + "/120 credits";
        String messageToDisplayLevel3 = level3Credits + "/120 credits";

        TextView v2 = (TextView) findViewById(R.id.credit_counter_2_text_view);
        TextView v3 = (TextView) findViewById(R.id.credit_counter_3_text_view);

        v2.setText(messageToDisplayLevel2);
        v3.setText(messageToDisplayLevel3);

    }

    public void addLevel2Module(View view) {
        Intent intent = new Intent(this, AddModule.class);

        int level = 2;
        intent.putExtra("LEVEL", level);
        intent.putExtra("dg", dg);
        startActivity(intent);


    }

    public void addLevel3Module(View view) {
        Intent intent = new Intent(this, AddModule.class);

        int level = 3;
        intent.putExtra("LEVEL", level);
        intent.putExtra("dg", dg);
        startActivity(intent);

    }

    private void displayListOfModules2() {

        TextView v = (TextView) findViewById(R.id.level_2_modules_text_view);

        for (OUModule module : dg.getLevel2modules()
                ) {
            v.append(module.toString());


        }
    }

    private void displayListOfModules3() {

        TextView v = (TextView) findViewById(R.id.level_3_modules_text_view);

        for (OUModule module : dg.getLevel3modules()
                ) {
            v.append(module.toString());


        }
    }

    private void displayResult() {
        TextView v = (TextView) findViewById(R.id.degree_class_text_view);
        String coh = "";
        String qa = "";
        String result;

        if (dg.getTotalCreditsLevel2() == 120 && dg.getTotalCreditsLevel3() == 120) {
            coh = displayDegreeClassBeforeQualityAssurance();
            qa = displayQualityAssurance();

        }


        result = "Result before Quality Assurance: \n" +
                "" + coh + " \nQuality Assurance: \n" +
                qa + "\n\nYour final Class of Honours: \n\n" +
                displayFinal().toUpperCase();


        v.setText(result);

    }

    private String displayQualityAssurance() {
        int qa = dg.calculateQualityAssurance();

        String messageToDisplay = "";


        if (qa == 1) {
            messageToDisplay = "Distinction";
        } else if (qa == 2) {
            messageToDisplay = "Upper Second 2:1";
        } else if (qa == 3) {
            messageToDisplay = "Lower Second 2:2";
        } else if (qa == 4) {
            messageToDisplay = "Third Class";
        } else {
            messageToDisplay = " . . . ";
        }


        return messageToDisplay;
    }

    private String displayFinal() {


        String messageToDisplay = "";

        if (dg.getTotalCreditsLevel3() >= 120 && dg.getTotalCreditsLevel2() >= 120) {

            int finalClass = dg.calculateFinalClassOfHonours();

            if (finalClass == 1) {
                messageToDisplay = "Distinction";
            } else if (finalClass == 2) {
                messageToDisplay = "Upper Second 2:1";
            } else if (finalClass == 3) {
                messageToDisplay = "Lower Second 2:2";
            } else if (finalClass == 4) {
                messageToDisplay = "Third Class";
            } else {
                messageToDisplay = " . . . ";
            }

        }


        return messageToDisplay;
    }

    private String displayDegreeClassBeforeQualityAssurance() {


        int coh = dg.calculateClassOfHonours();
        String messageToDisplay = "";

        if (dg.getTotalCreditsLevel2() == 120 && dg.getTotalCreditsLevel3() == 120) {

            if (coh == 1) {
                messageToDisplay = "Distinction";
            } else if (coh == 2) {
                messageToDisplay = "Upper Second 2:1";
            } else if (coh == 3) {
                messageToDisplay = "Lower Second 2:2";
            } else if (coh == 4) {
                messageToDisplay = "Third Class";
            } else {
                messageToDisplay = " . . . ";
            }


        }

        return messageToDisplay;


    }


}
