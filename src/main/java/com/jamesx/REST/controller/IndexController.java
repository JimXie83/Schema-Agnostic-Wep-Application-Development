package com.jamesx.REST.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**************************************************
 * By JamesXie 2016
 **************************************************/
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping(method = RequestMethod.GET)
    public String Index() {
        return "Index";
    }

    /***Don't delete the following line***/
    /*<--method injection here-->*/

    @RequestMapping(value = "/category/",method = RequestMethod.GET)
    public String category() {return "category";}

    @RequestMapping(value = "/orders/",method = RequestMethod.GET)
    public String orders() {return "orders";}

    @RequestMapping(value = "/employees/",method = RequestMethod.GET)
    public String employees() {return "employees";}

    @RequestMapping(value = "/contacts/",method = RequestMethod.GET)
    public String contacts() {return "contacts";}

    @RequestMapping(value = "/contactsBatchEdit/",method = RequestMethod.GET)
    public String contactsBatchEdit() {return "contacts_batchEdit";}

}