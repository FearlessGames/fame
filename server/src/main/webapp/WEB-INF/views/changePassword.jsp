<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="top.jsp"/>
<div class="header">
    <p id="headerText">SPACED</p>
</div>
<div class="space20"></div>
<div id="createAccountBody">
    <div class="header2">CHANGE PASSWORD</div>
    <div id="createAccountForm">
        <form:form action="/changePassword.html" commandName="changePasswordForm" class="form-main">
            <table>

                <tr>
                    <td>Password:</td>
                    <td><form:password path="password" class="textfield"/></td>
                </tr>
                <form:errors path="password" class="error"/>
                <tr>
                    <td>Password:</td>
                    <td><form:password path="repeatedPassword" class="textfield"/></td>
                </tr>
                <tr>
                    <td><input class="button" name="submit" type="submit" value="Reset"/></td>
                    <td></td>
                </tr>
                <form:hidden path="token"/>
            </table>
        </form:form>
    </div>
</div>
<jsp:include page="bottom.jsp"/>