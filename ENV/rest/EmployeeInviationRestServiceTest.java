package com.sargent.disc.arquillian.rest;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sargent.disc.dao.SmartDAO;
import com.sargent.disc.domain.model.InvitationEmp;
import com.sargent.disc.rest.InvitationEMPListCutom;
import com.sargent.disc.rest.JaxRsActivator;
import com.sargent.disc.services.GenericInvitationEmpService;
import com.sargent.disc.util.Resources;



@RunWith(Arquillian.class)
@RunAsClient

public class EmployeeInviationRestServiceTest  {
	@Deployment
	public static Archive<?> createTestArchive() {
		return ShrinkWrap.create(WebArchive.class, "test55.war")
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
	@Inject
	Logger logger;

	@BeforeClass
	public static  void setUpClass() throws Exception {

	}
	
    private static final String RESOURCE_PREFIX = JaxRsActivator.class.getAnnotation(ApplicationPath.class).value().substring(1);
  
    @ArquillianResource
    URL deploymentUrl;
    
    @Test
    public void clistBuilder() throws Exception
    {
       String urlString= deploymentUrl.toString() + RESOURCE_PREFIX ;
       HttpClient client = new HttpClient();

       {
          //GetMethod method = new GetMethod("http://localhost:8080/EmpMVNInvitation/rest/employeeInvitation/clist/builder?start=0&limit=35");
    	   GetMethod method = new GetMethod(urlString+"/employeeInvitation/clist/builder?start=0&limit=35");
          int status = client.executeMethod(method);
          //Assert.assertEquals(HttpResponseCodes.SC_OK, status);
          Assert.assertEquals(HttpStatus.SC_OK, status);
          
          ObjectMapper mapper = new ObjectMapper();
          InvitationEMPListCutom  root = mapper.readValue(method.getResponseBodyAsString(), InvitationEMPListCutom.class);
          System.out.println(root.toString());
          
          Assert.assertNotNull("LIST is  null", root);
          Assert.assertNotNull("root data is  null", root.getData());
          Assert.assertNotNull("root data is  null", root.getData());
          
          // logger.log(Level.SEVERE, list.toString());
          method.releaseConnection();
       }
    }
    
    

   // @Test
    public void testGetCustomerByIdUsingClientRequest() throws Exception {
        //deploymentUrl = new URL("http://localhost:8180/test/");
        // GET http://localhost:8080/test/rest/customer/1
      //  @SuppressWarnings("deprecation")
		//ClientRequest request = new ClientRequest(deploymentUrl.toString() + RESOURCE_PREFIX + "/employeeInvitation/list/?start=0&limit=35");
        
       // request.header("Accept", MediaType.APPLICATION_JSON);
        //request.
    }
    
//    @Test
//    @RunAsClient 
//    @InSequence(1)
//    public void test_from_client_side() {
//    }

	//localhost:8080/EmpMVNInvitation/rest/employeeInvitation/list?start=0&limit=35
//
//    @Test @GET @Path("rest/employeeInvitation/list")
//    @QueryParams({ 
//    	@QueryParam(name = "start", value = "0"),@QueryParam(name = "limit", value = "100")
//    	})
//    @Consumes(MediaType.APPLICATION_JSON)
//    public void shouldBeAbleToListEmployess(InvitationEMPList response) 
//    {
//       //Assert.assertEquals(Status.OK.getStatusCode(), response.getStatus());
//       
//      // Customer customers = response.getEntity();
//       //Assert.assertEquals(1, customers.getId().intValue());
//    }
}

//mvn -e clean test -Parq-jbossas-managed
