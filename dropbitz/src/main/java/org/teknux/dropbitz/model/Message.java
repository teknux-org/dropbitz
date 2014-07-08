package org.teknux.dropbitz.model;

public class Message {   
    private String id = null;
    private String message = null;
    private Type type = null;
    private boolean closable = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isClosable() {
        return closable;
    }

    public void setClosable(boolean closable) {
        this.closable = closable;
    }

    public enum Type {
        SUCCESS("success"),
        INFO("info"),
        WARNING("warning"),
        DANGER("danger");
        
        private String cssClass;

        Type(String cssClass) {
            this.cssClass = cssClass;
        }

        public String getCssClass() {
            return cssClass;
        }        
    }
}
