package com.adriangrabowski.android.oudegreecalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class AddModule extends AppCompatActivity {


    int level;
    int credits;
    int grade;
    String name;
    Context context;


    DegreeCalculator dg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);
        level = getIntent().getIntExtra("LEVEL", 0);
        dg = (DegreeCalculator) getIntent().getSerializableExtra("dg");
        credits = 30;
        grade = 2;
        context = getApplicationContext();
    }

    public void addNewModule(View view) {



        EditText nameField = (EditText) findViewById(R.id.name_text_view);


        name = nameField.getText().toString();
        boolean isNewModuleAdded = false;


        if (name.length() > 0) {


            if (level == 2 && dg.getTotalCreditsLevel2() + credits <= 120) {

                dg.addModule(level, "", name, credits, grade);
                isNewModuleAdded = true;

            }


            if (level == 3 && dg.getTotalCreditsLevel3() + credits <= 120) {

                dg.addModule(level, "", name, credits, grade);
                isNewModuleAdded = true;

            }

            if (!isNewModuleAdded) {
                Toast.makeText(context, R.string.toast_too_many_credits, Toast.LENGTH_LONG).show();
            }


            Intent intent = new Intent(this, MainActivity.class);


            intent.putExtra("dg", dg);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


            startActivity(intent);
            finish();


        } else {
            Toast.makeText(context, R.string.toast_module_code_empty, Toast.LENGTH_LONG).show();
        }



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
