package com.jamesx.REST.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jamesx.service.generic.IGenericService;
import com.jamesx.util.HelperUtil;
import com.jamesx.util.PagingInfo;
import com.jamesx.util.SearchFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.LinkedHashMap;
import java.util.List;

import static com.jamesx.util.HelperUtil.clearChildEntities;
/**************************************************
 * By JamesXie 2016
 **************************************************/
public class AbstractRestController<T> {

    public IGenericService entityService;

    /********************************************************************************************
     * return all entities in DB,
     * child entities will be set to null for performance
     ********************************************************************************************/
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<T>> getAllEntities() throws Exception {
        List<T> entitiesList = entityService.findAll();
        if (entitiesList.isEmpty()) return new ResponseEntity<List<T>>(HttpStatus.NO_CONTENT);
        //set child entity as empty to solve lazy-loading issue
        clearChildEntities(entitiesList);
        //return new ResponseEntity(null, HttpStatus.OK);
        return new ResponseEntity<List<T>>(entitiesList, HttpStatus.OK);
    }

    /********************************************************************************************
     * retrieve entities with criteria specified in viewModel's "searchFilters"
     *
     * @param viewModel: contains: (1)"modelClassName" -> Root Entity Class
     *                   (2)"pagingInfo" -> paging information
     *                   (3)"searchFilters": search criteria
     ********************************************************************************************/
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public <T> ResponseEntity<Object> searchEntities(@RequestBody LinkedHashMap viewModel) throws Exception {
        //viewModel contains: (1)"modelClassName" -> Root Entity Class(2)"pagingInfo" (3)"searchFilters"
        Gson gson = new GsonBuilder().setDateFormat(HelperUtil.DATE_FORMAT).create();
        SearchFilter[] searchFilters= gson.fromJson(gson.toJson(HelperUtil.getSearchFilters(viewModel)), SearchFilter[].class);
        PagingInfo pagingInfo= pagingInfo = gson.fromJson(gson.toJson(HelperUtil.getPagingInfo(viewModel)), PagingInfo.class);
        pagingInfo = entityService.findByCriteriaPaging(pagingInfo,searchFilters);
        HelperUtil.clearChildEntities((List<?>) pagingInfo.serverData);
        //if (((List<?>) pagingInfo.serverData).isEmpty()) return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<Object>(pagingInfo, HttpStatus.OK);
    }

    /********************************************************************************************
     * retrieve an Entity with related Child-entities by ID specified
     *
     * @param id:
     * @return : entity's entity-graph including child-entities
     ********************************************************************************************/
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<T> findById(@PathVariable("id") Integer id) {
        T entity = (T) entityService.findById(id);
        if (entity == null) return new ResponseEntity<T>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<T>(entity, HttpStatus.OK);
    }

    /********************************************************************************************
     * retrieve an Entity with related Child-entities by ID specified
     *
     * @param viewModel: contains (1)"entity" (2)modelClassName
     * @return : updated entity (including all updated child-entities)
     ********************************************************************************************/
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE) //
    public <T> ResponseEntity<T> saveEntity(@RequestBody Object viewModel) throws Exception {//public <T> ResponseEntity<T> saveEntity(@RequestBody T entity, UriComponentsBuilder ucBuilder) {
        T entity = HelperUtil.JsonToEntity((LinkedHashMap) viewModel);
        HelperUtil.setParentReference(entity);     // set child-entity's root reference
        T savedEntity = (T) entityService.save((T) entity);
        return new ResponseEntity<T>((T) savedEntity, HttpStatus.OK);
    }

    /********************************************************************************************
     * save a list of entities to DB
     *
     * @param viewModel: contains (1)"entity" (2)modelClassName
     * @return : updated entity (including all updated child-entities)
     ********************************************************************************************/
    @RequestMapping(value = "/batch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE) //
    public <T> ResponseEntity<List<T>> saveEntityList(@RequestBody Object viewModel) throws Exception {//public <T> ResponseEntity<T> saveEntity(@RequestBody T entity, UriComponentsBuilder ucBuilder) {
        List<T> entity = HelperUtil.JsonToEntityList((LinkedHashMap) viewModel);
        entityService.batchSave(entity);
        return new ResponseEntity<List<T>>(entity, HttpStatus.OK);
    }
    /********************************************************************************************
     * delete an entity (including child-entities) from DB
     *
     * @param id: contains (1)"entity" (2)modelClassName
     * @return : nothing
     ********************************************************************************************/
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteById(@PathVariable("id") Integer id) {
        T entity = (T) entityService.findById(id);
        if (entity == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        entityService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}