package org.teknux.dropbitz.test.config;

import org.skife.config.Config;
import org.skife.config.Default;
import org.skife.config.DefaultNull;

public interface Configuration {

    @Config("key1")
    @Default("defaultValue1")
    String getKey1();
    
    @Config("key2")
    @DefaultNull
    String getKey2();
    
    @Config("key3")
    @Default("defaultValue3")
    String getKey3();
    
    @Config("key4")
    @DefaultNull
    String getKey4();
}
