package com.org.security.identity.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

import com.org.security.identity.stereotype.GroupMembership;

@IdentityManaged(GroupMembership.class)
@Entity
@Table(name="GROUPMEMBERSHIPTYPEENTITY")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")
public class GroupMembershipTypeEntity extends RelationshipTypeEntity {

}