package org.teknux.dropbitz.freemarker;

public enum View {
    AUTH("/main/auth"),
    ADMIN("/admin/admin"),
    DROP("/drop/drop"),
    FALLBACK("/drop/fallback");
    
    private String templateName;

    View(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateName() {
        return templateName;
    }
}
