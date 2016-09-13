package pasta.streamer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pasta.streamer.Pasta;
import pasta.streamer.PlayerService;
import pasta.streamer.R;

public class MainActivity extends AppCompatActivity {

    Pasta pasta;
    SharedPreferences prefs;
    boolean firstrun;

    @Bind(R.id.start)
    View start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        pasta = (Pasta) getApplicationContext();
        pasta.setScreen(this);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Thread checkfirsttime=new Thread(new Runnable() {
            @Override
            public void run() {
                firstrun=prefs.getBoolean("first_time",true);
            }
        });
        checkfirsttime.start();

        if (prefs.getBoolean("first_time", true)) {
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
            start.setVisibility(View.VISIBLE);
        } else openRequest();
    }

    @OnClick(R.id.start)
    public void firstStart() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //We ask for read-write permission
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Log.d("Explanation","Explanation needed");
                    //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);


                } else {


                    Log.d("Explanation","No explanation needed");
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);


                }
            }
        }
        prefs.edit().putBoolean("first_time", false).apply();
        openRequest();
    }

    private void openRequest() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(PlayerService.ACTION_INIT);
                intent.setClass(MainActivity.this, PlayerService.class);
                startService(intent);

                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        }, 1500);
    }
}
