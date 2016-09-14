package pasta.music.adapters;

import android.app.Activity;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.afollestad.async.Action;

import java.util.List;

import pasta.music.Pasta;
import pasta.music.data.AlbumListData;
import pasta.music.data.PlaylistListData;
import pasta.music.fragments.OmniFragment;

public class HomePagerAdapter extends FragmentStatePagerAdapter {

    Activity activity;
    Pasta pasta;

    OmniFragment albumFragment;
    OmniFragment playlistFragment;

    public HomePagerAdapter(final Activity activitiy, final FragmentManager manager) {
        super(manager);
        this.activity = activitiy;
        pasta = (Pasta) activity.getApplicationContext();

        albumFragment = new OmniFragment();
        playlistFragment = new OmniFragment();

        new Action<List<AlbumListData>>() {
            @NonNull
            @Override
            public String id() {
                return "getNewReleases";
            }

            @Nullable
            @Override
            protected List<AlbumListData> run() throws InterruptedException {
                return pasta.getNewAlbums();
            }

            @Override
            protected void done(@Nullable List<AlbumListData> result) {
                if (result == null) {
                    pasta.onCriticalError(activity, "new releases action");
                    return;
                }

                albumFragment.addData(result);
            }
        }.execute();

        new Action<List<PlaylistListData>>() {
            @NonNull
            @Override
            public String id() {
                return "getFeaturedPlaylists";
            }

            @Nullable
            @Override
            protected List<PlaylistListData> run() throws InterruptedException {
                return pasta.getFeaturedPlaylists();
            }

            @Override
            protected void done(@Nullable List<PlaylistListData> result) {
                if (result == null) {
                    pasta.onCriticalError(activity, "featured playlists action");
                    return;
                }
                playlistFragment.swapData(result);
                notifyDataSetChanged();
            }
        }.execute();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return albumFragment;
            case 1:
                return playlistFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "New Releases";
            case 1:
                return "Featured";
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }
}
