/*
 * Copyright (C) 2009-2011 Geometer Plus <contact@geometerplus.com>
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
import android.preference.EditTextPreference;

import android.util.Log;

class AddFormatPreference extends EditTextPreference implements ZLPreference {
	private final Screen myScreen;

	AddFormatPreference(Context context, Screen scr) {
		super(context);

		myScreen = scr;

		setTitle("Add file format");
		setOrder(100500);
		getEditText().setSingleLine();
	}

	boolean isValid(String extension) {
		return (extension != "" || (!extension.contains(";")));
	}

	@Override
	protected void onDialogClosed(boolean result) {
		if (result) {
			if (!isValid(getEditText().getText().toString())) return;
			
			if (Formats.addFormat(getEditText().getText().toString())) {
				myScreen.addFormatOption(Formats.extensionOption(getEditText().getText().toString()), false);
			}
		}
		super.onDialogClosed(result);
	}

	public void onAccept() {
	}

}
