package com.adriangrabowski.android.oudegreecalculator;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class AddModule extends AppCompatActivity {


    int level;
    int credits;
    int grade;
    String code;
    String name;
    Context context;

    EditText ed_module_code;
    EditText ed_module_name;
    RadioButton rb_number_of_credits_30;
    RadioButton rb_number_of_credits_60;
    RadioButton rb_level_2;
    RadioButton rb_level_3;

    RadioGroup rg_credits;
    RadioGroup rg_level;


    DegreeCalculator dg;

    private OUModuleDatabase ouModuleDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_module);

        ouModuleDatabase = OUModuleDatabase.getInstance(this);

        new DatabaseLoadAsync().execute();


        ed_module_code = (EditText) findViewById(R.id.code_text_view);
        ed_module_name = (EditText) findViewById(R.id.name_text_view);
        rb_number_of_credits_30 = (RadioButton) findViewById(R.id.credits30);
        rb_number_of_credits_60 = (RadioButton) findViewById(R.id.credits60);
        rb_level_2 = (RadioButton) findViewById(R.id.level2);
        rb_level_3 = (RadioButton) findViewById(R.id.level3);


        rg_credits = (RadioGroup) findViewById(R.id.credits_radio_group);
        rg_level = (RadioGroup) findViewById(R.id.level_radio_group);

        credits = 30;
        grade = 2;
        level = 2;
        name = "";


        code = getIntent().getStringExtra("CODE");

        new DownloadModuleInfoClass().execute(code);


        ed_module_code.setText(code);


        context = getApplicationContext();
    }

    public void addNewModule(View view) {


        code = ed_module_code.getText().toString().toUpperCase().replaceAll("\\s", "");

        if (code == null || code.isEmpty()) {
            Toast toast = Toast.makeText(context, R.string.toast_module_code_empty, Toast.LENGTH_LONG);
            toast.show();

            return;
        }


        OUModule moduleToBeAdded = new OUModule(code, name, credits, level, grade);


        if (dg.canAddModule(moduleToBeAdded)) {


            new DatabaseSaveModuleAsync().execute(moduleToBeAdded);


            Intent intent = new Intent(this, MainActivity.class);


            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


            startActivity(intent);
            finish();

        } else {
            Toast toast = Toast.makeText(context, R.string.toast_cant_add_module, Toast.LENGTH_LONG);
            toast.show();

        }


    }

    public void setLevel(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {

            case R.id.level2:
                if (checked) {
                    level = 2;
                }
                break;

            case R.id.level3:
                if (checked) {
                    level = 3;
                }
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

    private class DownloadModuleInfoClass extends AsyncTask<String, Void, ModuleInfo> {

        @Override
        protected ModuleInfo doInBackground(String... strings) {

            ModuleInfo moduleInfo = null;

            try {
                URL url = new URL("http://www.adriangrabowski.com:4567/oumodules/v1/modules/" + strings[0]);
                URLConnection connection = url.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder();
                String inputLine = "";

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);

                }

                in.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                moduleInfo = new ModuleInfo(jsonObject.getString("code"), jsonObject.getString("name"), jsonObject.getInt("level"), jsonObject.getInt("credits"));


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return moduleInfo;
        }

        @Override
        protected void onPostExecute(ModuleInfo moduleInfo) {
            super.onPostExecute(moduleInfo);

            if (moduleInfo == null || moduleInfo.getLevel() < 2) {
                return;
            }

            ed_module_name.setText(moduleInfo.getName());

            if (moduleInfo.getLevel() == 2) {
                rg_level.check(rb_level_2.getId());

            } else if (moduleInfo.getLevel() == 3) {
                rg_level.check(rb_level_3.getId());

            }

            if (moduleInfo.getCredits() == 30) {
                rg_credits.check(rb_number_of_credits_30.getId());
            } else if (moduleInfo.getCredits() == 60) {
                rg_credits.check(rb_number_of_credits_60.getId());
            }

            level = moduleInfo.getLevel();
            credits = moduleInfo.getCredits();
            name = moduleInfo.getName();


        }
    }


    private class DatabaseSaveModuleAsync extends AsyncTask<OUModule, Void, Void> {


        @Override
        protected Void doInBackground(OUModule... modules) {

            ouModuleDatabase.getOUModuleDAO().insert(modules[0]);


            return null;
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


        }
    }


}
