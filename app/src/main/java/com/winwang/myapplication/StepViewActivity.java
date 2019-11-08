package com.winwang.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StepViewActivity extends AppCompatActivity {

    private int current = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_view);
        final ScoreStepView stepView = findViewById(R.id.step_view);
        StepView st = findViewById(R.id.step_view_1);
        Button bt1 = findViewById(R.id.bt_1);
        Button bt2 = findViewById(R.id.bt_2);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current++;
                stepView.setCurrentStepWithoutAnim(current);
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current--;
                stepView.setCurrentStepWithoutAnim(current);
            }
        });
        stepView.setMaxStep(100);
        stepView.setCurrentStep(current);
        st.setMaxStep(500);
        st.setCurrentStep(400);

        startActivity(new Intent(this,ScrollerActivity.class));

    }
}
