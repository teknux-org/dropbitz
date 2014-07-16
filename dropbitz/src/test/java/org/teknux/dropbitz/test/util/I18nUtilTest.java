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

import java.util.Locale;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.teknux.dropbitz.exception.I18nServiceException;
import org.teknux.dropbitz.util.I18nUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class I18nUtilTest {
    @Test
    public void test01BadLocaleFromString() throws I18nServiceException {
        try {
            I18nUtil.getLocaleFromString(null);
            Assert.fail("Should throw NPE");
        } catch (NullPointerException e){
        }
        try {
            I18nUtil.getLocaleFromString("");
            Assert.fail("Should throw NPE");
        } catch (NullPointerException e){
        }
        try {
            I18nUtil.getLocaleFromString("x");
            Assert.fail("Should throw I18nServiceException");
        } catch (I18nServiceException e){
        }
    }
    
    @Test
    public void test02LocaleFromString() throws I18nServiceException {
        Assert.assertEquals(Locale.ENGLISH, I18nUtil.getLocaleFromString(Locale.ENGLISH.toString()));
        Assert.assertEquals(Locale.FRENCH, I18nUtil.getLocaleFromString(Locale.FRENCH.toString()));
        Assert.assertEquals(Locale.US, I18nUtil.getLocaleFromString(Locale.US.toString()));
        Assert.assertEquals(Locale.FRANCE, I18nUtil.getLocaleFromString(Locale.FRANCE.toString()));
    }
}
