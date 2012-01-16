/*
 * Copyright (C) 2007-2012 Geometer Plus <contact@geometerplus.com>
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

	public static final int NATIVE = 0;
	public static final int EXTERNAL = 1;
	public static final int UNDEFINED = 2;

	private static String NATIVE_FORMATS = "epub;fb2;mobi;oeb";

	public static String extensionToOption(String extension) {
		extension = extension.toLowerCase();
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
		if (extension.equals("opf")) return false;
		if (extension.contains(";")) return false;
		if (extension.contains(" ")) return false;
		if (extension.contains(".")) return false;
		return true;
	}

	public static boolean addFormat(String extension) {
		extension = extension.toLowerCase();
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
		extension = extension.toLowerCase();
		ZLStringOption formats = new ZLStringOption("Formats", "ExternalFormats", "");
		String s = formats.getValue();
		if (s.equals(extension)) {
			s = "";
			formats.setValue(s);
			return;
		}
		if (s.startsWith(extension + ";")) {
			s = s.substring(extension.length() + 1);
			formats.setValue(s);
			return;
		}
		if (s.endsWith(";" + extension)) {
			s = s.substring(0, s.length() - extension.length() - 1);
			formats.setValue(s);
			return;
		}
		s = s.replace(";" + extension + ";", ";");
		formats.setValue(s);
	}

	public static ZLStringOption extensionOption(String extension) {
		extension = extension.toLowerCase();
		if (getNativeFormats().contains(extension)) {
			return new ZLStringOption("Formats", extensionToOption(extension), "org.geometerplus.zlibrary.ui.android");
		} else {
			ZLStringOption opt = new ZLStringOption("Formats", extensionToOption(extension), "");
			return new ZLStringOption("Formats", extensionToOption(extension), "");
		}
	}

	public static int getStatus(String extension) {
		extension = extension.toLowerCase();
		String pkg = extensionOption(extension).getValue();
		if (pkg.equals("org.geometerplus.zlibrary.ui.android")) return NATIVE;
		if (pkg.equals("")) return UNDEFINED;
		return EXTERNAL;
	}


}
