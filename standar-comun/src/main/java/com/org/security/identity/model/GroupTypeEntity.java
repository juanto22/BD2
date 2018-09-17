package com.org.security.identity.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

import com.org.security.identity.stereotype.Group;

import lombok.Getter;
import lombok.Setter;

@IdentityManaged(Group.class)
@Entity
@Getter
@Setter
public class GroupTypeEntity extends AbstractIdentityTypeEntity {

	@AttributeValue
	private String name;

	@AttributeValue
	@ManyToOne(fetch = FetchType.EAGER)
	private GroupTypeEntity parent;

	@OwnerReference
	@ManyToOne(fetch = FetchType.EAGER)
	private PartitionTypeEntity partition;

}
