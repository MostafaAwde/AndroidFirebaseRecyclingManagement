package com.example.recycling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        Button start_btn3 = findViewById(R.id.start_btn3);
        WebView webView = findViewById(R.id.third_page);
        start_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity4.this, Login.class);
                startActivity(intent);
            }
        });
        webView.loadUrl("file:///android_asset/third_page.html");
    }

}