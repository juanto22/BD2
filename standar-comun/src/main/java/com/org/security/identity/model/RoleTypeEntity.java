package com.org.security.identity.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

import com.org.security.identity.stereotype.Role;

import lombok.Getter;
import lombok.Setter;

@IdentityManaged(Role.class)
@Entity
@Getter
@Setter
public class RoleTypeEntity extends AbstractIdentityTypeEntity {

	@AttributeValue
	private String name;

	@OwnerReference
	@ManyToOne(fetch = FetchType.EAGER)
	private PartitionTypeEntity partition;

}
