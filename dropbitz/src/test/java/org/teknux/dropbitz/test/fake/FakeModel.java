package org.teknux.dropbitz.test.fake;

import org.teknux.dropbitz.model.view.Model;

public class FakeModel extends Model{

    private String varTest;
   
    public FakeModel(String varTest) {
        this.varTest = varTest;
    }

    public String getVarTest() {
        return varTest;
    }

    public void setVarTest(String varTest) {
        this.varTest = varTest;
    }
}
