/*
 * Copyright (C) 2010-2012 Geometer Plus <contact@geometerplus.com>
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
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.view.*;
import android.widget.*;

import org.geometerplus.zlibrary.core.resources.ZLResource;

import org.geometerplus.zlibrary.ui.android.R;

class MultiStringPreference extends DialogPreference {
	private final ZLResource myResource;

	MultiStringPreference(Context context, ZLResource resource, String resourceKey) {
		super(context, null);
		myResource = resource.getResource(resourceKey);
		final String title = myResource.getValue();
		setTitle(title);
		setDialogTitle(title);
		setDialogLayoutResource(R.layout.multi_string_dialog);

		final ZLResource buttonResource = ZLResource.resource("dialog").getResource("button");
		setPositiveButtonText(buttonResource.getResource("ok").getValue());
		setNegativeButtonText(buttonResource.getResource("cancel").getValue());
	}

	@Override
	protected void onBindDialogView(View view) {
		final ListView listView = (ListView)view.findViewById(R.id.multi_string_list);
		listView.setAdapter(new MultiStringAdapter());
		super.onBindDialogView(view);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (which == DialogInterface.BUTTON_POSITIVE) {
		}
	}

	private static class MultiStringAdapter extends BaseAdapter {
		public int getCount() {
			return 4;
		}

		public String getItem(int position) {
			return "Item " + position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			final View view = convertView != null
				? convertView
				: LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_string_item, parent, false);
			return view;
		}
	}
}
