package com.org.security.identity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.OwnerReference;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

import com.org.security.identity.stereotype.User;

import lombok.Getter;
import lombok.Setter;

@IdentityManaged(User.class)
@Entity
@Getter
@Setter
public class UserTypeEntity extends AbstractIdentityTypeEntity {

	@AttributeValue
	@Column
	private String userName;

	@OwnerReference
	@ManyToOne(fetch = FetchType.EAGER)
	private RealmTypeEntity realm;

	@AttributeValue
	@Column
	private String firstName;

	@AttributeValue
	@Column
	private String lastName;

	@AttributeValue
	@Column
	private String email;

	@AttributeValue
	@Column(length = 255)
	private String middleName;

	@AttributeValue
	@Column(length = 25)
	private String telephone;

	@AttributeValue
	@Column(length = 500)
	private String address;

	@AttributeValue
	@Column
	private int postIndex;

	@AttributeValue
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastVisitDate;

}
