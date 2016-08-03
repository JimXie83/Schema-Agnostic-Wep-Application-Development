<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html ng-app="myApp">
<head>
    <title>Employee Management</title>
    <script src="<c:url value="/content/js/angular/1.5.3/angular.js"/>"></script>
    <script src="<c:url value="/content/js/angular/1.5.3/angular-animate.js"/>"></script>
    <script src="<c:url value="/content/js/angular-ui/ui-bootstrap-tpls.js"/>"></script>
    <script src="<c:url value="/content/js/angular-smart-table/smart-table.js"/>"></script>
    <link href="<c:url value="/content/css/bootstrap/css/bootstrap.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/content/js/angular-toastr/angular-toastr.css"/>" rel="stylesheet"/>
    <script src="<c:url value="/content/js/angular-toastr/angular-toastr.tpls.js"/>"></script>
    <link href="<c:url value="/content/css/app.css"/>" rel="stylesheet"/>

    <script type="text/javascript"> var MODEL_CLASS_NAME = "com.jamesx.domain.Employees"; </script>
    <script src="<c:url value="/content/js/SearchEditForAll.js"/>"></script>
</head>
<body ng-controller="SearchEntityController as ctrl">

<div class="generic-container" style="white-space: nowrap;">
    <ul class="nav nav-tabs">
        <li ng-class="{'active' : ctrl.activeTab == 1}"><a href="#" ng-click="ctrl.setActiveTab(1)">Search employees Information</a></li>
        <li ng-class="{'active' : ctrl.activeTab == 2}"><a href="#" ng-click="ctrl.setActiveTab(2)">Edit employees Information</a></li>
    </ul>

    <div class="tab-content" ng-cloak>
        <div ng-class="{'tab-pane active': ctrl.activeTab === 1, 'tab-pane' : ctrl.activeTab !== 1}">
            <form name="searchForm" role="form">
                <div class="panel panel-default">
                    <div class="formcontainer">
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Employee No.</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="empNo"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.empNo" name="empNo"
                                           class="form-control input-sm" placeholder="Enter Employee No."/>
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Date of Birth</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm"
                                            search_col="birthDate"></select>

                                    <div my-date-picker ng-model="ctrl.searchEntity.birthDate"></div>
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Last Name</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm"
                                            search_col="lastName"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.lastName" name="lastName"
                                           class="form-control input-sm" placeholder="Enter Last Name"/>
                                </div>
                            </div>


                        </div>
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Gender</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm"
                                            search_col="gender"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.gender" name="gender"
                                           class="form-control input-sm" placeholder="Enter Gender"/>
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Active</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm"
                                            search_col="active"></select>
                                    <label class="control-lable input-sm"><input type="checkbox" width="20px"
                                                                                 ng-model="ctrl.searchEntity.active">Active</label>

                                </div>
                            </div>


                        </div>

                        <div class="row top30">
                            <div class="form-group row text-center">
                                <button ng-click="ctrl.search()" class="btn btn-success custom-width" title="Search records with specified criteria"><span class="glyphicon glyphicon-search"></span> Search </button>
                                <button ng-click="ctrl.clearSearch()" class="btn btn-success custom-width" title="Clear all search criteria"><span class="glyphicon glyphicon-scissors"></span> Clear </button>
                                <button ng-click="ctrl.showSearchFilters()" class="btn btn-success custom-width" title="Dispaly specified search criteria"><span class="glyphicon glyphicon-eye-open"></span> Criteria </button>
                                <button ng-click="ctrl.reDoSearch()" class="btn btn-success custom-width" title="Recover previou inputs"><span class="glyphicon glyphicon-repeat"></span> Re-Do </button>
                                <button ng-click="ctrl.newEdit()" class="btn btn-success custom-width" title="Create a new record"><span class="glyphicon glyphicon-plus-sign"></span> New </button>
                                <%--<button ng-click="ctrl.debug()" class="btn btn-success custom-width" title="Debug"> Debug</button>--%>
                            </div>
                        </div>
                    </div>
                </div>
            </form>

            <table st-table="ctrl.displayEntities" st-safe-src="ctrl.entities"
                   class="myTable" ng-show="!ctrl.isLoading">

                <thead>
                <tr>
                    <th st-sort="empNo">Employee No.</th>
                    <th st-sort="birthDate">Date of Birth</th>
                    <th st-sort="firstName">First Name</th>
                    <th st-sort="lastName">Last Name</th>
                    <th st-sort="gender">Gender</th>
                    <th st-sort="hireDate">Hire Date</th>

                    <th width="3%"></th>
                </tr>
                </thead>
                <tbody>
                <tr ng-repeat="u in ctrl.displayEntities">
                    <td><span ng-bind="u.empNo"></span></td>
                    <td><span ng-bind="u.birthDate | {{ctrl.DateFormat}}"></span></td>
                    <td><span ng-bind="u.firstName"></span></td>
                    <td><span ng-bind="u.lastName"></span></td>
                    <td><span ng-bind="u.gender"></span></td>
                    <td><span ng-bind="u.hireDate | {{ctrl.DateFormat}}"></span></td>

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
                                <div class="col-md-3 input-sm">Employee No.</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.empNo"
                                           name="empNo" ng-change="ctrl.onChange()" maxlength="6"
                                           class="form-control input-sm" placeholder="Enter Employee No."/>
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Date of Birth</div>
                                <div class="col-md-1">
                                    <div my-date-picker ng-change="ctrl.onChange()"
                                         ng-model="ctrl.editEntity.birthDate"></div>
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">First Name</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.firstName"
                                           name="firstName" ng-change="ctrl.onChange()" maxlength="14"
                                           class="form-control input-sm" placeholder="Enter First Name"/>
                                </div>
                            </div>


                        </div>
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Last Name</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.lastName"
                                           name="lastName" ng-change="ctrl.onChange()" maxlength="16"
                                           class="form-control input-sm" placeholder="Enter Last Name"/>
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Gender</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.gender"
                                           name="gender" ng-change="ctrl.onChange()" maxlength="6"
                                           class="form-control input-sm" placeholder="Enter Gender"/>
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Hire Date</div>
                                <div class="col-md-1">
                                    <div my-date-picker ng-change="ctrl.onChange()"
                                         ng-model="ctrl.editEntity.hireDate"></div>
                                </div>
                            </div>


                        </div>
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Active</div>
                                <div class="col-md-1">
                                    <label class="col-md-1 control-lable input-sm"><input type="checkbox" width="20px"
                                                                                          ng-change="ctrl.onChange()"
                                                                                          ng-model="ctrl.editEntity.active">Active</label>

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
                                <%--<button ng-click="ctrl.debug()" class="btn btn-success custom-width" title="Debug"> Debug</button>--%>
                            </div>
                        </div>

                        <table class="top30 im-centered">
                            <thead>
                            <tr>
                                <th>Employee Salary History</th>
                                <th>Employee Titles</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td valign="top">
                                    <div>
                                        <table class="myTable">
                                            <thead>
                                            <tr>
                                                <th nowrap width="25%">Salary</th>

                                                <th nowrap width="25%">From Date</th>

                                                <th nowrap width="25%">To Date</th>

                                                <th width="5%" align="right">
                                                    <button class="btn btn-warning custom-width" href="#"
                                                            ng-click="ctrl.addChild('salaries')">
                                                        <span class="glyphicon glyphicon-plus-sign">Add</span></button>
                                                </th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr ng-repeat="u in ctrl.editEntity.salaries"
                                                ng-show="ctrl.showChild(u.objectState)">

                                                <td>
                                                    <input ng-model="u.salary"
                                                           ng-change="ctrl.onChildChange('salaries',u.id)"
                                                           class="form-control input-sm"/>

                                                </td>
                                                <td>
                                                    <div class="input-group" ng-controller="myDatePickerCtrl" style="min-width: 166px;">
                                                        <input type="text" ng-model="u.fromDate" ng-change="ctrl.onChildChange('salaries',u.id)" class="form-control input-sm" uib-datepicker-popup="{{format}}"  is-open="opened" ng-required="true" close-text="Close" />
                                                        <span class="input-group-btn"> <button type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button> </span>
                                                    </div>

                                                    <%--<div my-date-picker ng-model="u.fromDate" ng-change="ctrl.onChildChange('salaries',u.id)"></div>--%>

                                                </td>
                                                <td>
                                                    <div class="input-group" ng-controller="myDatePickerCtrl" style="min-width: 166px;">
                                                        <input type="text" ng-model="u.toDate" ng-change="ctrl.onChildChange('salaries',u.id)" class="form-control input-sm" uib-datepicker-popup="{{format}}"  is-open="opened" ng-required="true" close-text="Close" />
                                                        <span class="input-group-btn"> <button type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button> </span>
                                                    </div>

                                                    <%--<div my-date-picker ng-model="u.toDate" ng-change="ctrl.onChildChange('salaries',u.id)"></div>--%>

                                                </td>

                                                <td>
                                                    <button type="button" ng-click="ctrl.removeChild('salaries', u.id)"
                                                            class="btn btn-danger custom-width"> Del
                                                    </button>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </td>
                                <td valign="top">
                                    <div>
                                        <table class="myTable table-hover">
                                            <thead>
                                            <tr>
                                                <th nowrap width="25%">Title</th>

                                                <th nowrap width="25%">From Date</th>

                                                <th nowrap width="25%">To date</th>

                                                <th width="5%">
                                                    <button class="btn btn-warning custom-width" href="#"
                                                            ng-click="ctrl.addChild('titles')">
                                                        <span class="glyphicon glyphicon-plus-sign">Add</span></button>
                                                </th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr ng-repeat="u in ctrl.editEntity.titles"
                                                ng-show="ctrl.showChild(u.objectState)">
                                                <td>
                                                    <input ng-model="u.title"
                                                           ng-change="ctrl.onChildChange('titles',u.id)"
                                                           class="form-control input-sm"/>

                                                </td>
                                                <td>
                                                    <div class="input-group" ng-controller="myDatePickerCtrl" style="min-width: 166px;">
                                                        <input type="text" ng-model="u.fromDate" ng-change="ctrl.onChildChange('titles',u.id)" class="form-control input-sm" uib-datepicker-popup="{{format}}"  is-open="opened" ng-required="true" close-text="Close" />
                                                        <span class="input-group-btn"> <button type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button> </span>
                                                    </div>
                                                    <%--<div my-date-picker ng-model="u.fromDate"--%>
                                                         <%--ng-change="ctrl.onChildChange('titles',u.id)"></div>--%>

                                                </td>
                                                <td>
                                                    <div class="input-group" ng-controller="myDatePickerCtrl" style="min-width: 166px;">
                                                        <input type="text" ng-model="u.toDate" ng-change="ctrl.onChildChange('titles',u.id)" class="form-control input-sm" uib-datepicker-popup="{{format}}"  is-open="opened" ng-required="true" close-text="Close" />
                                                        <span class="input-group-btn"> <button type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button> </span>
                                                    </div>
                                                    <%--<div my-date-picker ng-model="u.toDate"--%>
                                                         <%--ng-change="ctrl.onChildChange('titles',u.id)"></div>--%>

                                                </td>

                                                <td>

                                                    <button type="button" ng-click="ctrl.removeChild('titles', u.id)"
                                                            class="btn btn-danger custom-width"> Del
                                                    </button>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                            </tbody>

                        </table>


                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

</body>
</html>