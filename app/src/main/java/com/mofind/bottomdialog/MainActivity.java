package com.mofind.bottomdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;

import com.mofind.widget.BottomDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnOnClick1(View v) {
        new BottomDialog(this).setTitle("Title").setDefTv("hello mofind").setDefHint("lalala").show();
    }

    public void btnOnClick2(View v) {
        new BottomDialog(this).setTitle("Title").setAdapterData(new String[] {"item1", "item2", "item3" }, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }, 0).show();
    }
}
