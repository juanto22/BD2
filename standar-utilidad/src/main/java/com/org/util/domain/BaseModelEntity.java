package com.org.util.domain;

import java.io.Serializable;

public interface BaseModelEntity<ID> extends Serializable {
	
	public static final String SEQ_STORE = "SEQ_STORE";
	
	public ID getId();
	
	public void setId(ID id);
	
	
}
