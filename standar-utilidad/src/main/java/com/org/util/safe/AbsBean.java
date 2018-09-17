package com.org.util.safe;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public abstract class AbsBean implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4803121085172949025L;

	@Getter
	@Setter
	private boolean any;

	@Getter
	@Setter
	private int pageSize;

}
