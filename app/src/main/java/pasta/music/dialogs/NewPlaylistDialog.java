package pasta.music.dialogs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import pasta.music.Pasta;
import pasta.music.R;
import pasta.music.data.PlaylistListData;
import pasta.music.utils.ImageUtils;

public class NewPlaylistDialog extends AppCompatDialog {

    Pasta pasta;
    PlaylistListData data;
    OnCreateListener listener;

    Toolbar toolbar;
    EditText titleView;

    public NewPlaylistDialog(Context context) {
        super(context, R.style.AppTheme_Dialog);
        pasta = (Pasta) context.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_new_playlist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Drawable close = ImageUtils.getVectorDrawable(getContext(), R.drawable.ic_close);
        DrawableCompat.setTint(close, Color.BLACK);
        toolbar.setNavigationIcon(close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) dismiss();
            }
        });

        toolbar.setTitle(data == null ? R.string.playlist_create : R.string.playlist_modify);

        titleView = (EditText) findViewById(R.id.playlistTitle);

        if (data != null) {
            titleView.setText(data.playlistName);
            ;
        }

        titleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titleView.getText().length() < 1)
                    titleView.setError(getContext().getString(R.string.no_playlist_text));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowing()) dismiss();
            }
        });

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleView.getText().length() < 1) {
                    titleView.setError(getContext().getString(R.string.no_playlist_text));
                    return;
                }

                //TODO: do something
            }
        });
    }

    public NewPlaylistDialog setPlaylist(@NonNull PlaylistListData data) {
        this.data = data;
        if (titleView != null) titleView.setText(data.playlistName);
        if (toolbar != null) toolbar.setTitle(R.string.playlist_modify);
        return this;
    }

    public NewPlaylistDialog setOnCreateListener(OnCreateListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnCreateListener {
        void onCreate();
    }

}
