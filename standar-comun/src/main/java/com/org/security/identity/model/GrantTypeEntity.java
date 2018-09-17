package com.org.security.identity.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;

import com.org.security.identity.stereotype.Grant;

@IdentityManaged(Grant.class)
@Entity
@Table(name="GRANTTYPEENTITY")
@PrimaryKeyJoinColumn(name="ID", referencedColumnName="ID")
public class GrantTypeEntity extends RelationshipTypeEntity{

}
