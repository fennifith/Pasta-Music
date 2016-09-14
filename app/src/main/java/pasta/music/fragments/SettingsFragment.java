package pasta.music.fragments;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.color.ColorChooserDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import pasta.music.Pasta;
import pasta.music.R;
import pasta.music.activities.HomeActivity;
import pasta.music.utils.PreferenceUtils;
import pasta.music.views.CustomImageView;

public class SettingsFragment extends FabFragment {

    @Bind(R.id.primary_color)
    CustomImageView primary;
    @Bind(R.id.accent_color)
    CustomImageView accent;

    private View rootView;

    private int selectedLimit, selectedQuality, selectedOrder;
    private SharedPreferences prefs;
    private Pasta pasta;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false).getRoot();
        ButterKnife.bind(this, rootView);

        pasta = (Pasta) getContext().getApplicationContext();
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnCheckedChanged(R.id.preload)
    public void changePreload(boolean preload) {
        if (prefs != null && preload != PreferenceUtils.isPreload(getContext())) {
            prefs.edit().putBoolean(PreferenceUtils.PRELOAD, preload).apply();
        }
    }

    @OnCheckedChanged(R.id.debug)
    public void changeDebug(boolean debug) {
        if (prefs != null && debug != PreferenceUtils.isDebug(getContext())) {
            prefs.edit().putBoolean(PreferenceUtils.DEBUG, debug).apply();
        }
    }

    @OnClick(R.id.limit)
    public void changeLimit() {
        new AlertDialog.Builder(getContext()).setTitle(R.string.limit).setSingleChoiceItems(R.array.limits, PreferenceUtils.getLimit(getContext()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedLimit = which;
            }
        }).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prefs.edit().putInt(PreferenceUtils.LIMIT, selectedLimit).apply();
                pasta.showToast(getString(R.string.restart_msg));
                dialog.dismiss();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    @OnCheckedChanged(R.id.darkmode)
    public void changeDarkMode(boolean dark) {
        if (prefs != null && dark != PreferenceUtils.isDarkTheme(getContext())) {
            prefs.edit().putBoolean(PreferenceUtils.DARK_THEME, dark).apply();
            pasta.showToast(getString(R.string.restart_msg));
        }
    }

    @OnCheckedChanged(R.id.thumbnails)
    public void changeThumbnails(boolean thumbnails) {
        if (prefs != null && thumbnails != PreferenceUtils.isThumbnails(getContext())) {
            prefs.edit().putBoolean(PreferenceUtils.THUMBNAILS, thumbnails).apply();
            pasta.showToast(getString(R.string.restart_msg));
        }
    }

    @OnCheckedChanged(R.id.cards)
    public void changeCards(boolean cards) {
        if (prefs != null && cards != PreferenceUtils.isCards(getContext())) {
            prefs.edit().putBoolean(PreferenceUtils.CARDS, cards).apply();
        }
    }

    @OnCheckedChanged(R.id.lists)
    public void changeListTracks(boolean listTracks) {
        if (prefs != null && listTracks != PreferenceUtils.isListTracks(getContext())) {
            prefs.edit().putBoolean(PreferenceUtils.LIST_TRACKS, listTracks).apply();
        }
    }

    @OnCheckedChanged(R.id.palette)
    public void changePalette(boolean palette) {
        if (prefs != null && palette != PreferenceUtils.isPalette(getContext())) {
            prefs.edit().putBoolean(PreferenceUtils.PALETTE, palette).apply();
            pasta.showToast(getString(R.string.restart_msg));
        }
    }

    @OnClick(R.id.primary)
    public void setPrimary() {
        new ColorChooserDialog.Builder((HomeActivity) getActivity(), R.string.primary_color)
                .titleSub(R.string.primary_color)
                .doneButton(R.string.save)
                .cancelButton(R.string.cancel)
                .backButton(R.string.md_back_label)
                .preselect(PreferenceUtils.getPrimaryColor(getContext()))
                .show();
    }

    @OnClick(R.id.accent)
    public void setAccent() {
        new ColorChooserDialog.Builder((HomeActivity) getActivity(), R.string.accent_color)
                .titleSub(R.string.accent_color)
                .doneButton(R.string.save)
                .cancelButton(R.string.cancel)
                .backButton(R.string.md_back_label)
                .accentMode(true)
                .preselect(PreferenceUtils.getAccentColor(getContext()))
                .show();
    }

    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        switch (dialog.getTitle()) {
            case R.string.primary_color:
                if (PreferenceUtils.getPrimaryColor(getContext()) == selectedColor) return;
                prefs.edit().putInt(PreferenceUtils.PRIMARY, selectedColor).apply();
                primary.transition(new ColorDrawable(selectedColor));
                break;
            case R.string.accent_color:
                if (PreferenceUtils.getAccentColor(getContext()) == selectedColor) return;
                prefs.edit().putInt(PreferenceUtils.ACCENT, selectedColor).apply();
                accent.transition(new ColorDrawable(selectedColor));
                break;
            default:
                return;
        }
        pasta.showToast(getString(R.string.restart_msg));
    }

    @OnClick(R.id.quality)
    public void setQuality() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.quality)
                .setSingleChoiceItems(R.array.qualities, PreferenceUtils.getQuality(getContext()), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedQuality = which;
                    }
                }).setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int id) {
                switch (selectedQuality) {
                    case 0:
                        prefs.edit().putInt(PreferenceUtils.QUALITY, 0).apply();
                        break;
                    case 1:
                        prefs.edit().putInt(PreferenceUtils.QUALITY, 1).apply();
                        break;
                    case 2:
                        prefs.edit().putInt(PreferenceUtils.QUALITY, 2).apply();
                        break;
                }
                pasta.showToast(getString(R.string.restart_msg));
                d.dismiss();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int id) {
                d.dismiss();
            }
        }).create().show();
    }

    @OnClick(R.id.organize)
    public void setOrganization() {
        PreferenceUtils.getOrderingDialog(getContext(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedOrder = which;
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int which) {
                PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt(PreferenceUtils.ORDER, selectedOrder).apply();
                d.dismiss();
            }
        }).show();
    }
}
