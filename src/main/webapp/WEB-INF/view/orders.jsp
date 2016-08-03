<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<html ng-app="myApp">
<head>
    <title>orders Info Management</title>
    <script src="<c:url value="/content/js/angular/1.5.3/angular.js"/>"></script>
    <script src="<c:url value="/content/js/angular/1.5.3/angular-animate.js"/>"></script>
    <script src="<c:url value="/content/js/angular-ui/ui-bootstrap-tpls.js"/>"></script>
    <script src="<c:url value="/content/js/angular-smart-table/smart-table.js"/>"></script>
    <link href="<c:url value="/content/css/bootstrap/css/bootstrap.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/content/js/angular-toastr/angular-toastr.css"/>" rel="stylesheet"/>
    <script src="<c:url value="/content/js/angular-toastr/angular-toastr.tpls.js"/>"></script>
    <link href="<c:url value="/content/css/app.css"/>" rel="stylesheet"/>

    <script type="text/javascript">
        var MODEL_CLASS_NAME = "com.jamesx.domain.Orders";
    </script>
    <script src="<c:url value="/content/js/SearchEditForAll.js"/>"></script>
</head>
<body ng-controller="SearchEntityController as ctrl">

<div class="generic-container" style="white-space: nowrap;">
    <ul class="nav nav-tabs">
        <li ng-class="{'active' : ctrl.activeTab == 1}"><a href="#" ng-click="ctrl.setActiveTab(1)">Search orders
            Information</a></li>
        <li ng-class="{'active' : ctrl.activeTab == 2}"><a href="#" ng-click="ctrl.setActiveTab(2)">Edit orders
            Information</a></li>
    </ul>

    <div class="tab-content" ng-cloak>
        <div ng-class="{'tab-pane active': ctrl.activeTab === 1, 'tab-pane' : ctrl.activeTab !== 1}">
            <form name="searchForm" role="form">
                <div class="panel panel-default">
                    <div class="formcontainer">
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Order No.</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="orderNo"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.orderNo"  name="orderNo"
                                           class="form-control input-sm" placeholder="Enter Order No."/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Order Date</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="orderDate"></select>
                                    <div my-date-picker ng-model="ctrl.searchEntity.orderDate"></div>                          
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Shipping Date</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="shippingDate"></select>
                                    <div my-date-picker ng-model="ctrl.searchEntity.shippingDate"></div>                          
                                </div>
                            </div>


                        </div>
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Status</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="orderStatus"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.orderStatus"  name="orderStatus"
                                           class="form-control input-sm" placeholder="Enter Status"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 control-lable input-sm">Courier</div>
                                <div class="col-md-1">
                                    <select class="search_op pull-left form-control input-sm" search_col="courier"></select>
                                    <input type="text" ng-model="ctrl.searchEntity.courier"  name="courier"
                                           class="form-control input-sm" placeholder="Enter Courier"/>                             
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
                   class="myTable table-striped" ng-show="ctrl.entities.length>0">

                <thead>
                <tr>
                    <th st-sort="orderNo">Order No.</th> 
                    <th st-sort="orderDate">Order Date</th> 
                    <th st-sort="shippingDate">Shipping Date</th> 
                    <th st-sort="orderStatus">Status</th> 
                    <th st-sort="courier">Courier</th> 

                    <th width="3%"></th>
                </tr>
                </thead>
                <tbody ng-show="!ctrl.isLoading">
                <tr ng-repeat="u in ctrl.displayEntities">
                 <td><span ng-bind="u.orderNo"></span></td>   
                 <td><span ng-bind="u.orderDate | {{ctrl.DateFormat}}"></span></td>   
                 <td><span ng-bind="u.shippingDate | {{ctrl.DateFormat}}"></span></td>   
                 <td><span ng-bind="u.orderStatus"></span></td>   
                 <td><span ng-bind="u.courier"></span></td>   

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
                                <div class="col-md-3 input-sm">Order No.</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.orderNo"
                                           name="orderNo" ng-change="ctrl.onChange()" maxlength="10"
                                           class="form-control input-sm" placeholder="Enter Order No."/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Order Date</div>
                                <div class="col-md-1">
                                    <div class="input-group" ng-controller="myDatePickerCtrl" style="min-width: 166px;">
                                        <input type="text" ng-model="ctrl.editEntity.orderDate" ng-change="ctrl.onChange()" class="form-control input-sm" uib-datepicker-popup="{{format}}" placeholder="{{format}}" is-open="opened" close-text="Close" />
                                        <span class="input-group-btn"> <button type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button> </span>
                                    </div>
                                    <%--<div my-date-picker ng-change="ctrl.onChange()" ng-model="ctrl.editEntity.orderDate"></div>--%> 
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Shipping Date</div>
                                <div class="col-md-1">
                                    <div class="input-group" ng-controller="myDatePickerCtrl" style="min-width: 166px;">
                                        <input type="text" ng-model="ctrl.editEntity.shippingDate" ng-change="ctrl.onChange()" class="form-control input-sm" uib-datepicker-popup="{{format}}" placeholder="{{format}}" is-open="opened" close-text="Close" />
                                        <span class="input-group-btn"> <button type="button" class="btn btn-default" ng-click="open($event)"><i class="glyphicon glyphicon-calendar"></i></button> </span>
                                    </div>
                                    <%--<div my-date-picker ng-change="ctrl.onChange()" ng-model="ctrl.editEntity.shippingDate"></div>--%> 
                                </div>
                            </div>


                        </div>
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Status</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.orderStatus"
                                           name="orderStatus" ng-change="ctrl.onChange()" maxlength="16"
                                           class="form-control input-sm" placeholder="Enter Status"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Courier</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.courier"
                                           name="courier" ng-change="ctrl.onChange()" maxlength="16"
                                           class="form-control input-sm" placeholder="Enter Courier"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Handler</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.orderHandler"
                                           name="orderHandler" ng-change="ctrl.onChange()" maxlength="40"
                                           class="form-control input-sm" placeholder="Enter Handler"/>                             
                                </div>
                            </div>


                        </div>
                        <div class="row form-inline top10">
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Description</div>
                                <div class="col-md-1">
                                    <input type="text" ng-model="ctrl.editEntity.description"
                                           name="description" ng-change="ctrl.onChange()" maxlength="40"
                                           class="form-control input-sm" placeholder="Enter Description"/>                             
                                </div>
                            </div>
                            <div class="form-group col-md-4">
                                <div class="col-md-3 input-sm">Valid</div>
                                <div class="col-md-1">
                                    <label class="col-md-1 control-lable input-sm"><input type="checkbox" width="20px" ng-change="ctrl.onChange()" ng-model="ctrl.editEntity.valid">Valid</label>
                  
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
                                <%--<button ng-click="ctrl.fillField()" class="btn btn-success custom-width">Fill</button>--%>                    
                                <%--<button ng-click="ctrl.debug()" class="btn btn-success custom-width" title="Debug"> Debug</button>--%>
                            </div>
                        </div>
                        <div>
                            <table class="table table-hover">
                                    <thead>
                                    <tr>
                                        <th width="1%">#</th>
                            
                                            <th nowrap width="25%">Product Name</th>
                            
                                            <th nowrap width="25%">Unit Price</th>
                            
                                            <th nowrap width="25%">Quantity</th>
                            
                                            <th nowrap width="25%">Discount</th>
                            
                                            <th width="5%">
                                                    <button class="btn btn-success custom-width"ng-click="ctrl.addChild('orderdetails')">Add</button>
                                            </th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr ng-repeat="u in ctrl.editEntity.orderdetails" ng-show="ctrl.showChild(u.objectState)">
                                        <td>{{$index + 1}}</td>
                                                        <td>
                                <input ng-model="u.productName" ng-change="ctrl.onChildChange('orderdetails',u.id)" class="form-control input-sm"/>

                            </td>
                            <td>
                                <input ng-model="u.unitPrice" ng-change="ctrl.onChildChange('orderdetails',u.id)" class="form-control input-sm"/>

                            </td>
                            <td>
                                <input ng-model="u.quantity" ng-change="ctrl.onChildChange('orderdetails',u.id)" class="form-control input-sm"/>

                            </td>
                            <td>
                                <input ng-model="u.discount" ng-change="ctrl.onChildChange('orderdetails',u.id)" class="form-control input-sm"/>

                            </td>

                                            <td>
                                                    <button type="button" ng-click="ctrl.removeChild('orderdetails', u.id)"class="btn btn-danger custom-width"> Del</button>
                                            </td>
                                    </tr>
                                    </tbody>
                            </table>
                        </div>                        

                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

</body>
</html>