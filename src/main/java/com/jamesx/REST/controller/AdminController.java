package com.jamesx.REST.controller;

import com.jamesx.util.HelperUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
/**************************************************
 * By JamesXie 2016
 **************************************************/
@RestController
public class AdminController {
    /*****************************************************
     * retrieve an Empty instance of Root Entity with
     *  one empty instance for each Child-Entity
     *  depth level = 1
     *  URL: http://www.myserver.com/api/meta?entityName=value
     *****************************************************/
	@RequestMapping(value = "/api/meta", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Object getEntityInstance(@QueryParam("entityName") String entityName) throws Exception {
		Object  oReturn=HelperUtil.getEntityInfo(Class.forName(entityName));
		return oReturn;
		//return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
	}
}
