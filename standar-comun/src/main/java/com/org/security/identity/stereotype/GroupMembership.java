package com.org.security.identity.stereotype;

import static org.picketlink.idm.model.annotation.RelationshipStereotype.Stereotype.GROUP_MEMBERSHIP;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.RELATIONSHIP_GROUP_MEMBERSHIP_GROUP;
import static org.picketlink.idm.model.annotation.StereotypeProperty.Property.RELATIONSHIP_GROUP_MEMBERSHIP_MEMBER;

import org.picketlink.idm.model.AbstractAttributedType;
import org.picketlink.idm.model.Account;
import org.picketlink.idm.model.Relationship;
import org.picketlink.idm.model.annotation.InheritsPrivileges;
import org.picketlink.idm.model.annotation.RelationshipStereotype;
import org.picketlink.idm.model.annotation.StereotypeProperty;
import org.picketlink.idm.query.RelationshipQueryParameter;

@RelationshipStereotype(GROUP_MEMBERSHIP)
public class GroupMembership extends AbstractAttributedType implements Relationship {
	
	public static final RelationshipQueryParameter MEMBER = RELATIONSHIP_QUERY_ATTRIBUTE.byName("member");
	public static final RelationshipQueryParameter GROUP = RELATIONSHIP_QUERY_ATTRIBUTE.byName("group");

	private Account member;
	private Group group;

	private GroupMembership() {
		this(null, null);
	}

	public GroupMembership(Account member, Group group) {
		this.member = member;
		this.group = group;
	}

	@InheritsPrivileges("group")
	@StereotypeProperty(RELATIONSHIP_GROUP_MEMBERSHIP_MEMBER)
	public Account getMember() {
		return member;
	}

	public void setMember(Account member) {
		this.member = member;
	}

	@StereotypeProperty(RELATIONSHIP_GROUP_MEMBERSHIP_GROUP)
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}
