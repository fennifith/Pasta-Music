package pasta.streamer.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import pasta.streamer.R;
import pasta.streamer.adapters.HomePagerAdapter;
import pasta.streamer.utils.PreferenceUtils;

public class HomeFragment extends Fragment {

    @Bind(R.id.vp)
    ViewPager vp;
    @Bind(R.id.tl)
    TabLayout tl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false).getRoot();
        ButterKnife.bind(this, rootView);

        ViewCompat.setElevation(tl, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics()));

        vp.setAdapter(new HomePagerAdapter(getActivity(), getActivity().getSupportFragmentManager()));
        tl.setupWithViewPager(vp);
        tl.setSelectedTabIndicatorColor(PreferenceUtils.getAccentColor(getContext()));

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
