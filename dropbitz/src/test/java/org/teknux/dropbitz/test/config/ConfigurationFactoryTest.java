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

package org.teknux.dropbitz.test.config;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runners.MethodSorters;
import org.teknux.dropbitz.config.ConfigurationFactory;
import org.teknux.dropbitz.exception.ConfigurationException;
import org.teknux.dropbitz.util.PathUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigurationFactoryTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();
    
    @Test
    public void test01ConfigFile() throws ConfigurationException {
        ConfigurationFactory<Configuration> configurationFactory = new ConfigurationFactory<Configuration>(Configuration.class);
        configurationFactory.setConfigFileBasePath(PathUtil.getJarDir() + File.separator + "test-classes" + File.separator + "config" + File.separator + "file");
        configurationFactory.setConfigResourceBasePath("/config"); //Nothing here
        
        Configuration configuration = configurationFactory.buildConfiguration();
        
        Assert.assertEquals("value1",  configuration.getKey1());
        Assert.assertEquals("value2",  configuration.getKey2());
        Assert.assertEquals("defaultValue3",  configuration.getKey3());
        Assert.assertEquals(null,  configuration.getKey4());
    }
    
    @Test
    public void test02ConfigResource() throws ConfigurationException, IOException {
        ConfigurationFactory<Configuration> configurationFactory = new ConfigurationFactory<Configuration>(Configuration.class);
        configurationFactory.setConfigFileBasePath(temporaryFolder.newFolder().getPath()); //Nothing here
        configurationFactory.setConfigResourceBasePath("/config/resource");
        
        Configuration configuration = configurationFactory.buildConfiguration();
        
        Assert.assertEquals("resValue1",  configuration.getKey1());
        Assert.assertEquals("resValue2",  configuration.getKey2());
        Assert.assertEquals("defaultValue3",  configuration.getKey3());
        Assert.assertEquals(null,  configuration.getKey4());
    }
    
    @Test
    public void test03ConfigBoth() throws ConfigurationException {
        ConfigurationFactory<Configuration> configurationFactory = new ConfigurationFactory<Configuration>(Configuration.class);
        configurationFactory.setConfigFileBasePath(PathUtil.getJarDir() + File.separator + "test-classes" + File.separator + "config" + File.separator + "file");
        configurationFactory.setConfigResourceBasePath("/config/resource");
        
        Configuration configuration = configurationFactory.buildConfiguration();
        
        Assert.assertEquals("value1",  configuration.getKey1());
        Assert.assertEquals("value2",  configuration.getKey2());
        Assert.assertEquals("defaultValue3",  configuration.getKey3());
        Assert.assertEquals(null,  configuration.getKey4());
    }
    
    @Test
    public void test04CreateConfig() throws ConfigurationException, IOException {
        ConfigurationFactory<Configuration> configurationFactory = new ConfigurationFactory<Configuration>(Configuration.class);
        configurationFactory.setConfigFileBasePath(temporaryFolder.newFolder().getPath());
        configurationFactory.setConfigResourceBasePath("/config/resourcedist");
        
        Configuration configuration = configurationFactory.buildConfiguration();
        
        Assert.assertEquals("distValue1",  configuration.getKey1());
        Assert.assertEquals("distValue2",  configuration.getKey2());
        Assert.assertEquals("defaultValue3",  configuration.getKey3());
        Assert.assertEquals(null,  configuration.getKey4());
    }
}
