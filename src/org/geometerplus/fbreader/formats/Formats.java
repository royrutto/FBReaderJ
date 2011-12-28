/*
 * Copyright (C) 2007-2011 Geometer Plus <contact@geometerplus.com>
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

package org.geometerplus.fbreader.formats;

import org.geometerplus.zlibrary.core.options.ZLStringOption;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Formats {

	private static String NATIVE_FORMATS = "epub;oeb;fb2;mobi;prc";

	public static String extensionToOption(String extension) {
		return "FORMAT_" + extension;
	}

	public static String optionToExtension(String option) {
		return option.replace("FORMAT_", "");
	}

	public static ArrayList<String> getExternalFormats() {
		return listFromString(new ZLStringOption("Formats", "ExternalFormats", "").getValue());
	}

	public static ArrayList<String> getNativeFormats() {
		return listFromString(NATIVE_FORMATS);
	}

	private static ArrayList<String> listFromString(String s) {
		if (!s.equals("")) {
			return new ArrayList<String>(Arrays.asList(s.split(";")));
		} else {
			return new ArrayList<String>();
		}
	}

	private static boolean isValid(String extension) {
		if (extension.equals("")) return false;
		if (extension.contains(";")) return false;
		if (extension.contains(".")) return false;
		return true;
	}

	public static boolean addFormat(String extension) {
		if (!isValid(extension)) {
			return false;
		}
		ZLStringOption formats = new ZLStringOption("Formats", "ExternalFormats", "");
		if (getExternalFormats().contains(extension) || getNativeFormats().contains(extension)) {
			return false;
		}
		if (formats.getValue().equals("")) {
			formats.setValue(extension);
			return true;
		} else {
			formats.setValue(formats.getValue() + ";" +extension);
			return true;
		}
	}

	public static void removeFormat(String extension) {
		ZLStringOption formats = new ZLStringOption("Formats", "ExternalFormats", "");
		String s = formats.getValue();
		s = s.replace(";" + extension, "");
		s = s.replace(extension + ";", "");
		s = s.replace(extension, "");
		formats.setValue(s);
	}

	public static ZLStringOption extensionOption(String extension) {
		if (getNativeFormats().contains(extension)) {
			return new ZLStringOption("Formats", extensionToOption(extension), "org.geometerplus.zlibrary.ui.android");
		} else {
			ZLStringOption opt = new ZLStringOption("Formats", extensionToOption(extension), "");
			return new ZLStringOption("Formats", extensionToOption(extension), "");
		}
	}


}
