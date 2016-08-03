package com.jamesx.REST.controller;

import com.jamesx.service.generic.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/category/")
public class CategoryRestController<T> extends AbstractRestController {
    @Autowired
    public CategoryRestController(@Qualifier("categoryService") IGenericService entityService) {
        super.entityService = entityService;
    }
}