package pasta.music.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pasta.music.Pasta;
import pasta.music.PlayerService;
import pasta.music.R;

public class MainActivity extends AppCompatActivity {

    Pasta pasta;
    SharedPreferences prefs;

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

        if (prefs.getBoolean("first_time", true)) {
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
            start.setVisibility(View.VISIBLE);
        } else openRequest();
    }

    @OnClick(R.id.start)
    public void firstStart() {
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
