package resources;

import java.text.ParseException;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.rs.security.cors.CorsHeaderConstants;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.cxf.rs.security.cors.LocalPreflight;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import representations.OrderRepresentation;
import representations.OrderRequest;
import activity.OrderActivity;
import errorHandling.ErrorMessage;
@CrossOriginResourceSharing(
		allowAllOrigins = true,
		allowCredentials = true,
		allowHeaders = {
				"'Accept': 'application/json'",
				"'Content-Type': 'application/json'"
		})

@Path("/")
public class OrderResource implements OrderService{
	@Context
	private HttpHeaders header;
	
	@OPTIONS
	@LocalPreflight
	@Path("/")
	public Response options() {
		
		return Response.ok()
				.header(CorsHeaderConstants.HEADER_AC_ALLOW_METHODS, "POST, PUT, GET, DELETE")
				.header(CorsHeaderConstants.HEADER_AC_ALLOW_CREDENTIALS,"true")
				.header(CorsHeaderConstants.HEADER_AC_ALLOW_ORIGIN,"*")
				.header(CorsHeaderConstants.HEADER_AC_ALLOW_HEADERS,"Content-Type")
				.build();	
	}
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/")
	//This retrieves a list of all the orders in the database
	public Set<OrderRepresentation> getAllOrders() throws ParseException {
		System.out.println("GET METHOD Request for all Orders .............");
		OrderActivity orderActivity = new OrderActivity();
		return orderActivity.getOrders();	
	}
	
	@OPTIONS
	@LocalPreflight
	@Path("/{orderId}")
	public Response idOptions(@PathParam("orderId") int orderID	) {
		
		return Response.ok()
				.header(CorsHeaderConstants.HEADER_AC_ALLOW_METHODS, "POST, PUT, GET, DELETE")
				.header(CorsHeaderConstants.HEADER_AC_ALLOW_CREDENTIALS,"true")
				.header(CorsHeaderConstants.HEADER_AC_ALLOW_ORIGIN,"*")
				.header(CorsHeaderConstants.HEADER_AC_ALLOW_HEADERS,"Content-Type")
				.build();	
	}
	
	
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{orderId}")
	//This resource method gets an order by its specific order number
	public OrderRepresentation getOrder(@PathParam("orderId") int id) throws ParseException {
		System.out.println("GET METHOD Request from Client with OrderRequest String ............." + id);
		OrderActivity orderActivity = new OrderActivity();
		return orderActivity.getSpecificOrder(id);
		}
	
	
	@POST
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@Path("/")
	//This order create accepts an orderRequest which consists of a list of bookRequests that will be converted to book objects
	//once the book objects are created they are passed down the stack and are iterated through in the Order constructor
	//iterating through the books to assign their order id to match the current order id created in the constructor.
	//Primary and foreign keys
	public OrderRepresentation createOrder (OrderRequest  orderRequest) throws ErrorMessage {
		System.out.println("POST METHOD Request from Client with ............." + orderRequest.getOrderID() + "  " + orderRequest.getSqlDate()
		+ " " + orderRequest.getSqlExpectedShippingDate() + " " + orderRequest.isShipped());		
		OrderActivity orderActivity = new OrderActivity(); 
		return orderActivity.createOrder(orderRequest);
	}
	
	@DELETE
	@Produces({MediaType.APPLICATION_JSON})
	@Path("/{orderId}")
	//Cancel an order
	public Response deleteOrder(@PathParam("orderId") int id) {
		System.out.println("Delete METHOD Request from Client with OrderRequest String ............." + id);
		OrderActivity orderActivity = new OrderActivity();
		String res = orderActivity.deleteOrder(id);
		if (res.equals("OK")) {
			return Response.status(Status.OK).build();
		}
		return null;
	}
	
}

/////