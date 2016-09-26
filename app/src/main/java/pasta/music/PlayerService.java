package pasta.music;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.exoplayer.ExoPlayer;

import java.util.ArrayList;

import pasta.music.activities.PlayerActivity;
import pasta.music.data.TrackListData;

public class PlayerService extends Service {

    public static final String
            ACTION_INIT = "pasta.ACTION_INIT",
            ACTION_PLAY = "pasta.ACTION_PLAY",
            ACTION_PLAY_EXTRA_START_POS = "pasta.ACTION_PLAY_EXTRA_START_POS",
            ACTION_PLAY_EXTRA_TRACKS = "pasta.ACTION_PLAY_EXTRA_TRACKS",
            ACTION_TOGGLE = "pasta.ACTION_TOGGLE",
            ACTION_NEXT = "pasta.ACTION_NEXT",
            ACTION_PREV = "pasta.ACTION_PREV",
            ACTION_MOVE_TRACK = "pasta.ACTION_MOVE_TRACK",
            ACTION_MOVE_TRACK_EXTRA_POS = "pasta.ACTION_MOVE_TRACK_EXTRA_POS",
            ACTION_MOVE_POS = "pasta.ACTION_MOVE_POS",
            ACTION_MOVE_POS_EXTRA_POS = "pasta.ACTION_MOVE_POS_EXTRA_POS",
            STATE_UPDATE = "pasta.STATE_UPDATE",
            EXTRA_PLAYING = "pasta.EXTRA_PLAYING",
            EXTRA_CUR_POSITION = "pasta.EXTRA_CUR_POSITION",
            EXTRA_CUR_TIME = "pasta.EXTRA_CUR_TIME",
            EXTRA_MAX_TIME = "pasta.EXTRA_MAX_TIME",
            EXTRA_CUR_TRACK = "pasta.EXTRA_SONG",
            EXTRA_TRACK_LIST = "pasta.EXTRA_TRACK_LIST";

    public static final int UPDATE_INTERVAL = 500;

    private static final int NOTIFICATION_ID = 12345;

    private ArrayList<TrackListData> trackList;
    private int curPos;

    private ExoPlayer player;
    private Pasta pasta;

    @Override
    public void onCreate() {
        super.onCreate();
        pasta = (Pasta) getApplicationContext();
    }

    private void initPlayer() {

    }

    @Override
    public void onDestroy() {
        if (player != null) player.release();
        super.onDestroy();
    }

    private void onError(String message) {
        //TODO: error things
        pasta.onError(this, message);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || intent.getAction() == null) {
            pasta.onError(this, "random start command");
            return START_STICKY;
        }

        switch (intent.getAction()) {
            case ACTION_INIT:
                initPlayer();
                break;
            case ACTION_PLAY:
                //TODO: play the intent extra track in the intent extra track list
                break;
            case ACTION_TOGGLE:
                //TODO: toggle the playing state
                break;
            case ACTION_NEXT:
                //TODO: play the next track
                break;
            case ACTION_PREV:
                //TODO: play the previous track
                break;
            case ACTION_MOVE_TRACK:
                //TODO: play the intent extra track
                break;
            case ACTION_MOVE_POS:
                //TODO: move to the intent extra position
                return START_STICKY;
        }
        return START_STICKY;
    }

    private int getInfinitePos(int pos) {
        if (pos >= trackList.size()) return 0;
        else if (pos < 0) return trackList.size() - 1;
        else return pos;
    }

    private void checkForState() {
        //TODO: I can't remember what this is for but it does something
    }

    private void sendUpdateToUI() {
        TrackListData curTrack = trackList.get(curPos);

        Intent intent = new Intent(STATE_UPDATE);
        intent.putExtra(EXTRA_PLAYING, false);
        intent.putExtra(EXTRA_CUR_POSITION, curPos);
        intent.putExtra(EXTRA_CUR_TIME, 0);
        intent.putExtra(EXTRA_MAX_TIME, 0);
        intent.putExtra(EXTRA_CUR_TRACK, curTrack);
        intent.putExtra(EXTRA_TRACK_LIST, trackList);
        sendBroadcast(intent);
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        boolean vectors = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(PlayerService.this)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(trackList.get(curPos).trackName)
                .addAction(vectors ? R.drawable.ic_prev : 0, "Previous", PendingIntent.getService(getApplicationContext(), 1, new Intent(getApplicationContext(), PlayerService.class).setAction(PlayerService.ACTION_PREV), PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(vectors ? (false ? R.drawable.ic_pause : R.drawable.ic_play) : 0, false ? "Pause" : "Play", PendingIntent.getService(getApplicationContext(), 1, new Intent(getApplicationContext(), PlayerService.class).setAction(PlayerService.ACTION_TOGGLE), PendingIntent.FLAG_UPDATE_CURRENT))
                .addAction(vectors ? R.drawable.ic_next : 0, "Next", PendingIntent.getService(getApplicationContext(), 1, new Intent(getApplicationContext(), PlayerService.class).setAction(PlayerService.ACTION_NEXT), PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentIntent(PendingIntent.getActivities(PlayerService.this, 0, new Intent[]{new Intent(PlayerService.this, PlayerActivity.class)}, 0));

        if (trackList.get(curPos).artists.size() > 0)
            builder.setContentText(trackList.get(curPos).artists.get(0).artistName);

        return builder;
    }

    private void showNotification() {
        startForeground(NOTIFICATION_ID, getNotificationBuilder().build());

        Glide.with(this).load(trackList.get(curPos).trackImage).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        startForeground(NOTIFICATION_ID, getNotificationBuilder().setLargeIcon(resource).setColor(palette.getVibrantColor(Color.GRAY)).build());
                    }
                });
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
