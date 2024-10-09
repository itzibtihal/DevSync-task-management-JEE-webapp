<%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 10/10/2024
  Time: 00:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="org.youcode.DevSync.domain.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page import="org.youcode.DevSync.domain.entities.Tag" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DevSync</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .card {
            margin-top: 20px;
            padding: 20px;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="card">
        <form action="crudtask" method="post">
            <h1>Save New Task</h1>
            <div class="form-group">
                <label for="title">Title</label>
                <input type="text" id="title" name="title" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" class="form-control" required></textarea>
            </div>
            <div class="form-group">
                <label for="tags">Tags</label>
                <select id="tags" name="tags" multiple class="form-control">
                    <%
                        List<Tag> tags = (List<Tag>) request.getAttribute("tags");
                        if (tags != null) {
                            for (Tag tag : tags) {
                    %>
                    <option value="<%= tag.getId() %>"><%= tag.getName() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="assignedUser">Assigned To</label>
                <select id="assignedUser" name="assignedUser" class="form-control">
                    <option value="">Select an option</option>
                    <%
                        List<User> users = (List<User>) request.getAttribute("users");
                        if (users != null) {
                            for (User user : users) {
                    %>
                    <option value="<%= user.getId() %>"><%= user.getUsername() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="startDate">Starting Date</label>
                <input type="datetime-local" id="startDate" name="startDate" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="dueDate">Ending Date</label>
                <input type="datetime-local" id="dueDate" name="dueDate" class="form-control" required>
            </div>
            <%
                User loggedInUser = (User) session.getAttribute("user");
            %>
            <input type="hidden" name="createdBy" value="<%= (loggedInUser != null) ? loggedInUser.getId() : "" %>">
            <!--hidden inputs-->
            <button type="submit" class="btn btn-primary">Save</button>
        </form>
    </div>
</div>
</body>
</html>

