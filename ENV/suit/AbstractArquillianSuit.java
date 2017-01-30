package com.sargent.disc.arquillian.suit;

import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.runner.RunWith;

import com.sargent.disc.dao.SmartDAO;
import com.sargent.disc.domain.model.InvitationEmp;
import com.sargent.disc.services.GenericInvitationEmpService;
import com.sargent.disc.util.Resources;


@RunWith(Arquillian.class)
public  abstract class  AbstractArquillianSuit {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test77.war")
				.addClasses(InvitationEmp.class, SmartDAO.class, GenericInvitationEmpService.class,Resources.class)
				.addPackage("com.sargent.disc.dao")
				.addPackage("com.sargent.disc.business.model")
				.addPackage("com.sargent.disc.domain.model")
				.addPackage("com.sargent.disc.services")
				.addPackage("com.sargent.disc.rest")
				.addPackage("com.sargent.disc.test")
				.addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	//@Inject
	//GenericInvitationEmpService service;

	@Inject
	public Logger log;

	
}

//mvn -e clean test -Parq-jbossas-managed
// mvn install  jboss-as:deploy
