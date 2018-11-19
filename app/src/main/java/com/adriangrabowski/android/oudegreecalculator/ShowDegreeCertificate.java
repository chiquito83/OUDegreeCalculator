package com.adriangrabowski.android.oudegreecalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ShowDegreeCertificate extends AppCompatActivity {


    private TextView beforeQA;
    private TextView qa;
    private TextView afterQA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_degree_certificate);

        beforeQA = (TextView) findViewById(R.id.tv_before_qa);
        qa = (TextView) findViewById(R.id.tv_quality_assurance);
        afterQA = (TextView) findViewById(R.id.tv_final_result);


        beforeQA.setText(getIntent().getStringExtra("BEFORE_QA"));
        qa.setText(getIntent().getStringExtra("QA"));
        afterQA.setText(getIntent().getStringExtra("AFTER_QA"));

    }
}
