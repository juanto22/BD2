package com.org.security.identity.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

import com.org.security.identity.stereotype.ApplicationAccess;

import lombok.Getter;
import lombok.Setter;

@IdentityManaged(ApplicationAccess.class)
@Entity
@Table(name="APPLICATIONACCESSTYPEENTITY")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")
@Getter
@Setter
public class ApplicationAccessTypeEntity extends RelationshipTypeEntity {

	@AttributeValue
	private Date lastSuccessfulLogin;

	@AttributeValue
	private Date lastFailedLogin;

	@AttributeValue
	private int failedLoginAttempts;

}
