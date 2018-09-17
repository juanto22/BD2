package com.org.security.identity.stereotype;

import java.util.Date;

import org.picketlink.idm.model.AbstractAttributedType;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.Relationship;
import org.picketlink.idm.model.annotation.AttributeProperty;
import org.picketlink.idm.model.annotation.InheritsPrivileges;
import org.picketlink.idm.query.RelationshipQueryParameter;

public class ApplicationAccess extends AbstractAttributedType implements Relationship {

	public static final RelationshipQueryParameter ASSIGNEE = RELATIONSHIP_QUERY_ATTRIBUTE.byName("assignee");
	public static final RelationshipQueryParameter APPLICATION = RELATIONSHIP_QUERY_ATTRIBUTE.byName("application");

	@InheritsPrivileges("application")
	private IdentityType assignee;

	private Application application;

	@AttributeProperty
	private Date lastSuccessfulLogin;

	@AttributeProperty
	private Date lastFailedLogin;

	@AttributeProperty
	private int failedLoginAttempts;

	private ApplicationAccess() {
	}

	public ApplicationAccess(IdentityType assignee, Application application) {
		setAssignee(assignee);
		setApplication(application);
	}

	public IdentityType getAssignee() {
		return this.assignee;
	}

	public void setAssignee(IdentityType assignee) {
		// only users and groups can be assigned
		if (assignee != null && !User.class.isAssignableFrom(assignee.getClass())
				&& !Group.class.isAssignableFrom(assignee.getClass())) {
			throw new IllegalArgumentException("Assignee can be only an User or a Group.");
		}

		this.assignee = assignee;
	}

	public Application getApplication() {
		return this.application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public Date getLastSuccessfulLogin() {
		return this.lastSuccessfulLogin;
	}

	public void setLastSuccessfulLogin(Date lastSuccessfulLogin) {
		this.lastSuccessfulLogin = lastSuccessfulLogin;
	}

	public Date getLastFailedLogin() {
		return this.lastFailedLogin;
	}

	public void setLastFailedLogin(Date lastFailedLogin) {
		this.lastFailedLogin = lastFailedLogin;
	}

	public int getFailedLoginAttempts() {
		return this.failedLoginAttempts;
	}

	public void setFailedLoginAttempts(int failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}

}
