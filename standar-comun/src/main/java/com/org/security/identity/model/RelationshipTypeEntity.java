package com.org.security.identity.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.RelationshipClass;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.model.Relationship;

import lombok.Getter;
import lombok.Setter;

@IdentityManaged(Relationship.class)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class RelationshipTypeEntity {

	@Identifier
	@Id
	private String id;

	@RelationshipClass
	private String typeName;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!getClass().isInstance(obj)) {
			return false;
		}

		RelationshipTypeEntity other = (RelationshipTypeEntity) obj;

		return getId() != null && other.getId() != null && getId().equals(other.getId());
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getId() != null ? getId().hashCode() : 0);
		return result;
	}

}
