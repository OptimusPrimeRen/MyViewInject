package com.optimus.myviewinject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.optimus.view.viewinject.ContentView;
import com.optimus.view.viewinject.OnClick;
import com.optimus.view.viewinject.ViewInject;
import com.optimus.view.viewinject.ViewInjectUtils;


@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.button)
    private Button button;
    @ViewInject(R.id.button2)
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectUtils.inject(this);
    }

    @OnClick({R.id.button,R.id.button2})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.button:Toast.makeText(this,"The First Button",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button2:Toast.makeText(this,"The Second Button",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
