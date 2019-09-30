package com.gamatechno.egov.safetravellibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gamatechno.ggfw.utils.Functions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Functions.ToastShort(this, "ok");
    }
}
