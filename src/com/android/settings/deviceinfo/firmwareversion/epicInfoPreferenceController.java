/*
 * Copyright (C) 2020 Wave-OS
 * Copyright (C) 2023 the RisingOS Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.os.Build;
import android.os.SystemProperties;
import android.widget.TextView;
import android.text.TextUtils;

import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.widget.LayoutPreference;

public class epicInfoPreferenceController extends AbstractPreferenceController {

    private static final String KEY_EPIC_INFO = "epic_info";
    private static final String KEY_EPIC_DEVICE = "epic_device";
    private static final String KEY_EPIC_VERSION = "epic_version";
    private static final String KEY_BUILD_STATUS = "rom_build_status";
    private static final String KEY_BUILD_VERSION = "epic_build_version";
    
    private static final String PROP_EPIC_VERSION = "ro.epic.version";
    private static final String PROP_EPIC_VERSION_CODE = "ro.epic.code";
    private static final String PROP_EPIC_RELEASETYPE = "ro.epic.releasetype";
    private static final String PROP_EPIC_MAINTAINER = "ro.epic.maintainer";
    private static final String PROP_EPIC_DEVICE = "ro.epic.device";
    private static final String PROP_EPIC_BUILD_TYPE = "ro.epic.packagetype";
    private static final String PROP_EPIC_BUILD_VERSION = "ro.epic.build.version";


    public epicInfoPreferenceController(Context context) {
        super(context);
    }

    private String getDeviceName() {
        String device = SystemProperties.get(PROP_EPIC_DEVICE, "");
        if (device.equals("")) {
            device = Build.MANUFACTURER + " " + Build.MODEL;
        }
        return device;
    }

    private String getEpicBuildVersion() {
        final String buildVer = SystemProperties.get(PROP_EPIC_BUILD_VERSION,
                this.mContext.getString(R.string.device_info_default));;

        return buildVer;
    }
    
    private String getEpicVersion() {
        final String version = SystemProperties.get(PROP_EPIC_VERSION,
                this.mContext.getString(R.string.device_info_default));
        final String versionCode = SystemProperties.get(PROP_EPIC_VERSION_CODE,
                this.mContext.getString(R.string.device_info_default));
        final String buildType = SystemProperties.get(PROP_EPIC_BUILD_TYPE,
                this.mContext.getString(R.string.device_info_default));

        return version + " | " + versionCode + " | " + buildType;
    }

    private String getEpicReleaseType() {
        final String releaseType = SystemProperties.get(PROP_EPIC_RELEASETYPE,
                this.mContext.getString(R.string.device_info_default));
	
        return releaseType.substring(0, 1).toUpperCase() +
                 releaseType.substring(1).toLowerCase();
    }
    
    private String getEpicbuildStatus() {
	final String buildType = SystemProperties.get(PROP_EPIC_RELEASETYPE,
                this.mContext.getString(R.string.device_info_default));
        final String isOfficial = this.mContext.getString(R.string.build_is_official_title);
	final String isCommunity = this.mContext.getString(R.string.build_is_community_title);
	
	if (buildType.toLowerCase().equals("official")) {
		return isOfficial;
	} else {
		return isCommunity;
	}
    }

    private String getEpicMaintainer() {
	final String EpicMaintainer = SystemProperties.get(PROP_EPIC_MAINTAINER,
                this.mContext.getString(R.string.device_info_default));
	final String buildType = SystemProperties.get(PROP_EPIC_RELEASETYPE,
                this.mContext.getString(R.string.device_info_default));
        final String isOffFine = this.mContext.getString(R.string.build_is_official_summary, EpicMaintainer);
	final String isOffMiss = this.mContext.getString(R.string.build_is_official_summary_oopsie);
	final String isCommMiss = this.mContext.getString(R.string.build_is_community_summary_oopsie);
	final String isCommFine = this.mContext.getString(R.string.build_is_community_summary, EpicMaintainer);
	
	if (buildType.toLowerCase().equals("official") && !EpicMaintainer.equalsIgnoreCase("Unknown")) {
	    return isOffFine;
	} else if (buildType.toLowerCase().equals("official") && EpicMaintainer.equalsIgnoreCase("Unknown")) {
	     return isOffMiss;
	} else if (buildType.equalsIgnoreCase("Community") && EpicMaintainer.equalsIgnoreCase("Unknown")) {
	     return isCommMiss;
	} else {
	    return isCommFine;
	}
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        final Preference arcVerPref = screen.findPreference(KEY_EPIC_VERSION);
        final Preference arcDevPref = screen.findPreference(KEY_EPIC_DEVICE);
        final Preference buildStatusPref = screen.findPreference(KEY_BUILD_STATUS);
        final Preference buildVerPref = screen.findPreference(KEY_BUILD_VERSION);
        final String EpicVersion = getEpicVersion();
        final String EpicDevice = getDeviceName();
        final String EpicReleaseType = getEpicReleaseType();
        final String EpicMaintainer = getEpicMaintainer();
	final String buildStatus = getEpicbuildStatus();
	final String buildVer = getEpicBuildVersion();
	final String isOfficial = SystemProperties.get(PROP_EPIC_RELEASETYPE,
                this.mContext.getString(R.string.device_info_default));
	buildStatusPref.setTitle(buildStatus);
	buildStatusPref.setSummary(EpicMaintainer);
	buildVerPref.setSummary(buildVer);
        arcVerPref.setSummary(EpicVersion);
        arcDevPref.setSummary(EpicDevice);
	if (isOfficial.toLowerCase().contains("official")) {
		 buildStatusPref.setIcon(R.drawable.verified);
	} else {
		buildStatusPref.setIcon(R.drawable.unverified);
	}
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getPreferenceKey() {
        return KEY_EPIC_INFO;
    }
}
