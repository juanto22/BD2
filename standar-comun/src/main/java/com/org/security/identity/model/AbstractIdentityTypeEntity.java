package com.org.security.identity.model;

import java.util.Date;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.IdentityClass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractIdentityTypeEntity {

	@Identifier
	@Id
	private String id;

	@IdentityClass
	private String typeName;

	@Temporal(TemporalType.TIMESTAMP)
	@AttributeValue
	private Date createdDate;

	@Temporal(TemporalType.TIMESTAMP)
	@AttributeValue
	private Date expirationDate;

	@AttributeValue
	private boolean enabled;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!getClass().isInstance(obj)) {
			return false;
		}

		AbstractIdentityTypeEntity other = (AbstractIdentityTypeEntity) obj;

		return getId() != null && other.getId() != null && getId().equals(other.getId()) && getTypeName() != null
				&& other.getTypeName() != null && getTypeName().equals(other.getTypeName());
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getId() != null ? getId().hashCode() : 0);
		return result;
	}

}
