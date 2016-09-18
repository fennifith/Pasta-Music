package pasta.music.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.async.Action;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pasta.music.Pasta;
import pasta.music.PlayerService;
import pasta.music.R;
import pasta.music.utils.StaticUtils;

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
        if (prefs.getBoolean("first_run",true)) {
            prefs.edit().putBoolean("first_run", false).apply();
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
            start.setVisibility(View.VISIBLE);
        } else openRequest();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        openRequest();
    }

    @OnClick(R.id.start)
    public void firstStart() {
        openRequest();
    }

    private void openRequest() {
        if (StaticUtils.isPermissionsGranted(this)) {
            new Action()
            {
                @NonNull
                @Override
                public String id() {
                    return "fetch_artists";
                }

                @Nullable
                @Override
                protected Object run() throws InterruptedException {
                    pasta.setUpArtists();
                    return null;
                }
            }.execute();
            new Action()
            {
                @NonNull
                @Override
                public String id() {
                    return "fetch_albums";
                }

                @Nullable
                @Override
                protected Object run() throws InterruptedException {
                    pasta.setUpAlbums();
                    return null;
                }
            }.execute();
            new Action() {
                @NonNull
                @Override
                public String id() {
                    return "fetch_songs";
                }

                @Nullable
                @Override
                protected Object run() throws InterruptedException {
                    pasta.setUpSongs();
                    return null;
                }

                @Override
                protected void done(@Nullable Object result) {
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
            }.execute();
        } else StaticUtils.requestPermissions(this);
    }
}
