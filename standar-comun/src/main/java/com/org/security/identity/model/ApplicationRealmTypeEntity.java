package com.org.security.identity.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

import com.org.security.identity.stereotype.ApplicationRealm;

@IdentityManaged(ApplicationRealm.class)
@Entity
@Table(name="APPLICATIONREALMTYPEENTITY")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")
public class ApplicationRealmTypeEntity extends PartitionTypeEntity{

}
