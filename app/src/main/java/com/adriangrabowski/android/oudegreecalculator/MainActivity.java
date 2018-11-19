package com.adriangrabowski.android.oudegreecalculator;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DegreeCalculator dg;
    private OUModuleDatabase ouModuleDatabase;

    private ListView moduleListView;
    private ModuleAdapter moduleAdapter;

    private ArrayList<OUModule> moduleArrayList;


    private FloatingActionButton fab;

    private ProgressBar progressBar2;
    private ProgressBar progressBar3;

    private Button resultButton;


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar) findViewById(R.id.progressBar3);
        resultButton = (Button) findViewById(R.id.button_result);






        ouModuleDatabase = OUModuleDatabase.getInstance(this);

        moduleListView = (ListView) findViewById(R.id.lv_listofallmodules);

        context = this;


        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new InputModuleCodeDialog();
                dialog.show(getSupportFragmentManager(), "InputModuleCodeDialog");
            }
        });


        dg = new DegreeCalculator();

        new DatabaseLoadAsync().execute();



        refreshList();

        refreshAll();


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void refreshList() {
        moduleArrayList = (ArrayList<OUModule>) dg.getAllModules();
        moduleAdapter = new ModuleAdapter(context, moduleArrayList);

        moduleListView.setAdapter(moduleAdapter);

        Collections.sort(moduleArrayList);

        moduleAdapter.notifyDataSetChanged();

    }

    private void refreshAll() {

        displayResult();
        updateCreditCounter();
        refreshProgressBars();
        refreshResultButton();

    }

    private void refreshResultButton() {

        resultButton.setText(displayFinal());

        if ((dg.getTotalCreditsLevel3() + dg.getTotalCreditsLevel2()) == 240) {
            resultButton.setVisibility(View.VISIBLE);
        } else {
            resultButton.setVisibility(View.GONE);
        }

    }

    private void refreshProgressBars() {

        int progress2 = (int) ((dg.getTotalCreditsLevel2() / 120F) * 100);
        int progress3 = (int) ((dg.getTotalCreditsLevel3() / 120F) * 100);

        progressBar2.setProgress(progress2);
        progressBar3.setProgress(progress3);

    }

    void removeModule(OUModule moduleToRemove) {
        dg.removeModule(moduleToRemove);
        OUModule[] moduleArray = new OUModule[dg.getAllModules().size()];

        new DatabaseSaveAsync().execute(dg.getAllModules().toArray(moduleArray));
        //refreshList(); //todo just checking
        //refreshAll();
        //moduleAdapter.notifyDataSetChanged(); //todo this one too
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onPause() {
        super.onPause();

        OUModule[] moduleArray = new OUModule[dg.getAllModules().size()];

        new DatabaseSaveAsync().execute(dg.getAllModules().toArray(moduleArray));
    }



    private void updateCreditCounter() {

        int level2Credits = dg.getTotalCreditsLevel2();
        int level3Credits = dg.getTotalCreditsLevel3();

        String messageToDisplayLevel2 = level2Credits + "/120 credits";
        String messageToDisplayLevel3 = level3Credits + "/120 credits";


    }


    public void displayResultInSeparateActivity(View view) {
        Intent intent = new Intent(this, ShowDegreeCertificate.class);

        intent.putExtra("BEFORE_QA", displayDegreeClassBeforeQualityAssurance());
        intent.putExtra("QA", displayQualityAssurance());
        intent.putExtra("AFTER_QA", displayFinal());
        startActivity(intent);
    }

    public void removeModuleOnClickHandler(View view) {


    }

    private void displayResult() {
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

    public DegreeCalculator getDg() {
        return dg;
    }

    public static class InputModuleCodeDialog extends DialogFragment {


        private Map<String, ModuleInfo> infoMap;

        public void setInfoMap(Map<String, ModuleInfo> infoMap) {
            this.infoMap = infoMap;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();

            View view = inflater.inflate(R.layout.dialog_input_module_code, null);

            final EditText editTextInputModuleCode = (EditText) view.findViewById(R.id.dialog_input_text_edit);


            builder.setView(view)
                    .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            // find module by code and go to add module activity

                            String moduleCode = editTextInputModuleCode.getText().toString();


                            Intent intent = new Intent(getContext(), AddModule.class);
                            intent.putExtra("CODE", moduleCode);

                            startActivity(intent);


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            InputModuleCodeDialog.this.getDialog().cancel();
                        }
                    });

            return builder.create();

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

            for (OUModule module : listofmodules) {

            }

            refreshList();

            refreshAll();


        }
    }

    private class DatabaseSaveAsync extends AsyncTask<OUModule, Void, Void> {


        @Override
        protected Void doInBackground(OUModule... modules) {

            ouModuleDatabase.getOUModuleDAO().deleteAll();


            for (int i = 0; i < modules.length; i++) {
                ouModuleDatabase.getOUModuleDAO().insert(modules[i]);
            }

            return null;
        }

        @Override //todo test this one
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            refreshList();
            refreshAll();
            moduleAdapter.notifyDataSetChanged();

            Log.i("MMM", dg.toString());


        }
    }
}
