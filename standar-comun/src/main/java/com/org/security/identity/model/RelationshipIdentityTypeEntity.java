package com.org.security.identity.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.RelationshipDescriptor;
import org.picketlink.idm.jpa.annotations.RelationshipMember;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.model.Relationship;

import lombok.Getter;
import lombok.Setter;

@IdentityManaged({ Relationship.class })
@Entity
@Getter
@Setter
public class RelationshipIdentityTypeEntity implements Serializable {

	@Id
	@GeneratedValue
	private Long identifier;

	@RelationshipDescriptor
	private String descriptor;

	@RelationshipMember
	private String identityType;

	@OwnerReference
	@ManyToOne(fetch = FetchType.EAGER)
	private RelationshipTypeEntity owner;

}
