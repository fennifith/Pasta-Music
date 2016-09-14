package pasta.music.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.afollestad.async.Action;

import java.util.List;

import pasta.music.Pasta;
import pasta.music.R;
import pasta.music.data.PlaylistListData;
import pasta.music.data.TrackListData;
import pasta.music.utils.ImageUtils;

public class AddToPlaylistDialog extends AppCompatDialog {

    private Pasta pasta;
    private TrackListData data;

    ListView listView;
    List<PlaylistListData> playlists;


    public AddToPlaylistDialog(Context context, TrackListData data) {
        super(context, R.style.AppTheme_Dialog);
        pasta = (Pasta) context.getApplicationContext();
        this.data = data;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_to_playlist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Drawable close = ImageUtils.getVectorDrawable(getContext(), R.drawable.ic_close);
        DrawableCompat.setTint(close, Color.BLACK);
        toolbar.setNavigationIcon(close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) dismiss();
            }
        });

        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new Action<Boolean>() {
                    @NonNull
                    @Override
                    public String id() {
                        return "addToPlaylist";
                    }

                    @Nullable
                    @Override
                    protected Boolean run() throws InterruptedException {
                        try {
                            PlaylistListData playlist = playlists.get(position);

                            //TODO: add track (data) to playlist
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }

                        return true;
                    }

                    @Override
                    protected void done(@Nullable Boolean result) {
                        if (result == null) result = false;
                        pasta.showToast(pasta.getString(result ? R.string.added : R.string.error));
                    }
                }.execute();

                if (isShowing()) dismiss();
            }
        });

        new Action<List<PlaylistListData>>() {
            @NonNull
            @Override
            public String id() {
                return "showAddToDialog";
            }

            @Nullable
            @Override
            protected List<PlaylistListData> run() throws InterruptedException {
                return pasta.getFavoritePlaylists();
            }

            @Override
            protected void done(@Nullable final List<PlaylistListData> result) {
                if (result == null) {
                    pasta.onError(getContext(), "add to playlist dialog");
                    AddToPlaylistDialog.this.dismiss();
                    return;
                }

                AddToPlaylistDialog.this.playlists = result;

                String[] names = new String[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    names[i] = result.get(i).playlistName;
                }

                listView.setAdapter(new ArrayAdapter<>(getContext(), R.layout.playlist_item_text, R.id.title, names));
            }
        }.execute();
    }
}
