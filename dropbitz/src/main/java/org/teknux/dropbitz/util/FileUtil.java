/*
 * Copyright (C) 2014 TekNux.org
 *
 * This file is part of the dropbitz Community GPL Source Code.
 *
 * dropbitz Community Source Code is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dropbitz Community Source Code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dropbitz Community Source Code.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teknux.dropbitz.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

public class FileUtil {

    private static final int ONE_KILOBYTE_IN_BYTE = 1024;
    private static final String[] FILE_SIZE_UNITS = new String[] { "B", "KiB", "MiB", "GiB", "TiB" };
    private static final DecimalFormat FILE_SIZE_FORMAT = new DecimalFormat("#,##0.#");

    /**
     * Get file from Absolute or Relative Path
     * 
     * @param workingDirectory
     * @param path Absolute or Relative
     * @return File
     */
    public static File getFile(String workingDirectory, String path) {
        if(new File(Objects.requireNonNull(path, "Path can not be null")).isAbsolute() || workingDirectory == null) {
            return new File(path);
        } else {
            return new File(workingDirectory, path);
        }
    }
    
    /**
     * Check if file is on directory or on sub-directory of specified directory
     * 
     * @param file
     * @param directory
     * @return If file is a child of directory
     * @throws IOException
     */
    public static boolean isChildOfDirectory(File file, File directory) throws IOException {
        if (file == null || directory == null) {
            return false;
        }

        return file.getCanonicalPath().startsWith(directory.getCanonicalPath() + File.separator);
    }

    /**
     * Utility method to format a given size in Byte to a human readable format.
     * 1 : "1 B"
     * 1024 : "1 KiB"
     * 2537253 : "2.3 MiB"
     *
     * @param sizeInByte
     * @return
     */
    public static String formatSize(long sizeInByte) {
        if (sizeInByte <= 0) {
            return "0";
        }

        final int digitGroups = (int) (Math.log10(sizeInByte) / Math.log10(ONE_KILOBYTE_IN_BYTE));
        return FILE_SIZE_FORMAT.format(sizeInByte / Math.pow(ONE_KILOBYTE_IN_BYTE, digitGroups)) + " " + FILE_SIZE_UNITS[digitGroups];
    }
}
