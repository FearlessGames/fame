<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="top.jsp"/>
<div class="header">
    <div class="padder">
        <form:form action="/resetPassword.html" commandName="mailForm" class="form-main">
            Email: <form:input path="email" class="textfieldWhite"/>
            <input class="button" name="submit" type="submit" value="Reset"/>
            <form:errors path="email" class="error"/>
        </form:form>
    </div>
</div>
<jsp:include page="bottom.jsp"/>