<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html ng-app="myApp">
<head>
    <title>category Info Management</title>
    <script src="<c:url value="/content/js/angular/1.5.3/angular.js"/>"></script>
    <script src="<c:url value="/content/js/angular/1.5.3/angular-animate.js"/>"></script>
    <script src="<c:url value="/content/js/angular-ui/ui-bootstrap-tpls.js"/>"></script>
    <script src="<c:url value="/content/js/angular-smart-table/smart-table.js"/>"></script>
    <link href="<c:url value="/content/css/bootstrap/css/bootstrap.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/content/js/angular-toastr/angular-toastr.css"/>" rel="stylesheet"/>
    <script src="<c:url value="/content/js/angular-toastr/angular-toastr.tpls.js"/>"></script>
    <link href="<c:url value="/content/css/app.css"/>" rel="stylesheet"/>

    <script type="text/javascript">
        var MODEL_CLASS_NAME = "com.jamesx.domain.Category";
    </script>
    <script src="<c:url value="/content/js/SearchEditForAll.js"/>"></script>
</head>
<body ng-controller="SearchEntityController as ctrl">

            <form name="searchForm" role="form">
                <div class="panel panel-default" style="white-space: nowrap;">
                    <div class="panel-heading"><span class="lead">category</span></div>
                    <div class="formcontainer">
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Category No.</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="categoryNo"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.categoryNo"  name="categoryNo"
                                           class="form-control input-sm" placeholder="Enter Category No."/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Category Name</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="categoryName"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.categoryName"  name="categoryName"
                                           class="form-control input-sm" placeholder="Enter Category Name"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Note</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="note"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.note"  name="note"
                                           class="form-control input-sm" placeholder="Enter Note"/>                             
                                </div>
                            </div>


                        </div>
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Date Created</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="dateCreated"></select>
                                    <div my-date-picker ng-model="ctrl.searchEntity.dateCreated"></div>                          
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Active</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="isActive"></select>
                                    <label class="control-lable input-sm"><input type="checkbox" width="20px" ng-model="ctrl.searchEntity.isActive">Active</label>

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
		    <div>
			<table st-table="ctrl.displayEntities" st-safe-src="ctrl.entities" class="myTable table-striped" ng-show="ctrl.entities.length>0">
			    <thead>
			    <tr>
                                <th width="0%"><div>#</div></th>
                             <th st-sort="categoryNo" width="20%">Category No.</th>
                             <th st-sort="categoryName" width="20%">Category Name</th>
                             <th st-sort="note" width="20%">Note</th>
                             <th st-sort="dateCreated" width="20%">Date Created</th>
                             <th st-sort="isActive" width="1%%">Active</th>

				<th width="3%"></th>
			    </tr>
			    </thead>
			    <tbody>
			    <tr ng-repeat="u in ctrl.displayEntities" ng-show="ctrl.batchShowChild(u.objectState)">
                                <td>{{$index + 1}}</td>
                                <td><input ng-model="u.categoryNo" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>
                                <td><input ng-model="u.categoryName" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>
                                <td><input ng-model="u.note" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>
                                    <td>
                                    <div class="input-group" ng-controller="myDatePickerCtrl" style="min-width: 166px;">
                                        <input type="text" ng-model="u.dateCreated" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm" uib-datepicker-popup="{{format}}" placeholder="{{format}}" is-open="opened" close-text="Close" />
                                        <span class="input-group-btn"> <button type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button> </span>
                                    </div>
                                    </td>
                                    <%--<td><div my-date-picker ng-model="u.dateCreated" ng-change="ctrl.batchOnChange(u.id)"></div></td>--%> 
                                <td><input type="checkbox" ng-model="u.isActive" ng-change="ctrl.batchOnChange(u.id)" class="form-control input-sm"/></td>           

				<td nowrap="">
				    <button type="button" ng-click="ctrl.batchRemoveChild(u.id)"
					    class="btn btn-danger custom-width"> Remove
				    </button>
				</td>
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