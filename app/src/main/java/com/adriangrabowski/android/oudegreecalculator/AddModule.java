package com.adriangrabowski.android.oudegreecalculator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class AddModule extends AppCompatActivity {


    int level;
    int credits;
    int grade;
    String name;


    DegreeCalculator dg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);
        level = getIntent().getIntExtra("LEVEL", 0);
        dg = (DegreeCalculator) getIntent().getSerializableExtra("dg");
        credits = 30;
        grade = 2;
    }

    public void addNewModule(View view) {

        //todo perform all the checks here

        EditText nameField = (EditText) findViewById(R.id.name_text_view);


        name = nameField.getText().toString();


        if (name.length() > 0) {


            if (level == 2 && dg.getTotalCreditsLevel2() + credits <= 120) {

                dg.addModule(level, "", name, credits, grade);

            }

            if (level == 3 && dg.getTotalCreditsLevel3() + credits <= 120) {

                dg.addModule(level, "", name, credits, grade);

            }


        }


        Intent intent = new Intent(this, MainActivity.class);


        intent.putExtra("dg", dg);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        startActivity(intent);
        finish();
    }

    public void setNumberOfCredits(View view) {

        boolean checked = ((RadioButton) view).isChecked();


        switch (view.getId()) {

            case R.id.credits30:
                if (checked) {
                    credits = 30;

                }
                break;
            case R.id.credits60:
                if (checked) {
                    credits = 60;

                }


        }


    }

    public void setGrade(View view) {

        boolean checked = ((RadioButton) view).isChecked();


        switch (view.getId()) {
            case R.id.distinction:
                if (checked) {
                    grade = 1;
                }
                break;
            case R.id.pass2:
                if (checked) {
                    grade = 2;

                }
                break;
            case R.id.pass3:
                if (checked) {
                    grade = 3;

                }
                break;
            case R.id.pass4:
                if (checked) {
                    grade = 4;
                }


        }


    }

}
