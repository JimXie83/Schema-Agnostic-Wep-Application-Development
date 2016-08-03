<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html ng-app="myApp">
<head>
    <title>contacts Info Management</title>
    <script src="<c:url value="/content/js/angular/1.5.3/angular.js"/>"></script>
    <script src="<c:url value="/content/js/angular/1.5.3/angular-animate.js"/>"></script>
    <script src="<c:url value="/content/js/angular-ui/ui-bootstrap-tpls.js"/>"></script>
    <script src="<c:url value="/content/js/angular-smart-table/smart-table.js"/>"></script>
    <link href="<c:url value="/content/css/bootstrap/css/bootstrap.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/content/js/angular-toastr/angular-toastr.css"/>" rel="stylesheet"/>
    <script src="<c:url value="/content/js/angular-toastr/angular-toastr.tpls.js"/>"></script>
    <link href="<c:url value="/content/css/app.css"/>" rel="stylesheet"/>

    <script type="text/javascript">
        var MODEL_CLASS_NAME = "com.jamesx.domain.Contacts";
    </script>
    <script src="<c:url value="/content/js/SearchEditForAll.js"/>"></script>
</head>
<body ng-controller="SearchEntityController as ctrl">

<div class="generic-container" style="white-space: nowrap;">
    <ul class="nav nav-tabs">
        <li ng-class="{'active' : ctrl.activeTab == 1}"><a href="#" ng-click="ctrl.setActiveTab(1)">Search Contact
            Information</a></li>
        <li ng-class="{'active' : ctrl.activeTab == 2}"><a href="#" ng-click="ctrl.setActiveTab(2)">Edit Contact
            Information</a></li>
    </ul>

    <div class="tab-content" ng-cloak>
        <div ng-class="{'tab-pane active': ctrl.activeTab === 1, 'tab-pane' : ctrl.activeTab !== 1}">
            <form name="searchForm" role="form">
                <div class="panel panel-default">
                    <div class="formcontainer">
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Last Name</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="lastName"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.lastName"  name="lastName"
                                           class="form-control input-sm" placeholder="Enter Last Name"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">First Name</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="firstName"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.firstName"  name="firstName"
                                           class="form-control input-sm" placeholder="Enter First Name"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Birthdate</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="birthday"></select>
                                    <div my-date-picker ng-model="ctrl.searchEntity.birthday"></div>
                                </div>
                            </div>


                        </div>
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Country</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="country"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.country"  name="country"
                                           class="form-control input-sm" placeholder="Enter Country"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">State</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="state"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.state"  name="state"
                                           class="form-control input-sm" placeholder="Enter State"/>                             
                                </div>
                            </div>


                        </div>

                        <div class="row top30">
                            <div class="form-group row text-center">
                                <button ng-click="ctrl.search()" class="btn btn-success custom-width"
                                        title="Search records with specified criteria"><span
                                        class="glyphicon glyphicon-search"></span> Search
                                </button>
                                <button ng-click="ctrl.clearSearch()" class="btn btn-success custom-width"
                                        title="Clear all search criteria"><span
                                        class="glyphicon glyphicon-scissors"></span> Clear
                                </button>
                                <button ng-click="ctrl.showSearchFilters()" class="btn btn-success custom-width"
                                        title="Dispaly specified search criteria"><span
                                        class="glyphicon glyphicon-eye-open"></span> Criteria
                                </button>
                                <button ng-click="ctrl.reDoSearch()" class="btn btn-success custom-width"
                                        title="Recover previou inputs"><span
                                        class="glyphicon glyphicon-repeat"></span>
                                    Re-Do
                                </button>
                                <button ng-click="ctrl.newEdit()" class="btn btn-success custom-width"
                                        title="Create a new record"><span
                                        class="glyphicon glyphicon-plus-sign"></span>
                                    New
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

            <table st-table="ctrl.displayEntities" st-safe-src="ctrl.entities" 
                   class="myTable table-striped" ng-show="ctrl.totalCount>0">

                <thead>
                <tr>
                    <th st-sort="lastName">Last Name</th> 
                    <th st-sort="firstName">First Name</th> 
                    <th st-sort="birthday">Birthdate</th> 
                    <th st-sort="country">Country</th> 
                    <th st-sort="state">State</th> 
                    <th st-sort="zip">Zip</th> 

                    <th width="3%"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="u in ctrl.displayEntities">
                 <td><span ng-bind="u.lastName"></span></td>   
                 <td><span ng-bind="u.firstName"></span></td>   
                 <td><span ng-bind="u.birthday | {{ctrl.DateFormat}}"></span></td>   
                 <td><span ng-bind="u.country"></span></td>   
                 <td><span ng-bind="u.state"></span></td>   
                 <td><span ng-bind="u.zip"></span></td>   

                    <td>
                        <button type="button" ng-click="ctrl.edit(u.id)" class="btn btn-success custom-width"> Edit
                        </button>
                    </td>

                </tr>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="8" class="text-center">
                        <div st-pagination="" st-items-by-page="ctrl.pageSize"></div>
                    </td>
                </tr>
                </tfoot>
            </table>

        </div>
        <div ng-class="{'tab-pane active': ctrl.activeTab === 2, 'tab-pane' : ctrl.activeTab !== 2}">
            <div class="panel panel-default">
                <div class="formcontainer">
                    <form name="editForm">
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Last Name</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.lastName"
                                           name="lastName" ng-change="ctrl.onChange()" maxlength="30"
                                           class="form-control input-sm" placeholder="Enter Last Name"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">First Name</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.firstName"
                                           name="firstName" ng-change="ctrl.onChange()" maxlength="25"
                                           class="form-control input-sm" placeholder="Enter First Name"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Birthdate</div>
                                <div class="col-md-1">
                                    <div class="input-group" ng-controller="myDatePickerCtrl" style="min-width: 166px;">
                                        <input type="text" ng-model="ctrl.editEntity.birthday" ng-change="ctrl.onChange()" class="form-control input-sm" uib-datepicker-popup="{{format}}"  is-open="opened" ng-required="true" close-text="Close" />
                                        <span class="input-group-btn"> <button type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button> </span>
                                    </div>
                                    <%--<div my-date-picker ng-change="ctrl.onChange()" ng-model="ctrl.editEntity.birthday"></div>--%>
                                </div>
                            </div>


                        </div>
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Place of Birth</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.brithplace"
                                           name="brithplace" ng-change="ctrl.onChange()" maxlength="60"
                                           class="form-control input-sm" placeholder="Enter Place of Birth"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Country</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.country"
                                           name="country" ng-change="ctrl.onChange()" maxlength="40"
                                           class="form-control input-sm" placeholder="Enter Country"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">State</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.state"
                                           name="state" ng-change="ctrl.onChange()" maxlength="40"
                                           class="form-control input-sm" placeholder="Enter State"/>                             
                                </div>
                            </div>


                        </div>
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">City</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.city"
                                           name="city" ng-change="ctrl.onChange()" maxlength="40"
                                           class="form-control input-sm" placeholder="Enter City"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Street</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.street"
                                           name="street" ng-change="ctrl.onChange()" maxlength="40"
                                           class="form-control input-sm" placeholder="Enter Street"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Zip</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.zip"
                                           name="zip" ng-change="ctrl.onChange()" maxlength="40"
                                           class="form-control input-sm" placeholder="Enter Zip"/>                             
                                </div>
                            </div>


                        </div>

                        <div class="row top30">
                            <div class="form-group row text-center">
                                <button ng-click="ctrl.save()" ng-disabled="!ctrl.isEditing" class="btn btn-success custom-width" title="Recover previou inputs"><span class="glyphicon glyphicon-floppy-disk"></span> Save </button>
                                <button ng-click="ctrl.newEdit()" ng-disabled="ctrl.isEditing" class="btn btn-success custom-width" title="Create a new record"><span class="glyphicon glyphicon-plus-sign"></span> New </button>
                                <button ng-click="ctrl.unDoEdit()" ng-show="ctrl.isEditing" class="btn btn-success custom-width" title="Cancel all the changes"><span class="glyphicon glyphicon-plus-sign"></span> Un-Do </button>
                                <button ng-click="ctrl.reDoEdit()" ng-show="!ctrl.isEditing && ctrl.hasBufferedChanges" class="btn btn-success custom-width" title="Re-Apply previous changes"><span class="glyphicon glyphicon-repeat"></span> Re-Do </button>
                                <button type="button" ng-disabled="ctrl.isEditing" ng-click="ctrl.deleteById(ctrl.editEntity.id)" class="btn btn-danger custom-width" title="Delete this record"><span class="glyphicon glyphicon-trash"></span> Remove </button>

                                <button ng-click="ctrl.fillField()" class="btn btn-success custom-width">Fill</button>
                                <button ng-click="ctrl.debug()" class="btn btn-success custom-width" title="Debug"> Debug</button>

                            </div>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

</body>
</html>