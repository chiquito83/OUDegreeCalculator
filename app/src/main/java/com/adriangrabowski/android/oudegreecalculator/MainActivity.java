package com.adriangrabowski.android.oudegreecalculator;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DegreeCalculator dg;
    OUModuleDatabase ouModuleDatabase;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ouModuleDatabase = OUModuleDatabase.getInstance(this);

        context = this;


        DegreeCalculator d = (DegreeCalculator) getIntent().getSerializableExtra("dg");

        if (d == null) {


            dg = new DegreeCalculator();

            new DatabaseLoadAsync().execute();

        } else {
            dg = d;
        }


        refreshAll();


    }

    private void refreshAll() {
        displayListOfModules2();
        displayListOfModules3();
        displayResult();
        updateCreditCounter();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putSerializable("DG", dg);
    }

    @Override
    protected void onPause() {
        super.onPause();

        OUModule[] moduleArray = new OUModule[dg.getAllModules().size()];

        new DatabaseSaveAsync().execute(dg.getAllModules().toArray(moduleArray));
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


        final LinearLayout parentLayout = (LinearLayout) findViewById(R.id.level_2_modules_list);
        parentLayout.removeAllViews();

        for (final OUModule module : dg.getLevel2modules()
                ) {

            TextView tv = new TextView(this);
            tv.setText(module.toString());


            LinearLayout moduleLayout = new LinearLayout(this);
            moduleLayout.setOrientation(LinearLayout.HORIZONTAL);
            moduleLayout.setTag(module);
            moduleLayout.addView(tv);


            Button removeButton = new Button(this);
            removeButton.setText("Remove");
            removeButton.setTag(module);


            moduleLayout.addView(removeButton);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dg.removeModule(module);


                    parentLayout.removeView(parentLayout.findViewWithTag(module));

                    displayResult();
                    updateCreditCounter();
                }
            });

            parentLayout.addView(moduleLayout);


        }



    }

    private void displayListOfModules3() {

        final LinearLayout parentLayout = (LinearLayout) findViewById(R.id.level_3_modules_list);
        parentLayout.removeAllViews();

        for (final OUModule module : dg.getLevel3modules()
                ) {

            TextView tv = new TextView(this);
            tv.setText(module.toString());


            LinearLayout moduleLayout = new LinearLayout(this);
            moduleLayout.setOrientation(LinearLayout.HORIZONTAL);
            moduleLayout.setTag(module);
            moduleLayout.addView(tv);
            moduleLayout.setPadding(1, 1, 1, 1);


            Button removeButton = new Button(this);
            removeButton.setText("Remove");
            removeButton.setTag(module);


            moduleLayout.addView(removeButton);

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dg.removeModule(module);


                    parentLayout.removeView(parentLayout.findViewWithTag(module));

                    displayResult();
                    updateCreditCounter();
                }
            });

            parentLayout.addView(moduleLayout);


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
                qa + "\nYour final Class of Honours: \n" +
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

    public void saveData(View view) {

        String fileName = "dg.dat";


        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        }
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(fos);
            os.writeObject(dg);
            os.close();
            fos.close();
        } catch (IOException e) {
            //e.printStackTrace();
        }


    }

    public void loadData(View view) {
        String fileName = "dg.dat";

        FileInputStream fis = null;
        try {
            fis = openFileInput(fileName);
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
        }

        if (fis != null) {

            ObjectInputStream is = null;
            try {
                is = new ObjectInputStream(fis);
            } catch (IOException e) {
                //e.printStackTrace();
            }
            try {
                dg = (DegreeCalculator) is.readObject();
            } catch (IOException e) {
                //e.printStackTrace();
            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
            }
            try {
                is.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            refreshAll();


        }


    }

    private class DatabaseLoadAsync extends AsyncTask<Void, Void, List<OUModule>> {

        @Override
        protected List<OUModule> doInBackground(Void... voids) {


            List<OUModule> listofmodules = ouModuleDatabase.getOUModuleDAO().getAllModules();


            return listofmodules;
        }

        @Override
        protected void onPostExecute(List<OUModule> listofmodules) {

            dg = new DegreeCalculator(listofmodules);
            refreshAll();


        }
    }

    private class DatabaseSaveAsync extends AsyncTask<OUModule, Void, Void> {


        @Override
        protected Void doInBackground(OUModule... modules) {


            for (int i = 0; i < modules.length; i++) {
                ouModuleDatabase.getOUModuleDAO().insert(modules[i]);
            }

            return null;
        }
    }


}
