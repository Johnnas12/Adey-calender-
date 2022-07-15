package com.androidcalenderproject.ethiocalendar;

 import android.os.Bundle;

 import androidx.appcompat.app.AppCompatActivity;

 public class MainActivity extends AppCompatActivity {
     EthioCalendarView ethioCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ethioCalendarView=(EthioCalendarView)findViewById(R.id.Ethio_Calendar_View);
    }
}