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

import java.util.*;

import org.geometerplus.android.fbreader.preferences.ZLPreferenceActivity.Screen;
import org.geometerplus.fbreader.formats.Formats;
import org.geometerplus.zlibrary.core.resources.ZLResource;

import android.content.Context;
import android.preference.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.util.Log;

class AddFormatPreference extends EditTextPreference {
	private final Screen myScreen;

	AddFormatPreference(Context context, Screen scr) {
		super(context);

		myScreen = scr;

		final String title = ZLResource.resource("dialog")
			.getResource("Preferences")
			.getResource("formatManaging")
			.getResource("addNewFormat")
			.getValue();
		setTitle(title);
		setOrder(100500);
		getEditText().setSingleLine();
	}

	private void showErrorDialog(final String errName) {
		((Activity)getContext()).runOnUiThread(new Runnable() {
			public void run() {
				final String title = ZLResource.resource("errorMessage").getResource(errName).getValue();
				new AlertDialog.Builder(((Activity)getContext()))
					.setTitle(title)
					.setIcon(0)
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					})
					.create().show();
				}
		});
	}

	@Override
	protected void onDialogClosed(boolean result) {
		if (result) {
			if (Formats.addFormat(getEditText().getText().toString())) {
				FormatPreference newPref = (FormatPreference)myScreen.addFormatOption(Formats.extensionOption(getEditText().getText().toString()), false);
				if (!newPref.runIfNotEmpty()) {
					showErrorDialog("appNotFound");

					myScreen.removePreference(newPref);
					Formats.removeFormat(getEditText().getText().toString());
				}
			} else {
				showErrorDialog("invalidFormat");
			}
		}
		super.onDialogClosed(result);
	}

}
