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

package org.teknux.dropbitz.test.util;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.teknux.dropbitz.util.FileUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class FileUtilTest {

    @Test
    public void formatSizeTest() {
        Assert.assertEquals(FileUtil.formatSize(0L), "0");
        Assert.assertEquals(FileUtil.formatSize(1L), "1 B");
        Assert.assertEquals(FileUtil.formatSize(1024L), "1 KiB");
        Assert.assertEquals(FileUtil.formatSize(1024L*1024L), "1 MiB");
        Assert.assertEquals(FileUtil.formatSize(1024L*1024L*1024L), "1 GiB");
        Assert.assertEquals(FileUtil.formatSize(1024L*1024L*1024L*1024L), "1 TiB");
    }

    @Test 
    public void test01getFile() throws IOException {
        if (! isWindowsOs()) {
            Assert.assertEquals("/workDir/file.txt", FileUtil.getFile("/workDir", "file.txt").getCanonicalPath());
            Assert.assertEquals("/workDir/file.txt", FileUtil.getFile("/workDir/", "file.txt").getCanonicalPath());
            Assert.assertEquals("/file.txt", FileUtil.getFile("/toto", "/file.txt").getCanonicalPath());
            Assert.assertEquals("/file.txt", FileUtil.getFile("/toto/", "/file.txt").getCanonicalPath());
        } else {
            Assert.assertEquals("C:\\workDir\\file.txt", FileUtil.getFile("C:\\workDir", "file.txt").getCanonicalPath());
            Assert.assertEquals("C:\\workDir\\file.txt", FileUtil.getFile("C:\\workDir\\", "file.txt").getCanonicalPath());
            Assert.assertEquals("C:\\file.txt", FileUtil.getFile("C:\\workDir", "C:\\file.txt").getCanonicalPath());
            Assert.assertEquals("C:\\file.txt", FileUtil.getFile("C:\\workDir\\", "C:\\file.txt").getCanonicalPath());
        }
    }
    
    @Test 
    public void test02isChildOfDirectory() throws IOException {
        if (! isWindowsOs()) {
            Assert.assertFalse((FileUtil.isChildOfDirectory(new File("/workDir/file.txt"), new File("/workDir2"))));
            Assert.assertFalse((FileUtil.isChildOfDirectory(new File("/workDirfile.txt"), new File("/workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("/workDir/file.txt"), new File("/workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("/workDir/./file.txt"), new File("/workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("/workDir/../workDir/file.txt"), new File("/workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("/workDir/file.txt"), new File("/workDir/../workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("/workDir/../workDir/file.txt"), new File("/workDir/../workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("/workDir/other/file.txt"), new File("/workDir"))));
            
            Assert.assertFalse((FileUtil.isChildOfDirectory(new File("workDir/file.txt"), new File("workDir2"))));
            Assert.assertFalse((FileUtil.isChildOfDirectory(new File("workDirfile.txt"), new File("workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir/file.txt"), new File("workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir/./file.txt"), new File("workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir/../workDir/file.txt"), new File("workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir/file.txt"), new File("workDir/../workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir/../workDir/file.txt"), new File("workDir/../workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir/other/file.txt"), new File("workDir"))));
        } else {
            Assert.assertFalse((FileUtil.isChildOfDirectory(new File("C:\\workDir\\file.txt"), new File("C:\\workDir2"))));
            Assert.assertFalse((FileUtil.isChildOfDirectory(new File("C:\\workDirfile.txt"), new File("C:\\workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("C:\\workDir\\file.txt"), new File("C:\\workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("C:\\workDir\\.\\file.txt"), new File("C:\\workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("C:\\workDir\\..\\workDir\\file.txt"), new File("C:\\workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("C:\\workDir\\file.txt"), new File("C:\\workDir\\..\\workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("C:\\workDir\\..\\workDir\\file.txt"), new File("C:\\workDir\\..\\workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("C:\\workDir\\other\\file.txt"), new File("C:\\workDir"))));
            
            Assert.assertFalse((FileUtil.isChildOfDirectory(new File("workDir\\file.txt"), new File("workDir2"))));
            Assert.assertFalse((FileUtil.isChildOfDirectory(new File("workDirfile.txt"), new File("workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir\\file.txt"), new File("workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir\\.\\file.txt"), new File("workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir\\..\\workDir\\file.txt"), new File("workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir\\file.txt"), new File("workDir\\..\\workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir\\..\\workDir\\file.txt"), new File("workDir\\..\\workDir"))));
            Assert.assertTrue((FileUtil.isChildOfDirectory(new File("workDir\\other\\file.txt"), new File("workDir"))));
        }
    }
  
    private static boolean isWindowsOs() {
        String osName = System.getProperty("os.name").toLowerCase();

        return (osName.indexOf("win") >= 0);
    }
}
