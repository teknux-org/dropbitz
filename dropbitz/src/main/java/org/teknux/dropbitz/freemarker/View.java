package org.teknux.dropbitz.freemarker;

public enum View {
    AUTH("/main/auth"),
    DROP("/main/drop"),
    FALLBACK("/upload/fallback");
    
    private String templateName;

    View(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }
}
