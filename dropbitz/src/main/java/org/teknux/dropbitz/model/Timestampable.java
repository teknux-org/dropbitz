package org.teknux.dropbitz.model;

import java.io.Serializable;
import java.util.Date;


public class Timestampable implements
		Serializable {

	private static final long serialVersionUID = 1L;

	private Date createDate;

	private Date updateDate;

	public Date getCreateDate() {
		return createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	protected void updateCreationDate() {
		if (createDate == null) {
			createDate = new Date();
		}
	}

	protected void updateModificationDate() {
		updateDate = new Date();
	}
}
