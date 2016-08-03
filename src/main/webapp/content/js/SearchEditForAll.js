'use strict';
//var ENTITY_INSTANCE_URL = window.location + 'api/meta?entityName=' + MODEL_CLASS_NAME; //MODEL_CLASS_NAME defined in JSP file
var ENTITY_INSTANCE_URL = window.location.origin + '/api/meta?entityName=' + MODEL_CLASS_NAME; //MODEL_CLASS_NAME defined in JSP file
var Entity_REST_URL = "";   //window.location.origin + '/api/emp/';
var MyDateFormat = "yyyy-MM-dd";
var ObjectState = {Unchanged: "Unchanged", Added: "Added", Modified: "Modified", Deleted: "Deleted"}; //var ObjectState = { Unchanged: 0, Added: 1, Modified: 2, Deleted: 4 };
var SearchOperators =[
    { value: "equal",   text: "= ",     desc: "equals to",                      applied_to: "Number,Date,String,Boolean" },
    { value: "gt",      text: "> ",     desc: "is greater than",                applied_to: "Number,Date,String" },
    { value: "lt",      text: "< ",     desc: "is less than",                   applied_to: "Number,Date,String" },
    { value: "notEqual",text: "<>",     desc: "NOT equals to",                  applied_to: "Number,Date,String,Boolean" },
    { value: "ge",      text: ">=",     desc: "is greater than or equals to",   applied_to: "Number,Date,String" },
    { value: "le",      text: "<=",     desc: "is less than or equals to",      applied_to: "Number,Date,String" },
    { value: "like",    text: "like",   desc: "begins with",                    applied_to: "String" },
    { value: "$",       text: "$",      desc: "contains",                       applied_to: "String" }
];
var App = angular.module('myApp', ['ngAnimate', 'ui.bootstrap', 'smart-table', 'toastr']);
// My-Date-Picker directive & controller
App.controller('myDatePickerCtrl', function ($scope) {
    $scope.open = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
    };
    $scope.format = MyDateFormat; //$scope.formats[0];
});

App.directive('myDatePicker', function ($parse) {
    function link($scope, element, attrs, ctrl) {}
    return {
        restrict: 'EA',
        templateUrl: window.location.origin + '/content/template/datepicker.html',
        link: link,
        controller: 'myDatePickerCtrl',
        require: ['ngModel'],
        scope: { dt: '=ngModel'}
    }
});
//config toastr
App.config(function (toastrConfig) {
    angular.extend(toastrConfig, {
        allowHtml: true,
        closeButton: true,
        positionClass:'toast-bottom-right'
    })
});
App.controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, param) {
    //$scope.msgBody is array for showing multiple lines
    $scope.msgBody = param.msgBody instanceof Array? param.msgBody:[param.msgBody];
    $scope.msgTitle = param.msgTitle;
    $scope.okButtonOnly = param.okButtonOnly == null ? false : param.okButtonOnly;
    $scope.ok = function () {
        $uibModalInstance.close("ok");
    };
    $scope.cancel = function () { $uibModalInstance.dismiss('cancel'); };
});

