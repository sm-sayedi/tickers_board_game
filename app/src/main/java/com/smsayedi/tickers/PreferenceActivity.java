package com.smsayedi.tickers;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class PreferenceActivity extends AppCompatActivity {

    Switch switch1, switch2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_preference);

        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
//        switch1.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.raleway_semibold));
//        switch2.setTypeface(ResourcesCompat.getFont(getApplicationContext(), R.font.raleway_semibold));

    }

    public void goBack(View view) {
        finish();
    }
}
