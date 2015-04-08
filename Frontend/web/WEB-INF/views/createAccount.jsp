<%@ page import="se.fearless.spacedweb.services.CaptchaService" %>
<%@ page import="se.fearless.spacedweb.services.ReCaptchaService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="top.jsp"/>
<div class="header">
    <p id="headerText">REGISTER TO SPACE ALPHA</p>
</div>
<div class="space20"></div>
<div id="createAccountBody">
    <div class="header2">CREATE ACCOUNT</div>
    <div id="createAccountForm">
        <form:form action="/createAccount.html" commandName="userAccountForm" class="form-main">
            <table>
                <tr>
                    <td>Username:</td>
                    <td><form:input path="username" class="textfield"/></td>
                </tr>
                <form:errors path="username" class="error"/>
                <tr>
                    <td>Password:</td>
                    <td><form:password path="password" class="textfield"/></td>
                </tr>
                <form:errors path="password" class="error"/>
                <tr>
                    <td>Password:</td>
                    <td><form:password path="repeatedPassword" class="textfield"/></td>
                </tr>
                <form:errors path="repeatedPassword" class="error"/>
                <tr>
                    <td>Email:</td>
                    <td><form:input path="email" class="textfield"/></td>
                </tr>
                <form:errors path="email" class="error"/>

                <tr>
                    <td>Human?</td>
                    <td><%
                        CaptchaService captchaService = new ReCaptchaService();
                        out.print(captchaService.renderToHtml());
                    %></td>
                </tr>

                <tr>
                    <td><input class="button" name="submit" type="submit" value="Register"/></td>
                    <td></td>
                </tr>
            </table>
        </form:form>
    </div>
</div>
<jsp:include page="bottom.jsp"/>