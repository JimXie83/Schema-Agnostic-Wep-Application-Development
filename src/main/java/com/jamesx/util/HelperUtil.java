package com.jamesx.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jamesx.util.myAnnotations.EntityRestUrl;
import com.jamesx.util.myAnnotations.SmartTableInfo;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;

/**************************************************
 * By JamesXie 2016
 *****************************************************/
public final class HelperUtil {
    public final static String DATE_FORMAT = "yyyy-MM-dd";

    /**************************************************
     * convert a Json string into LinkedHashSet that contains valid search fields
     * null or empty fields will be ignored
     * (Reason for using Gson is to avoid entity's "ClassNotFoun" Exception)
     * vmObject (Json String) has two components
     * <1> modelClassName: full name of entity class, e.g. "com.jamesx.domain.Employee"
     * <2> entity: contains entity data
     * Param: vmObject==> JSON object (passed from POST in Json format) into a list of Criterion
     * return: an entity of T (specified entity class)
     *****************************************************/
    public static List<Criterion> getValidCriteria(LinkedHashMap viewModel) {
        List<Criterion> myCriteriaList = new ArrayList<Criterion>();
        try {
            Class<?> entityClass = Class.forName(getEntityClassName(viewModel).toString());
            Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            // get Entity Object from LinkedHashMap
            Object entityObj = getEntityJsonString(viewModel);
            Object entity = gson.fromJson(gson.toJson(entityObj), entityClass);
            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                Object val = new PropertyDescriptor(field.getName(), entityClass).getReadMethod().invoke(entity);
                //Object value = PropertyUtils.getProperty(entity, "name");
                if (val == null || val.toString() == "") continue;      //ignore empty criterion
                if (val instanceof Collection<?>) continue;                    //ignore Child entity
                if (val instanceof ObjectState) continue;               //ignore ObjectState field
                if (val instanceof Number || val instanceof Timestamp) {
                    myCriteriaList.add(Restrictions.eq(field.getName(), val)); //.like(entry.getKey(), entry.getValue() + "%"));
                    continue;
                }
                myCriteriaList.add(Restrictions.like(field.getName(), val + "%"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return myCriteriaList;
    }

    /********************************************************************************************
     * convert a Json string into entity as specified Entity object
     * (Reason for using Gson is to avoid entity's "ClassNotFoun" Exception)
     * vmObject (Json String) has two components
     * <1> modelClassName: full name of entity class, e.g. "com.jamesx.domain.Employee"
     * <2> entity: contains entity data
     * Param: vmObject==> JSON object (passed from POST in Json format) into a list of Criterion
     * return: an entity of T (specified entity class)
     ********************************************************************************************/
    public static <T> T JsonToEntity(LinkedHashMap viewModel) {
        T entity = null;
        try {
            // Creates the json object Register an adapter to manage the date types as long values
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateDeSerializer()).create();

                    //.registerTypeAdapter(java.sql.Date.class, new GsonSqlDateDeSerializer()).create();
            Object entityObj = getEntityJsonString(viewModel);
            Class<?> entityClass = Class.forName(getEntityClassName(viewModel).toString());
            //String test=gson.toJson(entityObj);

            entity = gson.fromJson(gson.toJson(entityObj), (Class<T>) entityClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    /********************************************************************************************
     * convert a Json string into a list of entity as specified Entity object
     * (Reason for using Gson is to avoid entity's "ClassNotFoun" Exception)
     * <1> modelClassName: full name of entity class, e.g. "com.jamesx.domain.Employee"
     * <2> entity: contains entity data
     * Param: viewModel==> LinkedHashMap object (passed from POST in Json format)
     * return: List<T> of entity of T (specified entity class)
     ********************************************************************************************/
    public static <T> List<T> JsonToEntityList(LinkedHashMap viewModel) {
        List<T> entityList = new ArrayList<>();
        try {
            // Creates the json object Register an adapter to manage the date types as long values
            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateDeSerializer()).create();
            Object entityObj = getEntityJsonString(viewModel);
            Class<?> entityClass = Class.forName(getEntityClassName(viewModel).toString());
            for (Object obj : (List) entityObj) {
                entityList.add(gson.fromJson(gson.toJson(obj), (Class<T>) entityClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entityList;
    }

    /********************************************************************************************
     * functions for reading JSON string from client-side
     ********************************************************************************************/
    public static Object getEntityClassName(LinkedHashMap viewModel) {
        return viewModel.get("modelClassName");
    }

    public static Object getEntityJsonString(LinkedHashMap viewModel) {
        return viewModel.get("entity");
    }

    public static Object getPagingInfo(LinkedHashMap viewModel) {
        return viewModel.get("pagingInfo");
    }

    public static Object getSearchFilters(LinkedHashMap viewModel) {
        return viewModel.get("searchFilters");
    }

    /********************************************************************************************
     * Convert an Entity object to Json String
     ********************************************************************************************/
    public static String EntityToJson(Object obj) {
        Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
        return gson.toJson(obj);
    }

    /********************************************************************************************
     * Convert an Entity object to String
     ********************************************************************************************/
    public static String EntityToString(Object obj) {
        StringBuilder json = new StringBuilder();
        try {
            json.append(obj.getClass().getSimpleName() + " [");
            Field[] fields = obj.getClass().getDeclaredFields();

            for (Field field : fields) {
                Object fldVal = null;
                fldVal = new PropertyDescriptor(field.getName(), obj.getClass()).getReadMethod().invoke(obj);
                if (fldVal instanceof Set<?>) continue;  //Child entity
                json.append(field.getName() + "=" + fldVal + ",");
                //if (fldVal == null) {json.append(field.getName()+": null"); continue;}
                //if (fldVal instanceof Number || fldVal instanceof Timestamp) { json.append(field.getName()+":"+fldVal); continue; }
            }
            json = json.deleteCharAt(json.lastIndexOf(",")).append(" ]");   //delete last ","
            //json = json.toString().substring(0,10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json.toString();
    }

    /********************************************************************************************
     * Convert a data object into a list of Criterion
     * 1> data object's empty/null field will be ignored
     * 2> fields with value will be converted into a Criterion object
     ********************************************************************************************/
    public static List<Criterion> generateCriteria(Object dataObject) {
        List<Criterion> myCriteria = new ArrayList<Criterion>();
        if (dataObject == null) return null;
        // * convert a data Object/entity into hashmap, key/value pair
        Map<String, Object> keyValue = HelperUtil.getEntityMap(dataObject);

        for (Map.Entry<String, Object> entry : keyValue.entrySet()) {
            String fieldName = entry.getKey();
            Object fieldValue = entry.getValue();
            myCriteria.add(Restrictions.like(fieldName, fieldValue + "%"));
        }
        return myCriteria;
    }

    /********************************************************************************************
     * convert a data object/entity into hashmap, key/value pair
     * empty/null field will be ignored
     ********************************************************************************************/
    public static Map<String, Object> getEntityMap(Object dataObj) {
        Map<String, Object> mapReturn = new HashMap<String, Object>();
        BeanInfo info;
        Object oVal;
        try {
            info = Introspector.getBeanInfo(dataObj.getClass());
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                // if child property of oneToMany relationship, ignore for search
                if (pd.getPropertyType() == Set.class || pd.getPropertyType() == Collection.class) continue;
                Method reader = pd.getReadMethod();
                if (reader != null && !reader.getName().startsWith("getClass")) {
                    oVal = reader.invoke(dataObj);
                    // if field value is null or empty string for String-type Column, excluded from criteria
                    if (oVal == null || (oVal instanceof String && oVal.toString().trim().length() == 0)) continue;
                    mapReturn.put(pd.getName(), oVal);
                }
            } //m.getName().startsWith("getClass")
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapReturn;
    }

    /********************************************************************************************
     * get entity default EagerLoading Entity Graph name
     * format: "graph.EntityCalssName.eagerLoad"
     * Parameter: name of classType or Class Type
     ********************************************************************************************/
    public static String getEagerLoadingEntityGraphName(Object classType) {
        if (classType instanceof String) {
            return "graph." + classType + ".eagerLoad";
        }
        return "graph." + ((Class<?>) classType).getSimpleName() + ".eagerLoad";
    }

    /********************************************************************************************
     * get entity's fields, return a coma-delimited string: "field1,field2"
     ********************************************************************************************/
    public static String getClassFields(Class<?> entityType) {
        StringBuilder json = new StringBuilder();
        try {
            json.append("\"");
            Field[] fields = entityType.getDeclaredFields();

            for (Field field : fields) {
                if (field.getType() == Set.class) {
                    System.out.println("set name=" + field.getName());
                }
                json.append(field.getName() + ",");
            }
            json = json.deleteCharAt(json.lastIndexOf(",")).append("\"");   //delete last ","
            //json = json.toString().substring(0,10);
        } catch (Exception e) {
            throw e;
        }
        return json.toString();
    }

    /********************************************************************************************
     * <1>set child-entity's parent/root reference when save/update
     * <2>remove deleted child entities from Collection
     ********************************************************************************************/
    public static void setParentReference(Object entity) throws Exception {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType() == Set.class || field.getType() == Collection.class) {
                // retrieve child-entities by accessor method
                Collection objSet = (Collection) new PropertyDescriptor(field.getName(), entity.getClass()).getReadMethod().invoke(entity);
                // get Child Entity Type, e.g. Employee.empAwards
                Type type = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                // get Child-Entity's properties/fields, look for Root Entity
                Field[] childFields = Class.forName(type.getTypeName()).getDeclaredFields();
                List deletedChildList = new ArrayList<>();
                for (Field fld : childFields) {
                    if (fld.getType() == entity.getClass()) {// found the parent-Entity field
                        for (Object child : objSet) {
                            // set child-entity's Parent
                            new PropertyDescriptor(fld.getName(), child.getClass()).getWriteMethod().invoke(child, entity);
                            if (((IObjectWithState) child).getObjectState() == ObjectState.Deleted) {
                                deletedChildList.add(child);
                            }
                        }
                    }
                }
                // process deleted Child entities
                for (Object deletedChild : deletedChildList) {
                    objSet.remove(deletedChild);
                }
                ;
            }
        }
    }

    /********************************************************************************************
     * set child-entity to EMPTY, solving lazy-loading issue
     ********************************************************************************************/
    public static void clearChildEntities(Collection<?> entityList) throws Exception {
        for (Object entity : entityList) {
            Field[] fields = entity.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getType() == Set.class || field.getType() == Collection.class) {
                    if (field.getType() == Set.class || field.getType() == Collection.class) {
                        //use setMethod to clear the Collection
                        Method setMethod = new PropertyDescriptor(field.getName(), entity.getClass()).getWriteMethod();
                        //set child entity as empty with an empty Set<?>
                        setMethod.invoke(entity, new HashSet<>());
                    }
                }
            }
        }
    }

    /********************************************************************************************
     * retrieve an Empty instance of Root Entity with
     * one empty instance for each Child-Entity
     * depth level = 1
     ********************************************************************************************/
    public static Object getEntityInfo(Class<?> classType) throws Exception {
        Object entity = classType.getConstructor().newInstance();
        Map entityMeta = new HashMap<>();
        // retrieve Entity's REST URL
        String entityRestUrl = classType.getAnnotation(EntityRestUrl.class).url();
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            Method readMethod = new PropertyDescriptor(field.getName(), classType).getReadMethod();
            if (field.getType() == Set.class || field.getType() == Collection.class) {
                Collection objSet = (Collection) readMethod.invoke(entity);
                // get Child Entity Type, e.g. Employee.empAwards
                Type childType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                // get Child-Entity's properties/fields, look for Root Entity
                Object childObj = Class.forName(childType.getTypeName()).getConstructor().newInstance();//.getDeclaredFields();
                // set child entity's state to ObjectState.Deleted, so that child entity won't show on creation
                if (childObj instanceof IObjectWithState) {
                    ((IObjectWithState) childObj).setObjectState(ObjectState.Deleted);
                }
                objSet.add(childObj);
            }
        }
        Map metaData = new HashMap() {{
            put("EmptyInstance", entity);
            put("EntityRestUrl", entityRestUrl);
            put("MetaInfo", HelperUtil.getEntityMetaInfo(classType, 1));
            put("SmartTableInfo", HelperUtil.getSmartTableColInfo(classType));
        }};
        return metaData;
    }

    /********************************************************************************************
     * retrieve meta data of an Entity, recursively
     *
     * @param entityClass: entity type
     * @param iDepth:      recursive level, from Root level set to 1
     * @return
     * @throws Exception
     ********************************************************************************************/
    public static EntityMetaVM getEntityMetaInfo(Class<?> entityClass, int iDepth) throws Exception {
        // View Model's names is extracted from class name, e.g. Solution.Models.SalesOrder ==> SalesOrder
        EntityMetaVM metaData = new EntityMetaVM();
        metaData.VmName = entityClass.getSimpleName();
        //List<String> ChildrenNames = getChildEntityNames(entityClass);
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            if (iDepth < 0) break; //default depth level is from root View Model to Child View Model
            Method readMethod = new PropertyDescriptor(field.getName(), entityClass).getReadMethod();
            if (field.getType() == Set.class || field.getType() == Collection.class) {
                // get Child Entity Type, e.g. Employee.empAwards (Set<childType> or Collection<childType>
                Type childType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                EntityMetaVM child_vm_metadata = getEntityMetaInfo((Class) childType, iDepth - 1);
                child_vm_metadata.VmName = field.getName();
                metaData.Child_VMs.put(field.getName(), child_vm_metadata);
            } else {
                ColumnInfo columnInfo=getColumnMetaInfo(readMethod, field, metaData);
                //columnInfo.ColDesc=getC
                metaData.Columns.put(field.getName(), columnInfo);
            }
        }
        setColumnLabels(metaData, entityClass);
        return metaData;
    }

    public static Boolean isEmpty(Object obj) {
        return (obj == null || obj.toString().trim().length() == 0);
    }
    /********************************************************************************************
     * retrieve Column's labels from @SmartTableInfo annotation
     * @param metaData
     * @param clazz
     ********************************************************************************************/
    private static void setColumnLabels(EntityMetaVM metaData, Class<?> clazz) {
        SmartTableInfo stInfo = clazz.getAnnotation(SmartTableInfo.class);
        if (stInfo == null) return;
        String[] colNames=Arrays.asList(stInfo.Fields().split(",")).toArray(new String[0]);
        String[] colLabels=Arrays.asList(stInfo.Captions().split(",")).toArray(new String[0]);

        for (int i=0; i<colNames.length;i++){
            ColumnInfo colInfo=metaData.Columns.get(colNames[i].trim());
            if (colInfo!=null){
                if (colLabels.length>i && !isEmpty(colLabels[i])) colInfo.ColDesc=colLabels[i].trim();
            }
        }
    }

    /********************************************************************************************
     * retrieve Column's data type, this is used for client-side comparison operator
     * @param clazz: root entity class
     * @return "Number"/"Date"/"String"/?
     ********************************************************************************************/
    public static String getColumnDataType(Class<?> clazz) {
        Object superClass = clazz.getSuperclass();
        return superClass == Number.class ? "Number" : superClass == Date.class ? "Date" : clazz == String.class ? "String" : clazz.getSimpleName();
    }

    /********************************************************************************************
     * retrieve Smart-Table's Fields and Captions
     * @param clazz: root entity class
     * @return Smart-Table's Fields and Captions
     ********************************************************************************************/
    public static Map getSmartTableColInfo(Class<?> clazz) {
        SmartTableInfo stInfo = clazz.getAnnotation(SmartTableInfo.class);
        Map map = new HashMap<>();
        if (stInfo != null) {
            map.put("Captions", stInfo.Captions());
            map.put("Fields", stInfo.Fields());
        }
        return map;
    }

    /********************************************************************************************
     * retrieve the child entity list
     * @param clazz: root entity class
     * @return list of String contains child entity names
     ********************************************************************************************/
    public static List<String> getChildEntityNames(Class clazz) {
        List<String> childList = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == Set.class || field.getType() == Collection.class) childList.add(field.getName());
        }
        return childList;
    }

    /********************************************************************************************
     * retrieve meta data of @Column, @Id, @NotNull, @Min, @Max, @Size,  annotation
     *  Check annotations on both member variables/fields  and getter methods
     * @param readMethod
     * @param field
     * @param entityMetaVM
     * @return: ColumnInfo:
     ********************************************************************************************/
    public static ColumnInfo getColumnMetaInfo(Method readMethod, Field field, EntityMetaVM entityMetaVM) {
        ColumnInfo colInfo = new ColumnInfo();
        // process @Column annotation
        Column column = readMethod.getAnnotation(Column.class);
        // if @Column Annotatoin is not set on gettter Method, try to get it from field
        if (column==null) column=field.getAnnotation(Column.class);
        if (column != null) {
            colInfo.ColLength = column.length();
            colInfo.Nullable = column.nullable();
            colInfo.Precision=column.precision();
            colInfo.Scale =column.scale();
        }
        colInfo.ColName = field.getName();
        colInfo.ColDesc = field.getName();
        colInfo.ColType = getColumnDataType(field.getType());
        // process @Id annotation, primary key field
        Id id = readMethod.getAnnotation(Id.class);
        if (id==null) id=field.getAnnotation(Id.class);
        if (id != null) {
            colInfo.isKey = true;
            entityMetaVM.PrimaryKey = field.getName();    // set PK for entityMetaVM
        }
        // process @NotNull annotation
        NotNull notNull = readMethod.getAnnotation(NotNull.class);
        if (notNull==null) notNull=notNull = field.getAnnotation(NotNull.class);
        if (notNull != null) colInfo.Nullable = false;
        // process @Min annotation
        Min min = readMethod.getAnnotation(Min.class);
        if (min==null) min=field.getAnnotation(Min.class);
        if (min != null) colInfo.MinValue = min.value();
        // process @Max annotation
        Max max = readMethod.getAnnotation(Max.class);
        if (max==null) max=field.getAnnotation(Max.class);
        if (max != null) colInfo.MaxValue = max.value();
        // process @Size annotation
        Size size = readMethod.getAnnotation(Size.class);
        if (size==null) size=field.getAnnotation(Size.class);
        if (size != null) {
            colInfo.MinLength = size.min();
            colInfo.MaxLength = size.max();
        }
        Pattern pattern=readMethod.getAnnotation(Pattern.class);
        if (pattern==null) pattern=field.getAnnotation(Pattern.class);
        if (pattern!=null){
            colInfo.ColRegex=pattern.regexp();
        }
        return colInfo;
    }


}
