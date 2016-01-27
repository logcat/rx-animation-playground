package net.logcat.discountasciiwarehouse;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN_FRAGMENT = "MainFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(R.id.container, new MainFragment(), MAIN_FRAGMENT).commit();
        }
    }
}
