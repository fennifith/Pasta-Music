package pasta.music.adapters;

import android.app.Activity;
import android.os.Bundle;
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
import pasta.music.data.ArtistListData;
import pasta.music.data.PlaylistListData;
import pasta.music.data.TrackListData;
import pasta.music.fragments.OmniFragment;

public class FavoritePagerAdapter extends FragmentStatePagerAdapter {

    Activity activity;
    Pasta pasta;

    OmniFragment playlistFragment;
    OmniFragment albumFragment;
    OmniFragment trackFragment;
    OmniFragment artistFragment;

    public FavoritePagerAdapter(Activity activity, FragmentManager manager) {
        super(manager);
        this.activity = activity;
        pasta = (Pasta) activity.getApplicationContext();

        Bundle args = new Bundle();
        args.putBoolean("favorite", true);

        playlistFragment = new OmniFragment();
        playlistFragment.setArguments(args);

        albumFragment = new OmniFragment();
        albumFragment.setArguments(args);

        trackFragment = new OmniFragment();
        trackFragment.setArguments(args);

        artistFragment = new OmniFragment();
        artistFragment.setArguments(args);

        load();
    }

    public void load() {
        playlistFragment.clear();
        albumFragment.clear();
        trackFragment.clear();
        artistFragment.clear();

        new Action<List<PlaylistListData>>() {
            @NonNull
            @Override
            public String id() {
                return "getFavPlaylists";
            }

            @Nullable
            @Override
            protected List<PlaylistListData> run() throws InterruptedException {
                return pasta.getFavoritePlaylists();
            }

            @Override
            protected void done(@Nullable List<PlaylistListData> result) {
                if (result == null) {
                    pasta.onError(activity, "favorite playlist action");
                    return;
                }
                playlistFragment.swapData(result);
            }
        }.execute();

        new Action<List<AlbumListData>>() {
            @NonNull
            @Override
            public String id() {
                return "getFavAlbums";
            }

            @Nullable
            @Override
            protected List<AlbumListData> run() throws InterruptedException {
                return pasta.getFavoriteAlbums();
            }

            @Override
            protected void done(@Nullable List<AlbumListData> result) {
                if (result == null) {
                    pasta.onError(activity, "favorite album action");
                    return;
                }
                albumFragment.swapData(result);
            }
        }.execute();

        new Action<List<TrackListData>>() {
            @NonNull
            @Override
            public String id() {
                return "getFavTracks";
            }

            @Nullable
            @Override
            protected List<TrackListData> run() throws InterruptedException {
                return pasta.getFavoriteTracks();
            }

            @Override
            protected void done(@Nullable List<TrackListData> result) {
                if (result == null) {
                    pasta.onError(activity, "favorite track action");
                    return;
                }
                trackFragment.swapData(result);
            }
        }.execute();

        new Action<List<ArtistListData>>() {
            @NonNull
            @Override
            public String id() {
                return "getFavArtists";
            }

            @Nullable
            @Override
            protected List<ArtistListData> run() throws InterruptedException {
                return pasta.getFavoriteArtists();
            }

            @Override
            protected void done(@Nullable List<ArtistListData> result) {
                if (result == null) {
                    pasta.onError(activity, "favorite artist action");
                    return;
                }
                artistFragment.swapData(result);
            }
        }.execute();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return playlistFragment;
            case 1:
                return albumFragment;
            case 2:
                return trackFragment;
            case 3:
                return artistFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Playlists";
            case 1:
                return "Albums";
            case 2:
                return "Songs";
            case 3:
                return "Artists";
            default:
                return null;
        }
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();

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
