package org.teknux.dropbitz.model.view;

public class DropEmailModel {
	public String name;
	public String fileName;
	public String finalFileName;
	public boolean success;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFinalFileName() {
		return finalFileName;
	}

	public void setFinalFileName(String finalFileName) {
		this.finalFileName = finalFileName;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
}
