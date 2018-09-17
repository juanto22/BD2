package com.org.web.config.security;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.picketlink.annotations.PicketLink;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.config.IdentityConfiguration;
import org.picketlink.idm.config.IdentityConfigurationBuilder;
import org.picketlink.idm.internal.DefaultPartitionManager;
import org.picketlink.idm.model.Partition;
import org.picketlink.internal.EntityManagerContextInitializer;

import com.org.security.enums.ApplicationRealmNames;
import com.org.security.identity.model.ApplicationAccessTypeEntity;
import com.org.security.identity.model.ApplicationRealmTypeEntity;
import com.org.security.identity.model.ApplicationTypeEntity;
import com.org.security.identity.model.GrantTypeEntity;
import com.org.security.identity.model.GroupMembershipTypeEntity;
import com.org.security.identity.model.GroupTypeEntity;
import com.org.security.identity.model.PartitionTypeEntity;
import com.org.security.identity.model.PasswordCredentialTypeEntity;
import com.org.security.identity.model.RealmTypeEntity;
import com.org.security.identity.model.RelationshipIdentityTypeEntity;
import com.org.security.identity.model.RelationshipTypeEntity;
import com.org.security.identity.model.RoleTypeEntity;
import com.org.security.identity.model.UserTypeEntity;
import com.org.security.identity.stereotype.Application;
import com.org.security.identity.stereotype.ApplicationAccess;
import com.org.security.identity.stereotype.ApplicationRealm;
import com.org.security.identity.stereotype.Grant;
import com.org.security.identity.stereotype.Group;
import com.org.security.identity.stereotype.GroupMembership;
import com.org.security.identity.stereotype.Realm;
import com.org.security.identity.stereotype.Role;
import com.org.security.identity.stereotype.User;

@ApplicationScoped
public class IDMConfiguration {

	@Inject
	private EntityManagerContextInitializer contextInitializer;

	private IdentityConfiguration identityConfig = null;

	protected static final String APPLICATION_NAME = "Manager students application";

	private PartitionManager partitionManager;
	private Realm realm;
	private Application application;
	private ApplicationRealm applicationPartition;
	
	
	@Produces
	  public IdentityConfiguration createConfig() throws Exception {
	    if (identityConfig == null) {
	      initConfig();

	      createDefaultRealm();
	      createApplication();
	    }
	    return identityConfig;
	  }
	
	
	@SuppressWarnings("unchecked")
	@Produces
	  public void initConfig() throws Exception {
	    IdentityConfigurationBuilder builder = new IdentityConfigurationBuilder();
	    builder
	        .named("default")
	        .stores()
	        .jpa()
	        .supportType(
	            User.class,
	            Role.class,
	            Group.class,
	            Realm.class,
	            Application.class,
	            ApplicationRealm.class)
	        .supportGlobalRelationship(
	            Grant.class,
	            GroupMembership.class,
	            ApplicationAccess.class)
	        .supportCredentials(true)
	        .mappedEntity(
	            ApplicationAccessTypeEntity.class,
	            ApplicationTypeEntity.class,
	            ApplicationRealmTypeEntity.class,
	            PartitionTypeEntity.class,
	            GrantTypeEntity.class,
	            GroupMembershipTypeEntity.class,
	            GroupTypeEntity.class,
	            RealmTypeEntity.class,
	            RoleTypeEntity.class,
	            UserTypeEntity.class,
	            PasswordCredentialTypeEntity.class,
	            RelationshipTypeEntity.class,
	            RelationshipIdentityTypeEntity.class)
	        .addContextInitializer(contextInitializer);
	    identityConfig = builder.build();
	    partitionManager = new DefaultPartitionManager(builder.buildAll());
	  }
	

	private void createApplication() throws Exception {
		Partition partition = partitionManager.getPartition(ApplicationRealm.class, APPLICATION_NAME);
		if (partition == null) {
			this.application = new Application(APPLICATION_NAME);

			IdentityManager identityManager = partitionManager.createIdentityManager(realm);

			identityManager.add(application);

			this.applicationPartition = new ApplicationRealm(APPLICATION_NAME);

			partitionManager.add(this.applicationPartition);
		}
	}

	private void createDefaultRealm() throws NoSuchAlgorithmException {
		Partition partition = getDefaultPartition();
		if (partition == null) {
			realm = new Realm(ApplicationRealmNames.SCHOOL_APPLICATION.getCode());

			realm.setEnforceSSL(true);

			KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();

			realm.setPrivateKey(keyPair.getPrivate().getEncoded());
			realm.setPublickKey(keyPair.getPublic().getEncoded());

			realm.setNumberFailedLoginAttempts(3);

			partitionManager.add(realm);
		}
	}

	@Produces
	@PicketLink
	public Partition getDefaultPartition() {
		return partitionManager.getPartition(Realm.class, ApplicationRealmNames.SCHOOL_APPLICATION.getCode());
	}

}
