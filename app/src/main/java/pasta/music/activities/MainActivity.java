package pasta.music.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.afollestad.async.Action;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pasta.music.Pasta;
import pasta.music.PlayerService;
import pasta.music.R;
import pasta.music.data.TrackListData;

public class MainActivity extends AppCompatActivity {

    Pasta pasta;
    SharedPreferences prefs;
    Action fetchTracks=new Action() {
        @NonNull
        @Override
        public String id() {
            return "fetch_songs";
        }

        @Nullable
        @Override
        protected Object run() throws InterruptedException {
            pasta.setUp();
            return null;
        }

        @Override
        protected void done(@Nullable Object result) {
            //do something
        }
    };
    @Bind(R.id.start)
    View start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        pasta = (Pasta) getApplicationContext();
        pasta.setScreen(this);
        //We check the permission before fetching tracks
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            //We have permission
            fetchTracks.execute();
        }
        else
        {
         //We don't have permission
            Log.d("Permission","We don't have READ_EXTERNAL_STORAGE permission");
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean("first_run",true)) {
            prefs.edit().putBoolean("first_run", false).apply();
            startActivity(new Intent(MainActivity.this, IntroActivity.class));
            start.setVisibility(View.VISIBLE);
        } else openRequest();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case 1:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    Log.d("Permission","Permission was granted");
                    fetchTracks.execute();


                } else {

                    // permission denied
                    Log.d("Permission","Didn't get the READ_EXTERNAL_STORAGE permission. Can't fetch songs");

                }
                return;
            }
        }
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
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);


                } else {


                    Log.d("Explanation","No explanation needed");
                    ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);


                }
            }
        }



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
