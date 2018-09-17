package com.org.web.security.views;

import java.io.Serializable;

import com.org.util.enumeration.ViewStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class SecurityBaseView implements Serializable {

	private static final long serialVersionUID = -97652374532L;

	// NONE default view status
	private int statusView = 0;

	public ViewStatus getStatus() {
		return ViewStatus.getViewStatus(this.statusView);
	}

	public void setStatus(ViewStatus tipoEtapa) {
		this.statusView = tipoEtapa.getCode();
	}

}
