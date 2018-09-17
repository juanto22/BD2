package com.org.security.identity.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.picketlink.idm.credential.storage.EncodedPasswordStorage;
import org.picketlink.idm.jpa.annotations.CredentialClass;
import org.picketlink.idm.jpa.annotations.CredentialProperty;
import org.picketlink.idm.jpa.annotations.EffectiveDate;
import org.picketlink.idm.jpa.annotations.ExpiryDate;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.ManagedCredential;

import lombok.Getter;
import lombok.Setter;

@ManagedCredential(EncodedPasswordStorage.class)
@Entity
@Getter
@Setter
public class PasswordCredentialTypeEntity {

	@Id
	@GeneratedValue
	private Long id;

	@OwnerReference
	@ManyToOne(fetch = FetchType.EAGER)
	private UserTypeEntity owner;

	@CredentialClass
	private String typeName;

	@Temporal(TemporalType.TIMESTAMP)
	@EffectiveDate
	private Date effectiveDate;

	@Temporal(TemporalType.TIMESTAMP)
	@ExpiryDate
	private Date expiryDate;

	@CredentialProperty(name = "encodedHash")
	private String passwordEncodedHash;

	@CredentialProperty(name = "salt")
	private String passwordSalt;

}
