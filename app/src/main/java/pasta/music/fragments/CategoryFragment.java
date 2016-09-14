package pasta.music.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.afollestad.async.Action;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pasta.music.Pasta;
import pasta.music.R;
import pasta.music.adapters.OmniAdapter;
import pasta.music.data.CategoryListData;
import pasta.music.data.PlaylistListData;
import pasta.music.utils.PreferenceUtils;

public class CategoryFragment extends FullScreenFragment {

    @Bind(R.id.topTenTrackListView)
    RecyclerView recycler;
    @Bind(R.id.progressBar2)
    ProgressBar spinner;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    OmniAdapter adapter;
    GridLayoutManager manager;
    CategoryListData data;
    Action action;
    int limit;
    Pasta pasta;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false).getRoot();
        ButterKnife.bind(this, rootView);

        pasta = (Pasta) getContext().getApplicationContext();
        data = getArguments().getParcelable("category");
        limit = (PreferenceUtils.getLimit(getContext()) + 1) * 10;

        toolbar.setTitle(data.categoryName);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        setData(data.categoryName, PreferenceUtils.getPrimaryColor(getContext()), PreferenceUtils.getPrimaryColor(getContext()));

        spinner.setVisibility(View.VISIBLE);

        manager = new GridLayoutManager(getContext(), PreferenceUtils.getColumnNumber(getContext(), false));

        if (PreferenceUtils.isCards(getContext())) {
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return manager.getSpanCount();
                }
            });
        }

        recycler.setLayoutManager(manager);
        adapter = new OmniAdapter((AppCompatActivity) getActivity(), null, false);
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);

        action = new Action<List<PlaylistListData>>() {
            @NonNull
            @Override
            public String id() {
                return "getCategoryPlaylists";
            }

            @Nullable
            @Override
            protected List<PlaylistListData> run() throws InterruptedException {
                return pasta.getPlaylists(data);
            }

            @Override
            protected void done(@Nullable List<PlaylistListData> result) {
                if (spinner != null) spinner.setVisibility(View.GONE);
                if (result == null) {
                    pasta.onCriticalError(getActivity(), "category playlists action");
                    return;
                }
                adapter.swapData(result);
            }
        };
        action.execute();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (action != null && action.isExecuting()) action.cancel();
        ButterKnife.unbind(this);
    }
}
