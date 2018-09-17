package com.org.security.identity.model;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

import com.org.security.identity.stereotype.Realm;

import lombok.Getter;
import lombok.Setter;

@IdentityManaged(Realm.class)
@Entity
@Table(name = "REALMTYPEENTITY")
@PrimaryKeyJoinColumn(name = "ID", referencedColumnName = "ID")
@Getter
@Setter
public class RealmTypeEntity extends PartitionTypeEntity {

	@AttributeValue
	private boolean enforceSSL;

	@AttributeValue
	private int numberFailedLoginAttempts;

	@AttributeValue
	@Column(nullable = true)
	@Lob
	@Basic(fetch = LAZY)
	private byte[] publickKey;

	@AttributeValue
	@Column(nullable = true)
	@Lob
	@Basic(fetch = LAZY)
	private byte[] privateKey;
}
