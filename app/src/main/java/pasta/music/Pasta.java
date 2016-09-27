package pasta.music;

import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pasta.music.data.AlbumListData;
import pasta.music.data.ArtistListData;
import pasta.music.data.CategoryListData;
import pasta.music.data.PlaylistListData;
import pasta.music.data.TrackListData;
import pasta.music.dialogs.ErrorDialog;
import pasta.music.utils.PreferenceUtils;

public class Pasta extends Application {

    private ErrorDialog errorDialog;

    private List<TrackListData> tracks;
    private List<AlbumListData> albums;
    private List<PlaylistListData> playlists;
    private List<ArtistListData> artists;
    private ContentResolver musicResolver;

    @Override
    public void onCreate() {
        super.onCreate();
        musicResolver = getContentResolver();
        tracks = new ArrayList<>();
        albums = new ArrayList<>();
        playlists = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public void setUpArtists() {
        Cursor artistCursor = musicResolver.query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Artists.ARTIST,
                        MediaStore.Audio.Artists.ARTIST_KEY
                }, null, null,
                MediaStore.Audio.Artists.ARTIST + " ASC"
        );

        if (artistCursor != null && artistCursor.moveToFirst()) {
            do {
                String artistKey = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY));
                String artistName = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                Log.d("Artist", "Found new Artist " + artistName + " key= " + artistKey);
                artists.add(new ArtistListData(artistName, String.valueOf(artistKey)));
            } while (artistCursor.moveToNext());
            artistCursor.close();
        }
    }

    public void setUpAlbums() {
        Cursor albumCursor = musicResolver.query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Audio.Albums.ARTIST,
                        MediaStore.Audio.Albums._ID,
                        MediaStore.Audio.Albums.ALBUM_ART,
                        MediaStore.Audio.Albums.LAST_YEAR,
                        MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                        MediaStore.Audio.Albums.ALBUM}, null, null,
                MediaStore.Audio.Albums.ALBUM + " ASC");

        if (albumCursor != null && albumCursor.moveToFirst()) {
            do {
                String albumDate = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.LAST_YEAR));
                String albumArt = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                long albumId = albumCursor.getLong(albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String albumName = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
               int numSongs = albumCursor.getInt(albumCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
                Log.d("Albums", "Found new album " + albumName + " ID= " + albumId + " albumArt " + albumArt + " Date= " + albumDate);
                albums.add(new AlbumListData(String.valueOf(albumId), albumName, albumDate, albumArt,numSongs));
            } while (albumCursor.moveToNext());
            albumCursor.close();
        }
    }

    public void setUpSongs() {
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);

            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                Uri trackUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, thisId);

                tracks.add(new TrackListData(thisTitle, thisAlbum, trackUri.toString()));
            } while (musicCursor.moveToNext());
        }

        if (musicCursor != null) {
            musicCursor.close();
        }
    }

    public void setScreen(Context context) {
        //TODO: weird analytics things
    }

    public void onCriticalError(final Context context, String message) {
        if (errorDialog == null || !errorDialog.isShowing()) {
            String errorMessage = getString(R.string.error_msg);
            if (PreferenceUtils.isDebug(this))
                errorMessage += "\n\nError: " + message + "\nLocation: " + context.getClass().getName();

            errorDialog = new ErrorDialog(context).setMessage(errorMessage);
            errorDialog.show();
        }
    }

    public void onError(Context context, String message) {
        String toastMessage = getString(R.string.error);
        if (PreferenceUtils.isDebug(this))
            toastMessage += "\n\nError: " + message + "\nLocation: " + context.getClass().getName();

        showToast(toastMessage);
    }

    public void showToast(String message) {
        Toast toast = new Toast(this);

        View snackbar = LayoutInflater.from(this).inflate(R.layout.snackbar_layout, null);
        ((TextView) snackbar.findViewById(R.id.message)).setText(message);
        ViewCompat.setElevation(snackbar, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));

        toast.setView(snackbar);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setMargin(0, 0);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public List<AlbumListData> getFavoriteAlbums() {
        return new ArrayList<>();
    }

    @Nullable
    public List<PlaylistListData> getFavoritePlaylists() throws InterruptedException {
        return new ArrayList<>();
    }

    @Nullable
    public List<ArtistListData> getFavoriteArtists() throws InterruptedException {
        return new ArrayList<>();
    }

    public List<TrackListData> getFavoriteTracks() {
        return new ArrayList<>();
    }

    public boolean toggleFavorite(PlaylistListData data) {
        try {
            if (isFavorite(data)) {
                //TODO: remove playlist from favorites
            } else {
                //TODO: add playlist to favorites
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean toggleFavorite(AlbumListData data) {
        try {
            if (isFavorite(data)) {
                //TODO: remove album from favorites
            } else {
                //TODO: add album to favorites
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean toggleFavorite(TrackListData data) {
        try {
            if (isFavorite(data)) {
                //TODO: remove track from favorites
            } else {
                //TODO: add track to favorites
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean toggleFavorite(ArtistListData data) {
        try {
            if (isFavorite(data)) {
                //TODO: remove artist from favorites
            } else {
                //TODO: add artist to favorites
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Nullable
    public Boolean isFavorite(PlaylistListData data) throws InterruptedException {
        return false;
    }

    @Nullable
    public Boolean isFavorite(AlbumListData data) {
        return false;
    }

    @Nullable
    public Boolean isFavorite(TrackListData data) {
        return false;
    }

    @Nullable
    public Boolean isFavorite(ArtistListData data) throws InterruptedException {
        return false;
    }

    @Nullable
    public ArtistListData getArtist(String id) throws InterruptedException {
        return null;
    }

    @Nullable
    public AlbumListData getAlbum(String id) throws InterruptedException {
        return null;
    }

    @Nullable
    public List<TrackListData> getTracks(PlaylistListData data) throws InterruptedException {
        return new ArrayList<>();
    }

    @Nullable
    public List<TrackListData> getTracks(ArtistListData data) throws InterruptedException {
        return new ArrayList<>();
    }

    @Nullable
    public List<TrackListData> getTracks(AlbumListData data) throws InterruptedException {
        return new ArrayList<>();
    }

    @Nullable
    public List<TrackListData> getTracks(CategoryListData data) {
        return new ArrayList<>();
    }

    @Nullable
    public List<AlbumListData> getAlbums(ArtistListData data) {
        return new ArrayList<>();
    }

    @Nullable
    public List<PlaylistListData> getPlaylists(ArtistListData data) {
        return new ArrayList<>();
    }

    @Nullable
    public List<PlaylistListData> getPlaylists(CategoryListData data) {
        return new ArrayList<>();
    }

    @Nullable
    public List<ArtistListData> getArtists(ArtistListData data) {
        return new ArrayList<>();
    }

    public List<CategoryListData> getCategories() {
        return new ArrayList<>();
    }

    @Nullable
    public List<PlaylistListData> searchPlaylists(String query, int limit) throws InterruptedException {
        query = query.toLowerCase();

        List<PlaylistListData> playlists = new ArrayList<>();
        for (int i = 0; i < this.playlists.size() && i < limit; i++) {
            PlaylistListData data = this.playlists.get(i);
            if (query.contains(data.playlistName.toLowerCase()) || data.playlistName.toLowerCase().contains(query))
                playlists.add(data);
        }

        return playlists;
    }

    @Nullable
    public List<ArtistListData> searchArtists(String query, int limit) throws InterruptedException {
        query = query.toLowerCase();

        List<ArtistListData> artists = new ArrayList<>();
        for (int i = 0; i < this.artists.size() && i < limit; i++) {
            ArtistListData data = this.artists.get(i);
            if (query.contains(data.artistName.toLowerCase()) || data.artistName.toLowerCase().contains(query))
                artists.add(data);
        }

        return artists;
    }

    @Nullable
    public List<TrackListData> searchTracks(String query, int limit) throws InterruptedException {
        query = query.toLowerCase();

        List<TrackListData> tracks = new ArrayList<>();
        for (int i = 0; i < this.tracks.size() && i < limit; i++) {
            TrackListData data = this.tracks.get(i);
            if (query.contains(data.trackName.toLowerCase()) || data.trackName.toLowerCase().contains(query))
                tracks.add(data);
        }

        return tracks;
    }

    @Nullable
    public List<AlbumListData> searchAlbums(String query, int limit) throws InterruptedException {
        query = query.toLowerCase();

        List<AlbumListData> albums = new ArrayList<>();
        for (int i = 0; i < this.albums.size() && i < limit; i++) {
            AlbumListData data = this.albums.get(i);
            if (query.contains(data.albumName.toLowerCase()) || data.albumName.toLowerCase().contains(query))
                albums.add(data);
        }

        return albums;
    }

    @Nullable
    public List<AlbumListData> getNewAlbums() {
        List<AlbumListData> albums = new ArrayList<>();

        Collections.sort(this.albums, new Comparator<AlbumListData>() {
            @Override
            public int compare(AlbumListData o1, AlbumListData o2) {
                if (o1.albumDate == null || o2.albumDate == null) return 0;
                else return o1.albumDate.compareTo(o2.albumDate);
            }
        });

        for (int i = 0; i < this.albums.size() && i < 10; i++) {
            albums.add(this.albums.get(i));
        }

        return albums;
    }

    @Nullable
    public List<PlaylistListData> getFeaturedPlaylists() {
        return new ArrayList<>();
    }
}