App.controller('SearchEntityController', ['SearchEntityService', '$scope', '$filter', 'toastr','$uibModal', function (SearchEntityService, scope, filter, MessageService, $modal) {
    //App.controller('SearchEntityController', ['$scope', '$filter', 'SearchEntityService', function (scope,filter, SearchEntityService) {
    var self = this;
    self.searchEntity = {};    /* search object*/
    self.savedSearchEntity = {};    /* saved copy of search object*/
    self.editEntity = {};    /* entity for editing self.editEntityChild = {};           // entity for editing*/
    self.savedEditEntity = {};    /* entity for editing*/
    self.entities = [];
    self.savedEntities=[]; //save search result for batch-Edit
    /*[{"objectState":"Unchanged","id":78,"firstname":"james15","lastname":"smith","telephone":"410-2324444","email":"js@email.comXXXX","birthdate":1454821200000,"created":1454302800000,"empAwards":[],"empAddress":[]},{"objectState":"Unchanged","id":80,"firstname":"james2","lastname":"Peter2","telephone":"343-232232","email":"jp@yahoo.comXXX","birthdate":573109200000,"created":571122000000,"empAwards":[],"empAddress":[]},{"objectState":"Unchanged","id":82,"firstname":"John","lastname":"Doe","telephone":"874834343","email":"jd@QQ.com","birthdate":1432958400000,"created":1401422400000,"empAwards":[],"empAddress":[]},{"objectState":"Unchanged","id":83,"firstname":"John2","lastname":"Doe2","telephone":"324324343","email":"jd2@gmail.com","birthdate":1461729600000,"created":1464667200000,"empAwards":[],"empAddress":[]},{"objectState":"Unchanged","id":87,"firstname":"Hanover","lastname":"john","telephone":null,"email":"HJ@emal.com","birthdate":1464580800000,"created":928123200000,"empAwards":[],"empAddress":[]}];*/
    self.tempID = -99999999;    /* temp ID for newly added record (parent or Child)*/
    self.DateFormat = "date:'" + MyDateFormat + "'";
    self.emptyEditEntity = {};    /*with child-entity*/
    scope.form = {};    /*for setting form to pristine issue*/
    self.isLoading = true;     //true when smart-table is loading
    self.displayEntities = [].concat(self.entities);    // smart-table's row source
    self.pageSize = 5; // display number of rows per page
    self.serverSidePaging=false;
    self.totalCount = 0;
    self.isEditing=false;       //true, if Edit View is editing
    self.hasBufferedChanges=false;      //true, if vewModel has been modified
    self.metaDataLoaded = false;
    self.tableState = {};
    /************************************
     MetaInfo for each column
     public String ColName;      //Entity's Column/field Name, NOT DB's column name
     //superClass==Number.class?"Number":superClass==Date.class?"Date":"String";
     public String ColType;      // Column Type: Number/Date/String
     public Integer ColLength;   // Column Length
     public Integer MinLength;   // field minimum length
     public Integer MaxLength;   // field maximum length
     public Object MinValue;     // fidld Min Value
     public Object MaxValue;     // field Max Value
     public Boolean Nullable;    // true if field is nullable
     public Boolean isKey;       // true if this is Primary Key Field
     public String ColRegex;     // Regular Expression for validating column value
     *************************************/
    self.MetaInfo = {};
    self.columns = []; //for Smart-Table, example {column: 'firstname', caption: 'First Name', isSort: true}
    //scope.editEntity=self.editEntity;

    //<editor-fold defaultstate="collapsed" desc="Modal-related functions & variables">
    self.modalResponse="";
    self.showMessage=function(msgBody, msgTitle,size){
        self.dialog(msgBody, msgTitle,true,size);
    };

    self.confirm=function(msgBody, msgTitle, size){
        self.dialog(msgBody, msgTitle,false,size).then(
            function ok(response) { //"ok" if OK button clicked
                self.modalResponse = response;
                return response;
            },
            function cancel() { //"" if cancel button clicked
                console.log('Modal dismissed at: ' + new Date());
            }
        );
    };
    self.dialog = function (msgBody, msgTitle,okButtonOnly,size) {
        var modalInstance = $modal.open({
            backdrop: false,
            animation: true,
            templateUrl: window.location.origin + '/content/template/modal.html',
            controller: 'ModalInstanceCtrl',
            size: size,     //"sm","lg"
            resolve: {param: function(){return { msgBody:msgBody, msgTitle:msgTitle, okButtonOnly:okButtonOnly}}}
        });
        return modalInstance.result;
    };
    // </editor-fold>

    // initialize columns for smart-table
    function iniSmartTableCols(smartTableInfo) {
        if (isEmpty(smartTableInfo["Fields"])) MessageService.error("Smart table columns not specified in "+MODEL_CLASS_NAME);
        var aCaptions = smartTableInfo["Captions"].split(',');
        var aFields = smartTableInfo["Fields"].split(',');
        if (aCaptions.length!=aFields.length){alert("Count of fields and captions specified did not match...");return;}
        self.columns = [];
        for (var i = 0; i < aFields.length; i++) {
            var col1 = {column: aFields[i], caption: aCaptions[i], isSort: true};
            //if (getFieldType(aFields[i])=="Date")
            //    col1.column='formatDate('+aFields[i]+')';
            self.columns.push(col1);
        }
    }

    /******************************************************************
     * ignore (1)Empty column (2) Array (child entity Field) (3) objectState field
     * if (isEmpty(val) || val instanceof Array || colName=="objectState") continue;
     * @returns {string}
     *******************************************************************/
    self.validateAll= function () {
        var errMsg=[];
        // (1) validate Root record
        self.validateRec(self.editEntity,self.MetaInfo.Columns,errMsg);

        for(var childVM in self.MetaInfo.Child_VMs){    // loop thru each child table
            for(var i=0;i<self.editEntity[childVM].length;i++){   // loop thru each record of Child table
                self.validateRec(self.editEntity[childVM][i],self.MetaInfo.Child_VMs[childVM].Columns,errMsg,i);
            }
        }
        return errMsg;
    }

    /******************************************************************
     * validate a record of specified table, default Root table if not specified
     *  ColumnInfo: ColName; ColType; ColLength; MinLength; MaxLength;
     *              MinValue; MaxValue; Nullable; isKey; ColRegex;
     *  meta_Info:  {Child_VMs, Columns, PrimaryKey, VmName)
     *******************************************************************/
    self.validateRec=function(rowData, meta_Info, msg, rowId) {
        for (var col in meta_Info){
            var colObj=meta_Info[col];
            var rowLocation = rowId == undefined ? "" : " (Row#" + (rowId + 1) + ")";
            var oVal=rowData[col];
            if (rowData['objectState']==ObjectState.Deleted) {break;}  //no need to validate deleted record
            if (oVal instanceof Array) continue; //Root's child entity field, ignore it
            if((!isEmpty(colObj.Nullable)) && !colObj.Nullable && isEmpty(oVal)){
                msg.push("'"+colObj.ColDesc+"'" +" should not be empty."+ rowLocation);
                continue;
            }
            if (colObj.ColType == "Number" && (isNaN(oVal) || oVal==="")) {
                msg.push("'" + col + "'" + " is not a valid number." + rowLocation);
                continue;
            }
            if (colObj.Scale>0){
                if (parseInt(oVal)>0 && (parseInt(oVal)+"").length>colObj.Precision-colObj.Scale){
                    msg.push("'"+colObj.ColDesc+"'" +" integer portion two long."+ rowLocation);
                    continue;
                }
            }
            // don't check length for Date or Boolean columns
            if (colObj.ColType!=="Date" && colObj.ColType!=="Boolean"){
                if((!isEmpty(colObj.ColLength)) && !isEmpty(oVal) && (oVal+"").length>colObj.ColLength){
                    msg.push("'"+colObj.ColDesc+"'" +" length exceeds "+colObj.ColLength+ rowLocation);
                }
                if((!isEmpty(colObj.MaxLength)) && !isEmpty(oVal) && (oVal+"").length>colObj.MaxLength){
                    msg.push("'"+colObj.ColDesc+"'" +" length exceeds "+colObj.MaxLength+ rowLocation);
                }
                if((!isEmpty(colObj.MinLength)) && ( isEmpty(oVal) || (oVal+"").length<colObj.MinLength)){
                    msg.push("'"+colObj.ColDesc+"'" +" should be longer than "+colObj.MinLength+ rowLocation);
                }
            }

            if((!isEmpty(colObj.MinValue)) && oVal<colObj.MinValue){
                msg.push("'"+colObj.ColDesc+"'" +" should be greater than "+colObj.MinValue+ rowLocation);
            }
            if((!isEmpty(colObj.MaxValue)) && oVal>colObj.MaxValue){
                msg.push("'"+colObj.ColDesc+"'" +" should be less than "+colObj.MaxValue+ rowLocation);
            }

            if(!isEmpty(colObj.ColRegex)){
                var regEx = new RegExp(colObj.ColRegex);
                if (!regEx.test(oVal)) {
                    msg.push("Value of '"+colObj.ColDesc+"': '" +oVal+"' is not in correct format."+ rowLocation);
                }
            }
        }
        return msg;
    }

    /******************************************************************
     * initiallize search operators for all available columns, according to column type
     * @Note: dropdownlist must be of class "search_op",
     *      with attribute "search_op" set as search column name
     *******************************************************************/
    function ini_operators() {
        //init search operators by class name search_op, retrieve corresponding column namee
        angular.forEach(document.getElementsByClassName("search_op"), function (dropdownlist, i) {
            dropdownlist.title = "comparison operator";
            // remove all options from dropdown list
            for(var i = dropdownlist.options.length - 1 ; i >= 0 ; i--) { dropdownlist.remove(i); }
            var colName = angular.element(dropdownlist).attr('search_col').split('.').pop();  //fetch last item, which is corresponding column name
            //Sanity check, make sure Search Column specified is valid
            if( ! (colName in self.editEntity)) {
                MessageService.error("Column: "+colName+" specified in a Dropdown list is not defined in root model.", "error");
                return;
            };
            // add appropriate options to dropdownList according to Column type (String/Date/Number, etc)
            angular.forEach(SearchOperators, function (item, i) {
                var option = document.createElement("option");
                option.value = item.value;
                option.text = item.text; //+" "+item.desc;
                option.title = item.desc;
                // if search column's data type is in SearchOperators' applied_to,
                //      add this operator to available operators for the specified column
                if (item.applied_to.indexOf(getFieldType(colName)) >= 0) { dropdownlist.add(option); }
            });
            // for String column default dropdownList value to "Start with" ("like" operation)
            if (getFieldType(colName)=="String") dropdownlist.value="like";
        });
    }

    /******************************************************************
     * show search criteria
     *******************************************************************/
    self.showSearchFilters=function() {
        self=this;
        var filters=getSearchFilters(true);
        var criteria=[];
        if (!filters || filters.length==0) {
            criteria ="No criterion specified...";
        }else {
            angular.forEach(filters,function(filter,i){
                if (filter.value instanceof Date) filter.value=filter.value.toISOString().slice(0,10);
                criteria.push("'"+filter.name + "' "+getOperatorDesc(filter.operator) + " '"+filter.value+"'");
            });
        }
        self.showMessage(criteria,"Search Criteria");
        return criteria;
    }

    /******************************************************************
     * get description of search operator options
     *******************************************************************/
    function getOperatorDesc(opText){
        var opDesc="<Operator NOT found>";
        angular.forEach(SearchOperators,function(item,i){
            if (item.value == opText) {
                opDesc=item.desc;
                return;
            }
        });
        return opDesc;
    }

    /******************************************************************
     * get search operators values for all specified columns, according to column type
     *  ignore (1)Empty column (2) Array (child entity Field) (3) objectState field
     * @Note: dropdownlist must be of class "search_op",
     *      with attribute "search_op" set as search column name
     * @param show_criteria, if prensent and true, return column description, else column name
     * @Return: (Array) Object like {col1:0, col2:16, col3:32}
     *******************************************************************/
    function getSearchFilters(show_criteria) {
        var oReturn=[];
        var operators={};
        //get all search operators by class name search_op, retrieve corresponding column namee
        angular.forEach(document.getElementsByClassName("search_op"), function (dropdownlist, i) {
            var colName = angular.element(dropdownlist).attr('search_col').split('.').pop();  //fetch last item, which is corresponding column name
            // add appropriate options to dropdownList according to Column type (String/Date/Number, etc)
            operators[colName]=dropdownlist.value;
        });
        for (var colName in self.searchEntity){
            var val=self.searchEntity[colName];
            //ignore (1)Empty column (2) Array (child entity Field) (3) objectState field
            if (isEmpty(val) || val instanceof Array || colName=="objectState") continue;
            var searchFilter={name:!show_criteria?colName:self.MetaInfo.Columns[colName].ColDesc,operator:"",value:val,type:getFieldType(colName)};
            if (!isEmpty(operators[colName])){  //this colName haa a corresponding operator
                searchFilter.operator=operators[colName];
            }
            oReturn.push(searchFilter);
        }
        return oReturn;
    }

    /******************************************************************
     * return column data type of specified table & field
     * @param fieldName,
     * @param tableName: if not specified, Root Entity
     * @returns {*}: Number / Date / String
     *******************************************************************/
    function getFieldType(fieldName, tableName) {
        if (isEmpty(tableName)) {
            // if fieldname is children entity, return "Not Found"
            return self.MetaInfo.Columns[fieldName].ColType;
        }
        return self.MetaInfo.Child_VMs[tableName].Columns[fieldName].ColType;
    }

    /******************************************************************
     * get PrimaryKey, without tablename, return PK of Root Entity
     * @param tableName: table's PK, if null, return Root Entity's PK
     * @returns {*} PrimaryKey Name of specified table
     *******************************************************************/
    function getPK(tableName) {
        if (tableName == null) {
            return self.MetaInfo.PrimaryKey;
        }
        return self.MetaInfo.Child_VMs[tableName].PrimaryKey;
    }

    // initialized when page loaded for the first time
    angular.element(document).ready(function () {
        if (isEmpty(MODEL_CLASS_NAME)) {alert("MODEL_CLASS_NAME has not been defined in view page.");return;}
        SearchEntityService.getEmptyEntityInstance()
            .then(
                function (serverData) {
                    console.log("self=" + self);
                    Entity_REST_URL = window.location.origin + serverData['EntityRestUrl'];
                    var entity = serverData['EmptyInstance'];
                    self.MetaInfo = serverData['MetaInfo'];
                    self.searchEntity = angular.copy(entity);
                    self.savedSearchEntity = angular.copy(self.searchEntity);
                    self.editEntity = angular.copy(self.searchEntity);
                    self.savedEditEntity = angular.copy(self.searchEntity);
                    self.emptyEditEntity = angular.copy(entity); //hodling structure of editEntity
                    iniSmartTableCols(serverData["SmartTableInfo"]);
                    ini_operators();
                    self.metaDataLoaded = true;
                    //self.fetchAllEntities();
                    MessageService.success("Meta information successfully loaded.");
                }
            );
    });
    self.activeTab = 1;

    self.setActiveTab = function (tabToSet) {
        self.activeTab = tabToSet;
    };
    //*****************Search Functions**********************
    self.fetchAllEntities = function () {
        SearchEntityService.fetchAllEntities()
            .then(
                function (resultset) {
                    self.entities = resultset;
                    MessageService.success(self.entities.length + " records retrieved..");
                },
                function (errResponse) {
                    //alert(errResponse.data);
                    MessageService.error(errResponse.data);
                    console.error('Error while fetching Currencies');
                }
            );
    };
    self.getServerData = function (tableState) {
        if (!self.serverSidePaging) return;
        console.log("getServerData: self=" + self);
        tableState.pagination.numberOfPages = Math.ceil(self.totalCount / self.pageSize);
        if (!self.metaDataLoaded) {//set self.tableState
            self.tableState = angular.copy(tableState);
            return;
        } // if MetaData not loaded yet, don't call server
        self.isLoading = true;
        var pageInfo = {
            start: self.tableState.pagination.start || 0,
            pageSize: self.tableState.pagination.number || self.pageSize,
            orderBy: self.tableState.sort.predicate || " ",
            asc: self.tableState.sort.reverse || " "
        };
        MessageService.info("Searching...");
        SearchEntityService.search(self.searchEntity, MODEL_CLASS_NAME, pageInfo, tableState)  //MODEL_CLASS_NAME is global var defined in jsp page
            .then(
                function (resultList) {
                    console.log("getServerData->SearchEntityService.search: self=" + self);
                    self.entities = angular.copy(resultList.serverData);
                    self.displayEntities = angular.copy(resultList.serverData);
                    //set the pagination state with server-data
                    self.tableState.pagination = {
                        start: resultList.start, // start index
                        totalItemCount: resultList.totalCount,
                        number: resultList.pageSize, //number of item on a page
                        numberOfPages: Math.ceil(resultList.totalCount / resultList.pageSize) //total number of pages
                    }
                    self.isLoading = false;
                    //MessageService.clear();
                    MessageService.success(self.entities.length + " records retrieved..\n\n Total records found: " + resultList.totalCount);
                },
                function (errResponse) {
                    MessageService.error(errResponse.data);
                    console.error('Error while updating current record.');
                }
            );
    };
    self.search = function () {
        var pageInfo = {start: 0, pageSize: 100}; //pageSize: self.pageSize
        var jsonStr = JSON.stringify({
            modelClassName: MODEL_CLASS_NAME,
            pagingInfo: pageInfo,
            searchFilters: getSearchFilters()
        });
        MessageService.info("Searching...");
        SearchEntityService.search(jsonStr)  //MODEL_CLASS_NAME is global var defined in jsp page
            .then(
                function (resultList) {
                    self.entities = angular.copy(resultList.serverData);
                    self.displayEntities = angular.copy(resultList.serverData);
                    self.savedEntities=angular.copy(resultList.serverData); // for batch Edit
                    self.totalCount = Math.ceil(resultList.totalCount / resultList.pageSize);
                    //self.tableState.pagination.numberOfPages = Math.ceil(resultList.totalCount/resultList.pageSize);//set the number of pages so the pagination can update
                    self.isLoading = false;
                    //MessageService.clear();
                    MessageService.success("Total records found: " + resultList.totalCount);
                    //self.entities = resultList;
                    //MessageService.success(self.entities.length+" records retrieved..");;
                },
                function (errResponse) {
                    MessageService.error(errResponse.data);
                    console.error('Error while updating current record.');
                }
            );
    };
    self.clearSearch = function () {
        // preserve a copy for RE-DO
        self.savedSearchEntity = angular.copy(self.searchEntity);
        // retrieve each input field of search View Model, set all to empty
        // id set to null
        for (var fieldName in self.searchEntity) {
            self.searchEntity[fieldName] = null;
        }
        ini_operators();    //set search operators to original state
        scope.searchForm.$setPristine(); //reset search Form
    };
    self.reDoSearch = function () { /* copy old values back from saved copy*/
        self.searchEntity = angular.copy(self.savedSearchEntity);
    };

    // recursively tally # of records for Modified/Added/Deleted/Unchanged
    self.editSummary=function(entity, tally){
        if (tally==null) tally={Modified:0, Added:0, Deleted:0, Unchanged:0};
        if (entity instanceof Array){
            for (var i=0;i<entity.length;i++){ self.editSummary(entity[i],tally); }
        }else{
            if ('objectState' in entity){
                // make sure ObjectState is valid
                if (!entity['objectState'] in ObjectState) MessageService.error(entity['objectState']+" is not a valid ObjectState.(editSummary)");
                tally[entity['objectState']]++;  //if (entity['objectState']==ObjectState.Added) tally.Added++; if (entity['objectState']==ObjectState.Deleted) tally.Deleted++; if (entity['objectState']==ObjectState.Modified) tally.Modified++; if (entity['objectState']==ObjectState.Unchanged) tally.Unchanged++;
            }
            for (var field in entity){ if (entity[field] instanceof Array) self.editSummary(entity[field],tally); }
        }

        return tally;
    };

    //summary={Modified:0, Added:0, Deleted:0, Unchanged:0};
    self.readEditSummary=function(summary){
        var msg="";
        msg+=summary.Modified>0?"Number of modified records: " + summary.Modified + "<br/>" : "";
        msg += summary.Added > 0 ? "Number of inserted records: " + summary.Added + "<br/>" : "";
        msg += summary.Deleted > 0 ? "Number of deleted records: " + summary.Deleted + "<br/>" : "";
        return msg;
    };
    self.save = function () {
        var validationMsg= self.validateAll();
        if (validationMsg.length>0) {self.showMessage(validationMsg,"Please make the following corrections");return;};
       // show the summary of updates/deletes/inserts
        var summary=self.readEditSummary(self.editSummary(self.editEntity));
        MessageService.info(summary, "Saving...");
        SearchEntityService.save(self.editEntity, MODEL_CLASS_NAME)  //MODEL_CLASS_NAME is global var defined in jsp page
            .then(
                function (entity) {
                    self.editEntity = angular.copy(entity);
                    var i=self.findRowById(self.entities, self.editEntity['id']);
                    // update the search result after the save
                    if (i>=0) {
                        self.entities.splice(i, 1, angular.copy(self.editEntity));
                        self.displayEntities.splice(i, 1, angular.copy(self.editEntity));
                    }
                    scope.editForm.$setPristine(); //reset Form
                    self.isEditing=false;   //
                    self.hasBufferedChanges=false;
                    MessageService.success("Changes have been saved successfully.");
                },
                function (errResponse) {
                    MessageService.error(errResponse.data);
                    console.error('Error while updating current record.' + errResponse.data);
                }
            );
    };
    //********** Insert a new record*************
    self.newEdit = function () {
        MessageService.info("Editing selected record.");
        self.savedEditEntity = angular.copy(self.editEntity);
        self.editEntity = angular.copy(self.emptyEditEntity);
        self.editEntity['id'] = self.tempID++;
        self.editEntity['objectState'] = ObjectState.Added;
        self.setActiveTab(2);
        MessageService.info("Adding a new record..");
        self.isEditing=true;
    };

    //<editor-fold defaultstate="collapsed" desc="Batch-Edit-related operations">
    /*******************Begin of Batch-related operations***********************/
    //*****************save list of entities **********************
    self.batchSave = function () {
        var validationMsg=[];
        // (1) validate every Root record
        for(var i=0;i<self.entities.length;i++){ self.validateRec(self.entities[i],self.MetaInfo.Columns,validationMsg, i); }
        if (validationMsg.length>0) {self.showMessage(validationMsg,"Please make the following corrections");return;};
        var summary=self.readEditSummary(self.editSummary(self.entities));
        MessageService.info(summary, "Saving...");
        SearchEntityService.batchSave(self.entities, MODEL_CLASS_NAME)  //MODEL_CLASS_NAME is global var defined in jsp page
            .then(
                function (entity) {
                    scope.editForm.$setPristine(); //reset Form
                    MessageService.success("Changes have been saved successfully.");
                    self.savedEntities=angular.copy(self.entities); //set savedEntities to self.entities
                    self.isEditing=false;
                    self.hasBufferedChanges=false;
                },
                function (errResponse) {
                    alert(errResponse.data);
                    console.error('Error while saving....');
                }
            );
    };
    self.batchNewEdit = function () {
        var temp=angular.copy(self.emptyEditEntity);
        temp['id'] = self.tempID++;
        // set objectState to ObjectState.Added
        if (temp['objectState'] != "undefined") { temp['objectState'] = ObjectState.Added; }
        self.entities.push(temp);
        MessageService.success("Adding a new record...");
        //self.setActiveTab(2);
    };
    self.batchAddChild = function () {
        var temp=angular.copy(self.emptyEditEntity);
        temp['id'] = self.tempID++; // set temp ID
        // set objectState to ObjectState.Added
        if (temp['objectState'] != "undefined") { temp['objectState'] = ObjectState.Added; }
        self.entities.push(temp);
        MessageService.success("Adding a new record...");
        self.isEditing=true;
    };
    self.batchRemoveChild = function (id) {
        self.dialog("Are you sure to delete this record?", "Delete").then(
            function ok(response) { //"ok" if OK button clicked
                for (var i = 0; i < self.entities.length; i++) {
                    if (self.entities[i]['id'] === id) {
                        if (self.entities[i]['id'] <= 0) {
                            self.entities.splice(i, 1);
                            MessageService.success("New record discarded...");
                        } else {
                            self.entities[i]['objectState'] = ObjectState.Deleted;
                            MessageService.success("Existing record will be deleted when you save changes...");
                        }
                        self.isEditing=true;
                        return;
                    }
                }
                MessageService.error("Row ID:" + id + " not found...");
            },
            function cancel() { //"" if cancel button clicked
                console.log('Deletion cancelled. ' + new Date());
            }
        );
    };
    self.batchOnChange = function (id) {
        self.isEditing=true;
        for (var i = 0; i < self.entities.length; i++) {
            if (self.entities[i]['id'] === id) {    //found the record
                if (self.entities[i]['id'] >= 0) {  //existing record modified
                    if (self.entities[i]['objectState'] !== ObjectState.Modified) {
                        MessageService.info("Record modified...");
                    }
                    self.entities[i]['objectState'] = ObjectState.Modified;
                }
                return;
            }
        }
        MessageService.error("Row ID:" + id + " not found...");
    };
    self.batchShowChild = function (objectState) {
        console.log("objectstate=" + objectState);
        if (objectState == null || objectState != ObjectState.Deleted) {
            return true;
        }
        return false;
    };
    self.batchUnDoEdit = function () { /* copy old values back from saved copy*/
        self.dialog("Are you sure to discard all the changes ?", "Cancel changes").then(
            function ok(response) { //"ok" if OK button clicked
                var temp=angular.copy(self.entities);    // save changes for re-do
                self.entities = angular.copy(self.savedEntities); //self.savedEditEntity is original entities
                self.savedEntities=angular.copy(temp);
                scope.editForm.$setPristine(); //reset Form
                MessageService.success("All the changes have been canncelled...");
                self.isEditing=false;
                self.hasBufferedChanges=true;
            }
        );
    };
    self.batchReDoEdit = function () { /* copy old values back from saved copy*/
        self.dialog("Re-apply previous changes ?", "Re-apply changes").then(
            function ok(response) { //"ok" if OK button clicked
                var temp=angular.copy(self.savedEntities);
                self.savedEntities=angular.copy(self.entities);
                self.entities=angular.copy(temp);    // save changes for un-do
                MessageService.success("Changes re-applied...");
                self.isEditing=true;
            }
        );

    };
    //</editor-fold>

    //********** Insert a new record for batch edit*************
    // add a child entity
    self.addChild = function (childTable) {
        self.isEditing=true;
        var temp = angular.copy(self.emptyEditEntity[childTable][0]);
        temp['id'] = self.tempID++;   //make ID unique
        // set editEntity's ObjectState to ObjectState.Added
        if ('objectState' in temp) {
            temp['objectState'] = ObjectState.Added;
        } else {
            MessageService.error("objectState should be implemented on entity: "+childTable+" for update operation.");
        }
        // if the existing child record is passed from server, replace it, else add to child entities
        if (self.editEntity[childTable].length==1 && self.editEntity[childTable][0]['objectState']==ObjectState.Deleted && self.editEntity[childTable][0]['id']<=0){
            self.editEntity[childTable][0]=temp;
        }else {
            self.editEntity[childTable].push(temp);
        }
        MessageService.info("Adding a new item.");
    };
    // remove a child enitty
    self.removeChild = function (childTable, id) {
        self.dialog("Are you sure to delete this record?", "Delete").then(
            function ok(response) { //"ok" if OK button clicked
                var index = self.findRowById(self.editEntity[childTable], id);
                if (index >= 0) {  //found the child entity
                    //don't change ObjectState of newly inserted child
                    if (self.editEntity[childTable][index]['objectState'] == ObjectState.Added) {
                        self.editEntity[childTable].splice(index, 1);
                        MessageService.success("Removing newly inserted item.");
                    } else {
                        self.editEntity[childTable][index]['objectState'] = ObjectState.Deleted;
                        MessageService.warning("Item will be deleted when changes saved.");
                    }
                    self.isEditing=true;
                } else {
                    MessageService.error("Row ID:" + id + " not found...");
                }
            },
            function cancel() { //"" if cancel button clicked
                console.log('Deletion cancelled. ' + new Date());
            }
        );
    };

    // on modificatoin, set objectState to "Modified" for existing record
    self.onChange = function (id) {
        self.isEditing=true;
        if (self.editEntity['objectState'] !== ObjectState.Modified) {
            MessageService.info("Record modified...");
        }
        self.editEntity['objectState'] = ObjectState.Modified;
    };
    // on child-entity modificatoin, set objectState to "Modified" for existing record
    self.onChildChange = function (childTable, id) {
        self.isEditing=true;
        var index = self.findRowById(self.editEntity[childTable], id);
        if (index >= 0) {  //found the child entity
            //don't change ObjectState of newly inserted child
            if (self.editEntity[childTable][index]['objectState'] == ObjectState.Added) {
                return;
            }
            if (self.editEntity[childTable][index]['objectState'] !== ObjectState.Modified) {
                MessageService.info("Item modified...");
            }
            self.editEntity[childTable][index]['objectState'] = ObjectState.Modified;
        } else {
            MessageService.error("Row ID:" + id + " not found...");
        }
    };
    // locate a record in an array, return the row index if found, else return -1
    self.findRowById = function (oArray, id) {
        var index = -1;
        if (!oArray instanceof Array) {
            MessageService.error(oArray + " is not type of Array.");
            return index;
        }
        for (var i = 0; i < oArray.length; i++) {
            if (oArray[i]['id'] === id) {
                index = i;
                break;
            }
        }
        return index;
    }
    self.showChild = function (objectState) {
        return !(objectState === ObjectState.Deleted);
    };
    self.unDoEdit = function () { /* copy old values back from saved copy*/
        self.dialog("Are you sure to discard all the changes ?", "Cancel changes").then(
            function ok(response) { //"ok" if OK button clicked
                var temp=angular.copy(self.editEntity);
                self.editEntity = angular.copy(self.savedEditEntity);
                self.savedEditEntity=temp;
                MessageService.success("All the changes discarded.");
                scope.editForm.$setPristine();
                self.isEditing=false;
                self.hasBufferedChanges=true;
            }
        );
    };
    self.reDoEdit = function () { /* copy old values back from saved copy*/
        self.dialog("Are you sure to re-apply previous changes ?", "Apply changes").then(
            function ok(response) { //"ok" if OK button clicked
                var temp=angular.copy(self.savedEditEntity);
                self.savedEditEntity = angular.copy(self.editEntity);
                self.editEntity=temp;
                //self.editEntity = angular.copy(self.savedEditEntity);
                MessageService.success("All changes have been re-applied.");
                scope.editForm.$setPristine();
                self.isEditing=true;
            }
        );
    };
    self.deleteById = function (id) {
        var deletedId=id;
        self.dialog("Are you sure to delete this record?", "Delete").then(
            function ok(response) { //"ok" if OK button clicked
                SearchEntityService.deleteById(id).then(
                    function (id) {
                        // remove deleted entity from self.entity List
                        for(var i in self.entities){if (self.entities[i]['id']==deletedId) {
                            self.entities.splice(i,1);
                            self.displayEntities=angular.copy(self.entities);
                            break;}}
                        self.reset();
                        self.editEntity=angular.copy(self.emptyEditEntity);
                        self.setActiveTab(1);
                        MessageService.success("The record has been successfully deleted.");
                    },
                    function (errResponse) {
                        MessageService.error(errResponse.data);
                        console.error('Error while deleting the record.');
                    }
                );
            },
            function cancel() { //"" if cancel button clicked
                console.log('Deletion cancelled... ' + new Date());
            }
        );

    };

    // when click on Edit button
    self.edit = function (id) {
        SearchEntityService.getEntityById(id)
            .then(
                function (entity) {
                    MessageService.success("Editing selected record..");
                    self.editEntity = angular.copy(entity);
                    self.savedEditEntity = angular.copy(entity);
                    self.isEditing=false;
                    self.hasBufferedChanges=false;
                    self.setActiveTab(2);       //activate edit tab
                }
            );
    };

    self.fillField = function () {
        for (var fieldName in self.editEntity) {
            if (getFieldType(fieldName)=="String") self.editEntity[fieldName] = 'xx' + Math.floor(Math.random() * 1000);
        }
    };
    self.debug = function () {
        self=this;
        debugger;
    };
    self.reset = function () {
        scope.searchForm.$setPristine(); //reset Form
        scope.editForm.$setPristine();
        self.isEditing=false;
        self.hasBufferedChanges=false;
    };
}]);

