<%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 11/10/2024
  Time: 11:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="org.youcode.DevSync.domain.entities.Task" %>
<%@ page import="org.youcode.DevSync.domain.enums.StatusTask" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Task Status</title>
</head>
<body>
<div class="container">
    <h1>Edit Task Status</h1>
    <form action="MyTask" method="post">
        <input type="hidden" name="id" value="<%= ((Task) request.getAttribute("task")).getId() %>"> <!-- Include task ID -->

        <div>
            <label>Status</label>
            <select name="status" required>
                <option value="NOT_STARTED" <%= ((Task) request.getAttribute("task")).getStatus() == StatusTask.NOT_STARTED ? "selected" : "" %>>Not Started</option>
                <option value="IN_PROGRESS" <%= ((Task) request.getAttribute("task")).getStatus() == StatusTask.IN_PROGRESS ? "selected" : "" %>>In Progress</option>
                <option value="COMPLETED" <%= ((Task) request.getAttribute("task")).getStatus() == StatusTask.COMPLETED ? "selected" : "" %>>Completed</option>
                <option value="OVERDUE" <%= ((Task) request.getAttribute("task")).getStatus() == StatusTask.OVERDUE ? "selected" : "" %>>Overdue</option>
            </select>
        </div>

        <div>
            <button type="submit">Update Status</button>
        </div>
    </form>
</div>
</body>
</html>

