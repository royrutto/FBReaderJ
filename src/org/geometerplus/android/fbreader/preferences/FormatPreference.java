/*
 * Copyright (C) 2009-2012 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader.preferences;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.Intent;
import android.preference.*;

import android.net.Uri;

import java.util.*;

import org.geometerplus.zlibrary.core.options.ZLStringOption;
import org.geometerplus.fbreader.formats.Formats;
import org.geometerplus.fbreader.formats.BigMimeTypeMap;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.android.fbreader.preferences.ZLPreferenceActivity.Screen;

import android.app.AlertDialog;

import android.util.Log;

class FormatPreference extends ListPreference {
	private final ZLStringOption myOption;
	private final HashSet<String> myPaths = new HashSet<String>();
	private final Screen myScreen;
	private final String myFormat;
	private final boolean myIsNative;

	FormatPreference(Context context, ZLStringOption option, String formatName, boolean isNative, Screen scr) {
		super(context);

		myOption = option;
		setTitle(formatName);
		myScreen = scr;
		myFormat = formatName;
		myIsNative = isNative;
//		fillList();
		final String emptySummary = ZLResource.resource("dialog")
			.getResource("Preferences")
			.getResource("formatManaging")
			.getResource("appNotSet")
			.getValue();
		if (myOption.getValue() != "") {
			final PackageManager pm = getContext().getPackageManager();
			try {
				ApplicationInfo info = pm.getApplicationInfo(myOption.getValue(), 0);
				setSummary(info.loadLabel(pm).toString());
			} catch (PackageManager.NameNotFoundException e) {
				myOption.setValue("");
				setSummary(emptySummary);
			}
		} else {
			setSummary(emptySummary);
		}
	}

	protected void onClick() {
		fillList();
		super.onClick();
	}

	protected void fillList() {
		final PackageManager pm = getContext().getPackageManager();
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<String> values = new ArrayList<String>();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("file:///sdcard/fgsfds." + myFormat));
		final String mimeType = BigMimeTypeMap.getType(myFormat);
		if (mimeType != null) {
			intent.setDataAndType(Uri.parse("file:///sdcard/fgsfds." + myFormat), mimeType);
		}
		for (ResolveInfo packageInfo : pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)) {
			if (!myPaths.contains(packageInfo.activityInfo.applicationInfo.packageName)) {
				values.add(packageInfo.activityInfo.applicationInfo.packageName);
				names.add(packageInfo.activityInfo.applicationInfo.loadLabel(pm).toString());
				myPaths.add(packageInfo.activityInfo.applicationInfo.packageName);
			}
		}
		myPaths.clear();
		if (!myIsNative) {
			final String deleteItem = ZLResource.resource("dialog")
				.getResource("Preferences")
				.getResource("formatManaging")
				.getResource("deleteFormat")
				.getValue();
			values.add("DELETE");
			names.add(deleteItem);
		}
		setEntries(names.toArray(new String[names.size()]));
		setEntryValues(values.toArray(new String[values.size()]));
		if (myOption.getValue() != "") {
			setValue(myOption.getValue());
		}

	}

	@Override
	protected void onDialogClosed(boolean result) {
		super.onDialogClosed(result);
		if (result) {
			if (getValue().equals("DELETE")) {
				myScreen.removePreference(this);
				Formats.removeFormat(getTitle().toString());
				myOption.setValue("");
			} else {
				setSummary(getEntry());
				myOption.setValue(getValue());
			}
		}
	}
}