App.factory('SearchEntityService', ['$http', '$q', function ($http, $q) {
    return {
        fetchAllEntities: function () {
            return $http.get(Entity_REST_URL)
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        console.error('Error while fetching tableinfos');
                        return $q.reject(errResponse);
                    }
                );
        },
        // search with criteria
        search: function (jsonStr) {
            console.log("SearchEntityService.searching...\n" + jsonStr);
            return $http.post(Entity_REST_URL + "search", jsonStr)
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        console.error('Error while fetching records of ' + searchModelClassName);
                        return $q.reject(errResponse);
                    }
                );
        },

        // pass entity and entity class name (e.g. "com.james.domain.Employee") to server
        // when "id" is present, save will update the record, otherwise, it would insert a new record
        save: function (entity, modelClassName) {
            var jsonStr = JSON.stringify({entity: entity, modelClassName: modelClassName});
            console.log("saving...\n" + jsonStr);
            return $http.post(Entity_REST_URL, jsonStr)
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        console.error('Error while updating the record:\n' + errResponse.data);
                        return $q.reject(errResponse);
                    }
                );
        },
        // save a list of entities to DB
        batchSave: function (entityList, modelClassName) {
            var jsonStr = JSON.stringify({entity: entityList, modelClassName: modelClassName});
            console.log("saving...\n" + jsonStr);
            return $http.post(Entity_REST_URL+"batch", jsonStr)
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        console.error('Error while updating the record:\n' + errResponse.data);
                        return $q.reject(errResponse);
                    }
                );
        },
        // edit a record by retrieving it from server
        getEntityById: function (id) {
            return $http.get(Entity_REST_URL + id)
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        console.error('Error retrieving record with id=' + id);
                        return $q.reject(errResponse);
                    }
                );
        },
        // use entity type to get an empty instance
        getEmptyEntityInstance: function () {
            //if (isEmpty(ENTITY_INSTANCE_URL)) alert("Please specify the Root Model Class name in view page.");
            return $http.get(ENTITY_INSTANCE_URL)
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        console.error('Error while getEmptyEntityInstance');
                        return $q.reject(errResponse);
                    }
                );
        },
        // delete a record
        deleteById: function (id) {
            return $http.delete(Entity_REST_URL + id)
                .then(
                    function (response) {
                        return response.data;
                    },
                    function (errResponse) {
                        //alert(errResponse.data);
                        console.error('Error while deleting the record');
                        return $q.reject(errResponse);
                    }
                );
        }
    };

}]);
// return true if a variable/object is empty
function isEmpty(obj){
    // if obj is of Boolean type and false, considered NOT empty
    return (obj==undefined || obj==null || (obj+"").trim().length==0);
};