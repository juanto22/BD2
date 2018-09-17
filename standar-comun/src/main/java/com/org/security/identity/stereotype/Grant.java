package com.org.security.identity.stereotype;

import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.RELATIONSHIP_GRANT_ASSIGNEE;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.RELATIONSHIP_GRANT_ROLE;
import static org.picketlink.idm.model.annotation.RelationshipStereotype.Stereotype.GRANT;

import org.picketlink.idm.model.AbstractAttributedType;
import org.picketlink.idm.model.IdentityType;
import org.picketlink.idm.model.Relationship;
import org.picketlink.idm.model.annotation.InheritsPrivileges;
import org.picketlink.idm.model.annotation.RelationshipStereotype;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.query.RelationshipQueryParameter;

@RelationshipStereotype(GRANT)
public class Grant extends AbstractAttributedType implements Relationship {

	public static final RelationshipQueryParameter ASSIGNEE = RELATIONSHIP_QUERY_ATTRIBUTE.byName("assignee");
	public static final RelationshipQueryParameter ROLE = RELATIONSHIP_QUERY_ATTRIBUTE.byName("role");

	@InheritsPrivileges("role")
	@StereotypeProperty(RELATIONSHIP_GRANT_ASSIGNEE)
	private IdentityType assignee;

	@StereotypeProperty(RELATIONSHIP_GRANT_ROLE)
	private Role role;

	private Grant() {
		this(null, null);
	}

	public Grant(IdentityType assignee, Role role) {
		this.assignee = assignee;
		this.role = role;
	}

	public IdentityType getAssignee() {
		return this.assignee;
	}

	public void setAssignee(IdentityType assignee) {
		this.assignee = assignee;
	}

	public Role getRole() {
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
