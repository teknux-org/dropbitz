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

import java.security.NoSuchAlgorithmException;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.teknux.dropbitz.util.Md5Util;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Md5UtilTest {

    @Test
    public void test01Md5Sum() throws NoSuchAlgorithmException {
       Assert.assertEquals("5d41402abc4b2a76b9719d911017c592", Md5Util.hash("hello"));
       Assert.assertEquals("7d793037a0760186574b0282f2f435e7", Md5Util.hash("world"));
       Assert.assertEquals("5eb63bbbe01eeed093cb22bb8f5acdc3", Md5Util.hash("hello world"));
    }
}
