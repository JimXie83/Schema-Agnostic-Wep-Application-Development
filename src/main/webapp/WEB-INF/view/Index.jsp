<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html ng-app="myApp">
<head>

    <link href="<c:url value="/content/css/bootstrap/css/bootstrap.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/content/js/angular-toastr/angular-toastr.css"/>" rel="stylesheet"/>
    <link href="<c:url value="/content/css/app.css"/>" rel="stylesheet"/>
</head>
<body ng-controller="SearchEntityController as ctrl">

<div class="generic-container" style="white-space: nowrap;">
    <form name="searchForm" role="form">
        <div class="panel panel-default">
            <div class="panel-heading"><span class="lead">Schema-Agnostic CRUD Demo</span></div>
            <div class="formcontainer">
                <table>
                    <tr>
                        <td>
                            <a href="/employees/">Employees Information</a>
                        </td>
                    </tr>
                    <tr> <td><br></td> </tr>
                    <tr>
                        <td>
                            <a href="/orders/">Order Management</a>
                        </td>
                    </tr>
                    <tr> <td><br></td> </tr>
                    <tr>
                        <td>
                            <a href="/contacts/">Contact Management</a>
                        </td>
                    </tr>
                    <tr> <td><br></td> </tr>
                    <tr>
                        <td>
                            <a href="/category/">Batch Update Category</a>
                        </td>
                    </tr>
                    <tr> <td><br></td> </tr>
                    <tr>
                        <td>
                            <a href="/contactsBatchEdit/">Batch Update Contacts</a>
                        </td>
                    </tr>
                </table>

            </div>
        </div>
    </form>


</div>

</body>
</html>