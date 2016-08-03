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

<form name="searchForm" role="form">
    <div class="panel panel-default" style="white-space: nowrap;">
        <div class="panel-heading"><span class="lead">Contact Information</span></div>
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
                    <div class="col-md-3 control-lable input-sm">prénom</div>
                    <div class="col-md-1">
                        <select class="search_op pull-left form-control input-sm" search_col="firstName"></select>
                        <input type="text" ng-model="ctrl.searchEntity.firstName"  name="firstName"
                               class="form-control input-sm" placeholder="Enter prénom"/>
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
                    <div class="col-md-3 control-lable input-sm">lieu de naissance</div>
                    <div class="col-md-1">
                        <select class="search_op pull-left form-control input-sm" search_col="brithplace"></select>
                        <input type="text" ng-model="ctrl.searchEntity.brithplace"  name="brithplace"
                               class="form-control input-sm" placeholder="Enter lieu de naissance"/>
                    </div>
                </div>
                <div class="form-group col-md-4">
                    <div class="col-md-3 control-lable input-sm">pays</div>
                    <div class="col-md-1">
                        <select class="search_op pull-left form-control input-sm" search_col="country"></select>
                        <input type="text" ng-model="ctrl.searchEntity.country"  name="country"
                               class="form-control input-sm" placeholder="Enter pays"/>
                    </div>
                </div>
                <div class="form-group col-md-4">
                    <div class="col-md-3 control-lable input-sm">state</div>
                    <div class="col-md-1">
                        <select class="search_op pull-left form-control input-sm" search_col="state"></select>
                        <input type="text" ng-model="ctrl.searchEntity.state"  name="state"
                               class="form-control input-sm" placeholder="Enter state"/>
                    </div>
                </div>


            </div>
            <div class="row form-inline top10">
                <div class="form-group col-md-4">
                    <div class="col-md-3 control-lable input-sm">City</div>
                    <div class="col-md-1">
                        <select class="search_op pull-left form-control input-sm" search_col="city"></select>
                        <input type="text" ng-model="ctrl.searchEntity.city"  name="city"
                               class="form-control input-sm" placeholder="Enter City"/>
                    </div>
                </div>
                <div class="form-group col-md-4">
                    <div class="col-md-3 control-lable input-sm">Street</div>
                    <div class="col-md-1">
                        <select class="search_op pull-left form-control input-sm" search_col="street"></select>
                        <input type="text" ng-model="ctrl.searchEntity.street"  name="street"
                               class="form-control input-sm" placeholder="Enter Street"/>
                    </div>
                </div>
                <div class="form-group col-md-4">
                    <div class="col-md-3 control-lable input-sm">Zip</div>
                    <div class="col-md-1">
                        <select class="search_op pull-left form-control input-sm" search_col="zip"></select>
                        <input type="text" ng-model="ctrl.searchEntity.zip"  name="zip"
                               class="form-control input-sm" placeholder="Enter Zip"/>
                    </div>
                </div>


            </div>

            <div class="row top30">
                <div class="form-group row text-center">
                    <button ng-click="ctrl.search()" ng-disabled="ctrl.isEditing" class="btn btn-success custom-width" title="Search records with specified criteria"><span class="glyphicon glyphicon-search"></span> Search </button>
                    <button ng-click="ctrl.clearSearch()" class="btn btn-success custom-width" title="Clear all search criteria"><span class="glyphicon glyphicon-scissors"></span> Clear </button>
                    <button ng-click="ctrl.showSearchFilters()" class="btn btn-success custom-width" title="Dispaly specified search criteria"><span class="glyphicon glyphicon-eye-open"></span> Criteria </button>
                    <button ng-click="ctrl.batchSave()" ng-disabled="!ctrl.isEditing" class="btn btn-success custom-width" title="Recover previou inputs"><span class="glyphicon glyphicon-floppy-disk"></span> Save</button>
                    <button ng-click="ctrl.batchUnDoEdit()" ng-show="ctrl.isEditing" class="btn btn-success custom-width" title="Cancel all the changes"><span class="glyphicon glyphicon-plus-sign"></span> Un-Do</button>
                    <button ng-click="ctrl.batchReDoEdit()" ng-show="!ctrl.isEditing && ctrl.hasBufferedChanges" class="btn btn-success custom-width" title="Re-Apply previous changes"><span class="glyphicon glyphicon-repeat"></span> Re-Do </button>
                    <button ng-click="ctrl.batchAddChild()" class="btn btn-success custom-width" title="Create a new record"><span class="glyphicon glyphicon-plus-sign"></span> New </button>
                    <%--<button ng-click="ctrl.debug()" class="btn btn-success custom-width" title="Debug"> Debug</button>--%>
                </div>
            </div>
        </div>
    </div>
</form>
<form name="editForm">
    <div class="panel panel-default">
        <div class="tablecontainer">
            <table st-table="ctrl.displayEntities" st-safe-src="ctrl.entities" class="myTable table-striped" ng-show="ctrl.entities.length>0">
                <thead>
                <tr>
                    <th width="0%">#</th>
                    <th st-sort="lastName" width="11%">Last Name</th>
                    <th st-sort="firstName" width="11%">prénom</th>
                    <th st-sort="birthday" width="11%">Birthdate</th>
                    <th st-sort="brithplace" width="11%">lieu de naissance</th>
                    <th st-sort="country" width="11%">pays</th>
                    <th st-sort="state" width="11%">state</th>
                    <th st-sort="city" width="11%">City</th>
                    <th st-sort="street" width="11%">Street</th>
                    <th st-sort="zip" width="11%">Zip</th>

                    <th width="3%"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="u in ctrl.displayEntities" ng-show="ctrl.batchShowChild(u.objectState)">
                    <td>{{$index + 1}}</td>
                    <td><input ng-model="u.lastName" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>
                    <td><input ng-model="u.firstName" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>
                    <td><div my-date-picker ng-model="u.birthday" ng-change="ctrl.batchOnChange(u.id)"></div></td>
                    <td><input ng-model="u.brithplace" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>
                    <td><input ng-model="u.country" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>
                    <td><input ng-model="u.state" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>
                    <td><input ng-model="u.city" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>
                    <td><input ng-model="u.street" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>
                    <td><input ng-model="u.zip" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>

                    <td> <button type="button" ng-click="ctrl.batchRemoveChild(u.id)" class="btn btn-danger custom-width"> Remove</button> </td>
                </tr>
                </tbody>
                <tfoot>
                </tfoot>
            </table>


        </div>
    </div>
</form>
</body>
</html>