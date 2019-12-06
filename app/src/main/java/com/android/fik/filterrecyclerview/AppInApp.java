package com.android.fik.filterrecyclerview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class AppInApp extends AppCompatActivity {

    Button ippb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_in_app);
        init();
    }

    public void init(){
        ippb = (Button)findViewById(R.id.ippb);
        ippb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent browserIntent  = new Intent(Intent.ACTION_VIEW,Uri.parse("ippb://com.vsoft.onviewmobilebranchdeposit.ippb.pro.screens.DepositCheckActivity?agentId="+"1235"+"&facilityId="+"jgrr"+"&solId="+"jfnjg"+"&doorstepFlag="+"jfhjfj"+"&clientSessionId="+"hfjfhf"));
               startActivity(browserIntent);
            }
        });
    }

}
