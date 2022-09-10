package com.epic.fragments;

import com.android.internal.logging.nano.MetricsProto;

import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.UserHandle;
import com.android.settings.development.OverlayCategoryPreferenceController;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.content.om.IOverlayManager;
import android.content.res.Resources;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceCategory;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.PreferenceFragment;
import androidx.preference.SwitchPreference;
import android.provider.Settings;
import com.epic.support.preferences.SystemSettingEditTextPreference;
import com.epic.support.preferences.SystemSettingMasterSwitchPreference;
import com.epic.support.preferences.SystemSettingSwitchPreference;
import com.epic.support.preferences.SystemSettingListPreference;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import java.util.Locale;
import android.text.TextUtils;
import android.content.Context;
import androidx.fragment.app.Fragment;
import android.view.View;
import android.os.UserHandle;
import android.os.SystemProperties;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;
import android.util.Log;
import com.android.internal.util.epic.systemUtils;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.Lifecycle;
import org.json.JSONException;
import org.json.JSONObject;
import static android.os.UserHandle.USER_SYSTEM;
import android.os.RemoteException;
import android.os.ServiceManager;
import static android.os.UserHandle.USER_CURRENT;
import java.util.List;
import java.util.ArrayList;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settingslib.search.SearchIndexable;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.regex.Pattern;

@SearchIndexable(forTarget = SearchIndexable.ALL & ~SearchIndexable.ARC)
public class UserInterface extends DashboardFragment implements
        OnPreferenceChangeListener {

    public static final String TAG = "UserInterface";
    private Context mContext;
    
    private static final String SETTINGS_DASHBOARD_STYLE = "settings_dashboard_style";
    private static final String SETTINGS_HEADER_IMAGE = "settings_header_image";
    private static final String SETTINGS_HEADER_IMAGE_RANDOM = "settings_header_image_random";
    private static final String SETTINGS_HEADER_TEXT = "settings_header_text";
    private static final String SETTINGS_HEADER_TEXT_ENABLED = "settings_header_text_enabled";
    private static final String SETTINGS_CONTEXTUAL_MESSAGES = "settings_contextual_messages";
    private static final String USE_STOCK_LAYOUT = "use_stock_layout";
    private static final String ABOUT_PHONE_STYLE = "about_card_style";
    private static final String HIDE_USER_CARD = "hide_user_card";
    
    private SystemSettingListPreference mSettingsDashBoardStyle;
    private SystemSettingListPreference mAboutPhoneStyle;
    private SystemSettingSwitchPreference mUseStockLayout;
    private SystemSettingSwitchPreference mHideUserCard;
    private Preference mSettingsHeaderImage;
    private Preference mSettingsHeaderImageRandom;
    private Preference mSettingsMessage;
    private SystemSettingEditTextPreference mSettingsHeaderText;
    private SystemSettingSwitchPreference mSettingsHeaderTextEnabled;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);


        final ContentResolver resolver = getActivity().getContentResolver();

        PreferenceScreen prefSet = getPreferenceScreen();
        
        mSettingsDashBoardStyle = (SystemSettingListPreference) findPreference(SETTINGS_DASHBOARD_STYLE);
        mSettingsDashBoardStyle.setOnPreferenceChangeListener(this);
        mSettingsHeaderImageRandom = findPreference(SETTINGS_HEADER_IMAGE_RANDOM);
        mSettingsHeaderImageRandom.setOnPreferenceChangeListener(this);
        mSettingsMessage = findPreference(SETTINGS_CONTEXTUAL_MESSAGES);
        mSettingsMessage.setOnPreferenceChangeListener(this);
        mSettingsHeaderImage = findPreference(SETTINGS_HEADER_IMAGE);
        mSettingsHeaderImage.setOnPreferenceChangeListener(this);
        mUseStockLayout = (SystemSettingSwitchPreference) findPreference(USE_STOCK_LAYOUT);
        mUseStockLayout.setOnPreferenceChangeListener(this);
        mAboutPhoneStyle = (SystemSettingListPreference) findPreference(ABOUT_PHONE_STYLE);
        mAboutPhoneStyle.setOnPreferenceChangeListener(this);
        mHideUserCard = (SystemSettingSwitchPreference) findPreference(HIDE_USER_CARD);
        mHideUserCard.setOnPreferenceChangeListener(this);
        mSettingsHeaderText = (SystemSettingEditTextPreference) findPreference(SETTINGS_HEADER_TEXT);
        mSettingsHeaderText.setOnPreferenceChangeListener(this);
        mSettingsHeaderTextEnabled = (SystemSettingSwitchPreference) findPreference(SETTINGS_HEADER_TEXT_ENABLED);
        mSettingsHeaderTextEnabled.setOnPreferenceChangeListener(this);
    }

    @Override
    protected int getPreferenceScreenResId() {
        return R.xml.epic_settings_ui;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        ContentResolver resolver = getActivity().getContentResolver();
            if (preference == mSettingsDashBoardStyle) {
            systemUtils.showSettingsRestartDialog(getContext());
            return true;
        } else if (preference == mUseStockLayout) {
            systemUtils.showSettingsRestartDialog(getContext());
            return true;
        } else if (preference == mHideUserCard) {
            systemUtils.showSettingsRestartDialog(getContext());
            return true;
        } else if (preference == mAboutPhoneStyle) {
            systemUtils.showSettingsRestartDialog(getContext());
            return true;
        } else if (preference == mSettingsHeaderImage) {
            systemUtils.showSettingsRestartDialog(getContext());
            return true;
        } else if (preference == mSettingsHeaderImageRandom) {
            systemUtils.showSettingsRestartDialog(getContext());
            return true;
        } else if (preference == mSettingsMessage) {
            systemUtils.showSettingsRestartDialog(getContext());
            return true;
        } else if (preference == mSettingsHeaderTextEnabled) {
            boolean enable = (Boolean) newValue;
            SystemProperties.set("persist.sys.settings.header_text_enabled", enable ? "true" : "false");
            systemUtils.showSettingsRestartDialog(getContext());
            return true;
        } else if (preference == mSettingsHeaderText) {
            String value = (String) newValue;
            SystemProperties.set("persist.sys.settings.header_text", value);
            systemUtils.showSettingsRestartDialog(getContext());
            return true;
	}
        return false;
    }

    @Override
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        return buildPreferenceControllers(context, getSettingsLifecycle(), this);
    }

    private static List<AbstractPreferenceController> buildPreferenceControllers(
            Context context, Lifecycle lifecycle, Fragment fragment) {
        final List<AbstractPreferenceController> controllers = new ArrayList<>();
        return controllers;
    }

    @Override
    protected String getLogTag() {
        return TAG;
    }
    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.EPIC_SETTINGS;
    }

    /**
     * For Search.
     */
    public static final BaseSearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider(R.xml.epic_settings_ui);
}