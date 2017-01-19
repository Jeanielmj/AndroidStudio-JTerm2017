package com.example.demouser.jterm2017_hello;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private static Map<String, String> countryCodes = new HashMap<>();

    static{
        countryCodes.put("GBR", "United Kingdom");
        countryCodes.put("IDN", "Indonesia");
        countryCodes.put("IND", "India");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.lookupButton)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                lookUp();
            }
        });
    }

    public void lookUp(){
        String code = ((EditText)findViewById(R.id.editText)).getText().toString().toUpperCase();
        String country ;

        if(countryCodes.containsKey(code)){
            country = countryCodes.get(code);
        }else{
            country = code;
        }

        ((TextView)findViewById(R.id.countryCode)).setText(country);
    }
}
