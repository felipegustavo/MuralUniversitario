package com.muraluniversitario;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;

public class AboutActivity extends BaseActivity {

    @Override
    protected void create(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        ViewStub stub = (ViewStub) findViewById(R.id.view_stub);
        stub.setLayoutResource(R.layout.content_about);
        View inflated = stub.inflate();
    }

}
