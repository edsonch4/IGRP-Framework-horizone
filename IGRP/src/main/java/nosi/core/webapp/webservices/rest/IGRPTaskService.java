package nosi.core.webapp.webservices.rest;

import javax.ws.rs.QueryParam;

import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;

import nosi.core.webapp.Controller;
import nosi.core.webapp.Response;

/**
 * Emanuel
 * 25 Mar 2018
 */

@Path("/task-service")
public class IGRPTaskService extends Controller{

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getProcess(@QueryParam("igrp_app") String app,@QueryParam("igrp_page") String page,@QueryParam("igrp_action") String action,@QueryParam("igrp_task_id") String taskId) {
		this.addQueryString("taskId", taskId);
		Response r = this.call(app, page, action,this.queryString());
		Properties p = new Properties();
		p.put("status", r.getStatus());
		Gson gson = new Gson();		
		System.out.println(gson.toJson(p));
		return gson.toJson(p);
	}
}