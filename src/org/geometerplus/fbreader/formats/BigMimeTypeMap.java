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

package org.geometerplus.fbreader.formats;

import java.util.*;
import org.geometerplus.zlibrary.core.xml.*;
import org.geometerplus.zlibrary.core.filesystem.*;

public abstract class BigMimeTypeMap {
	private static HashMap<String, String> ourMap = new HashMap<String, String>();

	public static String getType(String ext) {
		return ourMap.get(ext);
	}

	private static void setType(String ext, String type) {
		ourMap.put(ext, type);
	}

	private static class MimeTypeReader extends ZLXMLReaderAdapter {

		@Override
		public boolean startElementHandler(String tag, ZLStringMap attributes) {
			if ("file".equals(tag)) {
				final String ext = attributes.getValue("ext");
				final String type = attributes.getValue("type");
				if (ext != null && type != null) {
					BigMimeTypeMap.setType(ext, type);
				}
			}
			return false;
		}
	}

	static {
		new MimeTypeReader().read(ZLFile.createFileByPath("mimetypes/mimetypes.xml"));
	}
}
