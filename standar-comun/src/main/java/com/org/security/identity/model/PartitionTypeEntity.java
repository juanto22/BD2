package com.org.security.identity.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.Identifier;
import org.picketlink.idm.jpa.annotations.PartitionClass;
import org.picketlink.idm.jpa.annotations.entity.ConfigurationName;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.model.Partition;

import lombok.Getter;
import lombok.Setter;

@IdentityManaged(Partition.class)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class PartitionTypeEntity {

	@Identifier
	@Id
	private String id;

	@AttributeValue
	private String name;

	@PartitionClass
	private String typeName;

	@ConfigurationName
	private String configurationName;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!getClass().isInstance(obj)) {
			return false;
		}

		PartitionTypeEntity other = (PartitionTypeEntity) obj;

		return getId() != null && other.getId() != null && getId().equals(other.getId());
	}

	@Override
	public int hashCode() {
		int result = getId() != null ? getId().hashCode() : 0;
		result = 31 * result + (getId() != null ? getId().hashCode() : 0);
		return result;
	}

}
