package yapstone.restful.java.example;
import javax.ws.rs.*;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/*
 Author: Giampietro Zedda
 Date  : 13/03/2019
 
 I suppose to have the folder addressBook under WEB-INF
 with a file for each email address named as the email name.
*/

@ApplicationPath("/")
public class AddressBookService {
	
	private String firstName=""; 
    private String lastName=""; 
    private String phoneNumber="";  
    private String address="";  
    private String emailAddress="";  
	
	private	String fileName=""; 
	private String fileRecord=""; 
	private String filePath=""; 
		
	@GET
	@Path("/entry")
	@Produces("application/json")
	public String readEntry(@QueryParam("emailAddress") String emailAddress) {
		this.emailAddress=emailAddress;
		
		String pattern = "{ \"firstName\":\"%s\", \"lastName\":\"%s\", \"phoneNumber\": \"%s\", \"address\":\"%s\", \"emailAddress\":\"%s\"}";
		
		try {
			//Get the file related to the email address
		    filePath = context.getRealPath("/WEB-INF/addressBook/")+this.emailAddress;
		    Path path = Paths.get(filePath);
            byte[] bytes = Files.readAllBytes(path);
		    fileRecord=new String(bytes);
		
            //Extract fields 
            Scanner scanner = new Scanner(fileRecord);
	     	firstName = scanner.nextString();
		    lastName = scanner.nextString();
		    phoneNumber = scanner.nextString();
		    emailAddress = scanner.nextString();
    	    address="";
            while(scanner.hasNextString()){
               address = address + " " + scanner.nextString();
            }		
		    scanner.close();
		} catch (IOException ex) {
			return null; 
	    }		
		return String.format(pattern,  this.firstName, this.lastName, this.phoneNumber, this.address, this.emailAddress );
	}
	
	
	@POST
	@Path("/entry")
	@Produces("application/json")
	public String createEntry(@QueryParam("firstName")    String firstName, 
					          @QueryParam("lastName")	  String lastName, 
					          @QueryParam("phoneNumber")  String phoneNumber
					          @QueryParam("address")	  String address, 
					          @QueryParam("emailAddress") String emailAddress){
												 
		String pattern = "{ \"firstName\":\"%s\", \"lastName\":\"%s\", \"phoneNumber\": \"%s\", \"address\":\"%s\", \"emailAddress\":\"%s\"}";
		String retVal="OK";

	    this.firstName=firstName; 
        this.lastName=lastName; 
        this.phoneNumber=phoneNumber; 
        this.address=address; 
        this.emailAddress=emailAddress; 

	    //Get the file related to the email address
		//Compose the record, address as the last 		
		filePath = context.getRealPath("/WEB-INF/addressBook/")+emailAddress;
		fileRecord=firstName+" "+lastName+" "+phoneNumber+" "+emailAddress+" "+address;
		
		//Write the file identified by the mail
		retVal=writeOrUpdate(filePath,fileRecord);
		if ( retVal.equal("KO" ) {
            return null;
        }  		
		return String.format(pattern,  this.firstName, this.lastName, this.phoneNumber, this.address, this.emailAddress );
	
	}
		

	@PUT 
	@Path("/entry/mail")
	@Produces("application/json")
	public String updateEntry(@QueryParam("firstName")    String firstName, 
					          @QueryParam("lastName")	  String lastName, 
					          @QueryParam("phoneNumber")  String phoneNumber
					          @QueryParam("address")	  String address, 
					          @QueryParam("emailAddress") String emailAddress){
												 
		String pattern = "{ \"firstName\":\"%s\", \"lastName\":\"%s\", \"phoneNumber\": \"%s\", \"address\":\"%s\", \"emailAddress\":\"%s\"}";
		String retVal="OK";

	    this.firstName=firstName; 
        this.lastName=lastName; 
        this.phoneNumber=phoneNumber; 
        this.address=address; 
        this.emailAddress=emailAddress; 
		
	    //Get the file related to the email address
		//Composing the record, address as the last 		
		filePath = context.getRealPath("/WEB-INF/addressBook/")+emailAddress;
		fileRecord=firstName+" "+lastName+" "+phoneNumber+" "+emailAddress+" "+address;
		
		//Update the file identified by the mail
		retVal=writeOrUpdate(filePath,fileRecord);
		if ( retVal.equal("KO" ) {
            return null;
        }  		
		return String.format(pattern,  this.firstName, this.lastName, this.phoneNumber, this.address, this.emailAddress );
	
	}
	
	@DELETE 
	@Path("/entry/mail")
	@Produces("application/json")
	public int deleteEntry(@QueryParam("emailAddress") String emailAddress) {	
		String pattern = "{ \"firstName\":\"%s\", \"lastName\":\"%s\", \"phoneNumber\": \"%s\", \"address\":\"%s\", \"emailAddress\":\"%s\"}";
        this.emailAddress=emailAddress
		filePath = context.getRealPath("/WEB-INF/addressBook/")+this.emailAddress;
   	    try{
    		
    		File file = new File(filePath);
        	
    		if(!file.delete()){
    			return null;
    		}
    	   
    	}catch(Exception e){    		
    		return null;
    		
    	}

		return String.format(pattern,  this.firstName, this.lastName, this.phoneNumber, this.address, this.emailAddress );
	}
	
	private String writeOrUpdate(String filePath, String fileRecord) {
		String retVal="OK";
        try 
		{
            File statText = new File(filePath);
            FileOutputStream is = new FileOutputStream(statText);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            w.write(fileRecord);
            w.close();
        } catch (IOException e) {
            retVal="KO";
        }
		return retVal;
    }

}
