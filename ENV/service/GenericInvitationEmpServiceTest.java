package com.sargent.disc.arquillian.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sargent.disc.dao.SmartDAO;
import com.sargent.disc.domain.model.InvitationEmp;
import com.sargent.disc.services.GenericInvitationEmpService;
import com.sargent.disc.util.Resources;


@RunWith(Arquillian.class)
public class GenericInvitationEmpServiceTest {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test66.war")
				.addClasses(InvitationEmp.class, SmartDAO.class, GenericInvitationEmpService.class,Resources.class)
				.addPackage("com.sargent.disc.dao")
				.addPackage("com.sargent.disc.business.model")
				.addPackage("com.sargent.disc.domain.model")
				.addPackage("com.sargent.disc.services")
				.addPackage("com.sargent.disc.test")
				.addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	}

	@Inject
	GenericInvitationEmpService service;

	@Inject
	Logger log;

	InvitationEmp emp,emp1;

	@BeforeClass
	public static  void setUpClass() throws Exception {

	}

	@After
	public   void tearDownClass() throws Exception {
		try{ 
			if(emp!= null)
				service.delete(emp.getId().longValue());

			if(emp1!= null)
				service.delete(emp1.getId().longValue());

			log.log(Level.INFO, "deleteing all");
		}catch(Exception io){
			io.printStackTrace();
		}
	}

	@Test
	@InSequence(0) 
	public void doesTheInvitationServiceWork() throws Exception {
		//creating
		log.log(Level.INFO, "creating full");
		emp = service.create("FIrstname","sureName","gender","address","postcode","country","citizenship","residence","passportno", "2010-10-10","sumncc@gamil.com","paPs1o123456");
		assertNotNull("Emp is not created",emp);

		//find
		Map <String, Object>  param = new HashMap<String, Object>();
		param.put("firstName", "FIrstname");		
		List<InvitationEmp> list = service.getDao().typedFindbyModel(InvitationEmp.class, InvitationEmp.list_by_FirstName, 0, -1, param);

		assertNotNull("Not found in the list",list);
		assertNotNull("Not found in the list",list.get(0));

		if(list.get(0) instanceof InvitationEmp == false) 
			fail("Not an object of InvitationEmp");
		emp = (InvitationEmp) list.get(0);
		emp.setFirtName("XXXX");

		//upadting
		InvitationEmp emp2 = service.update(emp.getId().longValue(), emp.getFirtName(), "sureName","gender","address","postcode","country","citizenship","residence","passportno", "2010-10-10","sumncc@gamil.com","paPs1o123456");
		assertNotNull("Not updates",emp2);
		assertSame("Not updated the name","XXXX",emp2.getFirtName());

		//deleting (when deleted the it returns 1 or -1 when not)
		int isDelete =service.delete(emp2.getId().longValue());
		assertEquals("Not deleted",isDelete,1);

	}

	@Test
	@InSequence(1) 
	public void doTheValidationsWork() throws Exception {
		log.log(Level.INFO, "creating with wrong email");
		emp1 = service.create("FIrstname","sureName","gender","address","postcode","country","citizenship","residence","passportno", "2010-10-10","sumncc","paPs1o123456");
		assertNull("creating with wrong email",emp1);

		emp1 =null;
		log.log(Level.INFO, "creating with wrong dob");
		emp1 = service.create("FIrstname","sureName","gender","address","postcode","country","citizenship","residence","passportno", "date","sumncc@yahoo.com","paPs1o123456");
		assertNull("creating with wrong dob",emp1);

		emp1 =null;
		log.log(Level.INFO, "creating with null firtname");
		emp1 =  service.create("","sureName","gender","address","postcode","country","citizenship","residence","passportno", "2010-10-10","sumncc@yahoo","paPs1o123456");
		assertNull("creating with null firtname",emp1);

		emp1 =null;
		log.log(Level.INFO, "creating with  firtname+ number");
		emp1 =  service.create("1222","sureName","gender","address","postcode","country","citizenship","residence","passportno", "2010-10-10","sumncc@yahoo","paPs1o123456");
		assertNull("creating with null  firtname+ number",emp1);
		
		emp1 =null;
		log.log(Level.INFO, "creating with null surename");
		emp1 =  service.create("FIrstname","","gender","address","postcode","country","citizenship","residence","passportno", "2010-10-10","sumncc@yahoo","paPs1o123456");
		assertNull("creating with null surename",emp1);

		emp1 =null;
		log.log(Level.INFO, "creating emap when country with number");
		emp1 = service.create("FIrstname","sureName","gender","address","postcode","country1","citizenship","residence","passportno", "2010-10-10","sumncc@gamil.com","paPs1o123456");
		assertNull("creating country with number",emp1);
		
		
	}
	
	@Test
	@InSequence(3) 
	public void doTheValidationsWorkOnPassword() throws Exception {
		emp1 =null;
		log.log(Level.INFO, "creating   password with no number");
		emp1 =  service.create("FirstName","sureName","gender","address","postcode","country","citizenship","residence","passportno", "2010-10-10","sumncc@yahoo","password");
		assertNull("creating   password with no number",emp1);
		
		emp1 =null;
		log.log(Level.INFO, "creating   password with no Capital letter");
		emp1 =  service.create("FirstName","sureName","gender","address","postcode","country","citizenship","residence","passportno", "2010-10-10","sumncc@yahoo","password123");
		assertNull("creating with  password no  Capital letter",emp1);
		
		emp1 =null;
		log.log(Level.INFO, "creating   password with no small letter");
		emp1 =  service.create("FirstName","sureName","gender","address","postcode","country","citizenship","residence","passportno", "2010-10-10","sumncc@yahoo","PASSWORD123");
		assertNull("creating   password with no  small letter",emp1);
		
		emp1 =null;
		log.log(Level.INFO, "creating   password with short length");
		emp1 =  service.create("FirstName","sureName","gender","address","postcode","country","citizenship","residence","passportno", "2010-10-10","sumncc@yahoo","Pa12");
		assertNull("creating   password with  short length",emp1);

	}

	@Test
	@InSequence(4) 
	public void doTheValidationsWorkOnEmail() throws Exception {
		emp1 =null;
		log.log(Level.INFO, "creating with wrong email");
		emp = service.create("FIrstname","sureName","gender","address","postcode","country","citizenship","residence","passportno", "2010-10-10","sumncc","paPs1o123456");
		assertNull("creating with wrong email",emp1);
	}
	

    @Test
    @RunAsClient 
    @InSequence(5)
    public void test_from_client_side() {
    }

}

//mvn -e clean test -Parq-jbossas-managed
// mvn install  jboss-as:deploy
