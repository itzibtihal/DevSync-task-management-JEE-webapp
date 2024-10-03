<!-- addUser.jsp -->
<%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 02/10/2024
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="org.youcode.DevSync.modals.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add User</title>
</head>
<body>
<h1>Add User</h1>
<form action="cruduser" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username"><br>
    <label for="firstName">First Name:</label>
    <input type="text" id="firstName" name="firstName"><br>
    <label for="lastName">Last Name:</label>
    <input type="text" id="lastName" name="lastName"><br>
    <label for="email">Email:</label>
    <input type="email" id="email" name="email"><br>
    <label for="role">Role:</label>
    <select id="role" name="role">
        <option value="USER">User</option>
        <option value="MANAGER">Manager</option>
        <option value="ADMIN">Admin</option>
    </select><br>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password"><br>
    <button type="submit">Add</button>
</form>
</body>
</html>