package com.example.andaztask.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.andaztask.R;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView alreadyAccount;
    private TextView termsAndConditons;
    String html="<font color=#000000>Already have an Account? </font> <a href><font color=#EE3F24>Log in</font><a/>";
    String termshtml="<font color=#000000>Agree with our </font> <a href><font color=#EE3F24>terms &amp; conditions</font><a/>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.back_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   onBackPressed();
            }
        });
        alreadyAccount=findViewById(R.id.already_account);
        alreadyAccount.setText(Html.fromHtml(html));
        termsAndConditons=findViewById(R.id.textView3);
        termsAndConditons.setText(Html.fromHtml(termshtml));

    }
}
