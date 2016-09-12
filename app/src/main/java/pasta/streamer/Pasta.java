package pasta.streamer;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pasta.streamer.data.AlbumListData;
import pasta.streamer.data.ArtistListData;
import pasta.streamer.data.CategoryListData;
import pasta.streamer.data.PlaylistListData;
import pasta.streamer.data.TrackListData;
import pasta.streamer.dialogs.ErrorDialog;
import pasta.streamer.utils.PreferenceUtils;

public class Pasta extends Application {

    private ErrorDialog errorDialog;

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
        return new ArrayList<>();
    }

    @Nullable
    public List<ArtistListData> searchArtists(String query, int limit) throws InterruptedException {
        return new ArrayList<>();
    }

    @Nullable
    public List<TrackListData> searchTracks(String query, int limit) throws InterruptedException {
        return new ArrayList<>();
    }

    @Nullable
    public List<AlbumListData> searchAlbums(String query, int limit) throws InterruptedException {
        return new ArrayList<>();
    }

    @Nullable
    public List<AlbumListData> getNewAlbums() {
        return new ArrayList<>();
    }

    @Nullable
    public List<PlaylistListData> getFeaturedPlaylists() {
        return new ArrayList<>();
    }
}
