package pasta.music.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.async.Action;
import com.afollestad.async.Async;
import com.afollestad.async.Pool;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pasta.music.Pasta;
import pasta.music.R;
import pasta.music.activities.HomeActivity;
import pasta.music.adapters.SectionedOmniAdapter;
import pasta.music.data.AlbumListData;
import pasta.music.data.ArtistListData;
import pasta.music.data.PlaylistListData;
import pasta.music.data.TrackListData;
import pasta.music.utils.ImageUtils;
import pasta.music.utils.PreferenceUtils;
import pasta.music.utils.StaticUtils;
import pasta.music.views.CustomImageView;

public class ArtistFragment extends FullScreenFragment {

    @Bind(R.id.progressBar2)
    ProgressBar spinner;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.topTenTrackListView)
    RecyclerView recycler;
    @Bind(R.id.header)
    CustomImageView header;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.extra)
    TextView extra;
    @Bind(R.id.genres)
    FlexboxLayout genres;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.somethingbar)
    View somethingbar;
    @Nullable @Bind(R.id.backgroundImage)
    CustomImageView backgroundImage;

    private ArtistListData data;
    private SectionedOmniAdapter adapter;
    private GridLayoutManager manager;
    private Pool pool;
    private boolean palette;
    private Pasta pasta;
    int limit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = DataBindingUtil.inflate(inflater, R.layout.fragment_artist, container, false).getRoot();
        ButterKnife.bind(this, rootView);

        data = getArguments().getParcelable("artist");

        palette = PreferenceUtils.isPalette(getContext());
        pasta = (Pasta) getContext().getApplicationContext();
        limit = (PreferenceUtils.getLimit(getContext()) + 1) * 10;

        setHasOptionsMenu(true);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        toolbar.inflateMenu(R.menu.menu_basic);
        modifyMenu(toolbar.getMenu());
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMenuClick(item);
                return false;
            }
        });

        title.setText(data.artistName);
        extra.setText(String.valueOf(data.followers) + " followers");

        if (data.genres.size() > 0) {
            for (String genre : data.genres) {
                View v = LayoutInflater.from(getContext()).inflate(R.layout.genre_item, null);
                ((TextView) v.findViewById(R.id.title)).setText(genre);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), HomeActivity.class);
                        i.putExtra("query", ((TextView) v.findViewById(R.id.title)).getText().toString());
                        startActivity(i);
                    }
                });
                genres.addView(v);
            }

            genres.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (recycler != null) recycler.setPadding(0, genres.getHeight(), 0, 0);
                    if (genres != null)
                        genres.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        } else {
            genres.setVisibility(View.GONE);
        }

        spinner.setVisibility(View.VISIBLE);

        manager = new GridLayoutManager(getContext(), PreferenceUtils.getColumnNumber(getContext(), false));
        if (PreferenceUtils.isCards(getContext())) {
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return manager.getSpanCount();
                }
            });
        } else {
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (adapter.getItemViewType(position) == 0 || adapter.getItemViewType(position) == 4) return manager.getSpanCount();
                    else return 1;
                }
            });
        }
        recycler.setLayoutManager(manager);


        adapter = new SectionedOmniAdapter((AppCompatActivity) getActivity(), null);
        recycler.setAdapter(adapter);

        pool = Async.parallel(new Action<List<TrackListData>>() {
            @NonNull
            @Override
            public String id() {
                return "searchTracks";
            }

            @Nullable
            @Override
            protected List<TrackListData> run() throws InterruptedException {
                return pasta.getTracks(data);
            }

            @Override
            protected void done(@Nullable List<TrackListData> result) {
                if (spinner != null) spinner.setVisibility(View.GONE);
                if (result == null) pasta.onError(getActivity(), "artist tracks action");
                else adapter.addData(result);
            }
        }, new Action<List<AlbumListData>>() {
            @NonNull
            @Override
            public String id() {
                return "searchAlbums";
            }

            @Nullable
            @Override
            protected List<AlbumListData> run() throws InterruptedException {
                return pasta.getAlbums(data);
            }

            @Override
            protected void done(@Nullable List<AlbumListData> result) {
                if (result == null) {
                    pasta.onError(getActivity(), "artist albums action");
                    return;
                }

                if (spinner != null) spinner.setVisibility(View.GONE);
                if (adapter != null) adapter.addData(result);
            }
        }, new Action<List<PlaylistListData>>() {
            @NonNull
            @Override
            public String id() {
                return "getPlaylists";
            }

            @Nullable
            @Override
            protected List<PlaylistListData> run() throws InterruptedException {
                return pasta.searchPlaylists(data.artistName, limit);
            }

            @Override
            protected void done(@Nullable List<PlaylistListData> result) {
                if (spinner != null) spinner.setVisibility(View.GONE);
                if (result == null) pasta.onError(getContext(), "artist playlists action");
                else adapter.addData(result);
            }
        }, new Action<List<ArtistListData>>() {
            @NonNull
            @Override
            public String id() {
                return "getArtists";
            }

            @Nullable
            @Override
            protected List<ArtistListData> run() throws InterruptedException {
                return pasta.getArtists(data);
            }

            @Override
            protected void done(@Nullable List<ArtistListData> result) {
                if (spinner != null) spinner.setVisibility(View.GONE);
                if (result == null)
                    pasta.onError(getContext(), "artist related artists action");
                else adapter.addData(result);
            }
        });

        Glide.with(getContext()).load(data.artistImage).placeholder(ImageUtils.getVectorDrawable(getContext(), R.drawable.preload)).into(new GlideDrawableImageViewTarget(header) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                if (header != null) header.transition(resource);

                Bitmap bitmap = ImageUtils.drawableToBitmap(resource);
                if (backgroundImage != null)
                    backgroundImage.transition(ImageUtils.blurBitmap(bitmap));
                if (palette) {
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            int primary = palette.getMutedColor(Color.GRAY);
                            if (collapsingToolbarLayout != null)
                                collapsingToolbarLayout.setContentScrimColor(primary);

                            ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), PreferenceUtils.getPrimaryColor(getContext()), primary);
                            animator.setDuration(250);
                            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    if (somethingbar != null)
                                        somethingbar.setBackgroundColor((int) animation.getAnimatedValue());
                                }
                            });
                            animator.start();

                            setData(data.artistName, primary, palette.getDarkVibrantColor(primary));
                        }
                    });
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_basic, menu);
        modifyMenu(menu);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        onMenuClick(item);
        return false;
    }

    @Override
    public void onDestroyView() {
        if (pool != null && pool.isExecuting()) pool.cancel();
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void modifyMenu(final Menu menu) {
        new Action<Boolean>() {
            @NonNull
            @Override
            public String id() {
                return "isArtistFav";
            }

            @Nullable
            @Override
            protected Boolean run() throws InterruptedException {
                try {
                    return pasta.isFavorite(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void done(@Nullable Boolean result) {
                if (result == null) {
                    pasta.onError(getActivity(), "artist favorite action");
                    return;
                }
                if (result) {
                    menu.findItem(R.id.action_fav).setIcon(R.drawable.ic_fav);
                } else {
                    menu.findItem(R.id.action_fav).setIcon(R.drawable.ic_unfav);
                }
            }

        }.execute();
    }

    private void onMenuClick(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.action_fav:
                new Action<Boolean>() {
                    @NonNull
                    @Override
                    public String id() {
                        return "favArtist";
                    }

                    @Nullable
                    @Override
                    protected Boolean run() throws InterruptedException {
                        if (!pasta.toggleFavorite(data)) {
                            return null;
                        } else return pasta.isFavorite(data);
                    }

                    @Override
                    protected void done(@Nullable Boolean result) {
                        if (result == null) {
                            pasta.onError(getActivity(), "artist favorite menu action");
                            return;
                        }
                        if (result) {
                            item.setIcon(R.drawable.ic_fav);
                        } else {
                            item.setIcon(R.drawable.ic_unfav);
                        }
                    }

                }.execute();
                break;
            case R.id.action_share:
                Intent s = new Intent(android.content.Intent.ACTION_SEND);
                s.setType("text/plain");
                s.putExtra(Intent.EXTRA_SUBJECT, data.artistName);
                s.putExtra(Intent.EXTRA_TEXT, StaticUtils.getArtistUrl(data.artistId));
                startActivity(Intent.createChooser(s, data.artistName));
                break;
            case R.id.action_web:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(StaticUtils.getArtistUrl(data.artistId))));
                break;
        }
    }
}
