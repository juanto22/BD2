package com.org.web.config.security;

import javax.enterprise.event.Observes;

import org.picketlink.config.SecurityConfigurationBuilder;
import org.picketlink.event.SecurityConfigurationEvent;

import com.org.security.enums.GroupsSecurityRolesNames;
/**
 * 
 * @author saca
 * Initial Security Path Configurations
 */
public class HttpSecurityConfiguration {

	public void configureHttpSecurity(@Observes SecurityConfigurationEvent event){
		SecurityConfigurationBuilder builder = event.getBuilder();
		
		builder
         .http()
         	.forGroup("Authentication") // group definition  
                 .authenticateWith()
                     .form()
                         .authenticationUri("/login.xhtml")
                         .loginPage("/login.xhtml")
                         .errorPage("/error.xhtml")
                         .restoreOriginalRequest()
             .forPath("/*.xhtml","Authentication")
             .forPath("/logout", "Authentication")
             	.logout()
             	.redirectTo("/login.xhtml")
             .forPath("/tec/*","Authentication") //estudent
             	.authorizeWith()
             		.group(GroupsSecurityRolesNames.TEC.getCode())
             			.redirectTo("/errors/access-denied.xhtml")
             				.whenForbidden()
              .forPath("/guest/*","Authentication") //teacher
              	.authorizeWith()
              		.group(GroupsSecurityRolesNames.GUEST.getCode())
              			.redirectTo("/errors/access-denied.xhtml")
              				.whenForbidden()
              .forPath("/security/*","Authentication") //Admins
              	.authorizeWith()
              		.group(GroupsSecurityRolesNames.ADMINS.getCode())
              			.redirectTo("/errors/access-denied.xhtml")
              				.whenForbidden()
              .forPath("/administration/*","Authentication") //Manager
              	.authorizeWith()
              		.group(GroupsSecurityRolesNames.ADMINS.getCode())
              			.redirectTo("/errors/access-denied.xhtml")
              				.whenForbidden()
             .forPath("/javax.faces.resource/*")
                 .unprotected();	 
	}	
}
