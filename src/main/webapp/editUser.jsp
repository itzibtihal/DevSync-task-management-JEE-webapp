<%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 02/10/2024
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="org.youcode.DevSync.modals.User" %>
<%@ page import="java.util.UUID" %>
<%@ page import="org.youcode.DevSync.modals.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Edit User</title>
</head>
<body>
<h1>Edit User</h1>
<%
  User user = (User) request.getAttribute("user");
%>
<form action="cruduser" method="post">
  <input type="hidden" name="_method" value="put">
  <input type="hidden" name="id" value="<%= user.getId() %>">
  <label for="username">Username:</label>
  <input type="text" id="username" name="username" value="<%= user.getUsername() %>"><br>
  <label for="firstName">First Name:</label>
  <input type="text" id="firstName" name="firstName" value="<%= user.getFirstName() %>"><br>
  <label for="lastName">Last Name:</label>
  <input type="text" id="lastName" name="lastName" value="<%= user.getLastName() %>"><br>
  <label for="email">Email:</label>
  <input type="email" id="email" name="email" value="<%= user.getEmail() %>"><br>
  <label for="role">Role:</label>
  <select id="role" name="role">
    <option value="USER" <%= user.getRole() == Role.USER ? "selected" : "" %>>User</option>
    <option value="MANAGER" <%= user.getRole() == Role.MANAGER ? "selected" : "" %>>Manager</option>
    <option value="ADMIN" <%= user.getRole() == Role.ADMIN ? "selected" : "" %>>Admin</option>
  </select><br>
  <label for="password">Password:</label>
  <input type="password" id="password" name="password" value="<%= user.getPassword() %>"><br>
  <button type="submit">Update</button>
</form>
</body>
</html>
