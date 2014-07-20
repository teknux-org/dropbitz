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
import java.util.Objects;

public class FileUtil {

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
}
