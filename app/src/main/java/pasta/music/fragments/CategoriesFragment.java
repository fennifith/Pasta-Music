package pasta.music.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
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
import pasta.music.adapters.CategoryAdapter;
import pasta.music.data.CategoryListData;
import pasta.music.utils.PreferenceUtils;

public class CategoriesFragment extends Fragment {

    @Bind(R.id.recyclerView)
    RecyclerView recycler;
    @Bind(R.id.progressBar)
    ProgressBar spinner;

    CategoryAdapter adapter;
    Pasta pasta;
    Action action;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        ButterKnife.bind(this, rootView);

        pasta = (Pasta) getContext().getApplicationContext();

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), PreferenceUtils.getColumnNumber(getContext(), metrics.widthPixels > metrics.heightPixels)));
        adapter = new CategoryAdapter((AppCompatActivity) getActivity(), null);
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);

        action = new Action<List<CategoryListData>>() {
            @NonNull
            @Override
            public String id() {
                return "getCategories";
            }

            @Nullable
            @Override
            protected List<CategoryListData> run() throws InterruptedException {
                return pasta.getCategories();
            }

            @Override
            protected void done(@Nullable List<CategoryListData> result) {
                if (spinner != null) spinner.setVisibility(View.GONE);
                if (result == null) {
                    pasta.onCriticalError(getActivity(), "categories action");
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
