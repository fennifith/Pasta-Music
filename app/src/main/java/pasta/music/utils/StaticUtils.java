package pasta.music.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pasta.music.PlayerService;
import pasta.music.data.TrackListData;

public class StaticUtils {

    public static boolean shouldResendRequest(Exception e) {
        //TODO: idk what to do here
        return false;
    }

    public static void restart(Context context) {
        try {
            Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            manager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, PendingIntent.getActivity(context, 196573, i, PendingIntent.FLAG_CANCEL_CURRENT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (context instanceof Activity) {
            ((Activity) context).finish();
        } else {
            System.exit(0);
        }
    }

    public static boolean isPermissionsGranted(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        if (info.requestedPermissions != null) {
            for (String permission : info.requestedPermissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.wtf("Permission", permission);
                    if (!permission.matches(Manifest.permission.SYSTEM_ALERT_WINDOW) && !permission.matches(Manifest.permission.GET_TASKS))
                        return false;
                }
            }
        }

        return true;
    }

    public static void requestPermissions(Activity activity) {
        PackageInfo info;
        try {
            info = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return;
        }

        if (info.requestedPermissions != null) {
            List<String> unrequestedPermissions = new ArrayList<>();
            for (String permission : info.requestedPermissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.wtf("Permission", permission);
                    if (permission.length() > 0 && !permission.matches(Manifest.permission.SYSTEM_ALERT_WINDOW) && !permission.matches(Manifest.permission.GET_TASKS))
                        unrequestedPermissions.add(permission);
                }
            }

            if (unrequestedPermissions.size() > 0)
                ActivityCompat.requestPermissions(activity, unrequestedPermissions.toArray(new String[unrequestedPermissions.size()]), 0);
        }
    }

    public static String timeToString(int minutes, int seconds) {
        return String.format(Locale.getDefault(), "%1$02d", minutes) + ":" + String.format(Locale.getDefault(), "%1$02d", seconds);
    }

    public static String getAlbumUrl(String id) {
        return "https://open.spotify.com/album/" + id;
    }

    public static String getPlaylistUrl(String user, String id) {
        return "https://open.spotify.com/user/" + user + "/playlist/" + id;
    }

    public static String getArtistUrl(String id) {
        return "https://open.spotify.com/artist/" + id;
    }

    public static int getStatusBarMargin(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return 0;

        int height = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) height = context.getResources().getDimensionPixelSize(resId);
        return height;
    }

    public static void play(int startPos, List<TrackListData> trackList, Context context) {
        Intent intent = new Intent(PlayerService.ACTION_PLAY);
        intent.setClass(context, PlayerService.class);
        intent.putExtra(PlayerService.ACTION_PLAY_EXTRA_START_POS, startPos);
        if (trackList instanceof ArrayList)
            intent.putParcelableArrayListExtra(PlayerService.ACTION_PLAY_EXTRA_TRACKS, (ArrayList<? extends Parcelable>) trackList);
        context.startService(intent);
    }

    public static void togglePlay(Context context) {
        Intent intent = new Intent(PlayerService.ACTION_TOGGLE);
        intent.setClass(context, PlayerService.class);
        context.startService(intent);
    }

    public static void next(Context context) {
        Intent intent = new Intent(PlayerService.ACTION_NEXT);
        intent.setClass(context, PlayerService.class);
        context.startService(intent);
    }

    public static void previous(Context context) {
        Intent intent = new Intent(PlayerService.ACTION_PREV);
        intent.setClass(context, PlayerService.class);
        context.startService(intent);
    }

    public static void jumpToPositionInTrack(int position, Context context) {
        Intent intent = new Intent(PlayerService.ACTION_MOVE_POS);
        intent.setClass(context, PlayerService.class);
        intent.putExtra(PlayerService.ACTION_MOVE_POS_EXTRA_POS, position);
        context.startService(intent);
    }

}
