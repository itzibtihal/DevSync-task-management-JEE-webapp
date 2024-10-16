<%@ page import="java.util.List" %>
<%@ page import="org.youcode.DevSync.domain.entities.User" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.util.Map" %>
<%@ page import="org.youcode.DevSync.domain.entities.Tag" %><%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 14/10/2024
  Time: 20:54
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<h1>User Achievements</h1>
<div>
    <p>Requests Created Today: <%= request.getAttribute("requestsToday") %></p>
    <p>Daily Requests: <%= request.getAttribute("dailyRequests") %></p>
    <p>Monthly Requests: <%= request.getAttribute("monthlyRequests") %></p>
    <p>Total Tokens Used: <%= request.getAttribute("totalTokensUsed") %></p>
</div>
<br>
<table border="1">
    <thead>
    <tr>
        <th>Username</th>
        <th>Total Tasks</th>
        <th>Achievement (%)</th>
    </tr>
    </thead>
    <tbody>
    <%
        List<User> users = (List<User>) request.getAttribute("users");
        Map<UUID, Long> userTaskCounts = (Map<UUID, Long>) request.getAttribute("userTaskCounts");
        Map<UUID, Double> userAchievements = (Map<UUID, Double>) request.getAttribute("userAchievements");

        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                long totalTasks = userTaskCounts.get(user.getId());
                double achievement = userAchievements.get(user.getId());
    %>
    <tr>
        <td><%= user.getUsername() %></td>
        <td><%= totalTasks %></td>
        <td><%= achievement %></td>
    </tr>
    <%
        }
    } else {
    %>
    <tr>
        <td colspan="3">No users found.</td>
    </tr>
    <%
        }
    %>
    </tbody>
</table>

<br> <br>

<form action="usersAchievements" method="post">
    <label for="tags">Select Tags:</label>
    <select id="tags" name="tags" multiple>
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

    <label for="startDate">Start Date:</label>
    <input type="datetime-local" id="startDate" name="startDate" required>

    <label for="endDate">End Date:</label>
    <input type="datetime-local" id="endDate" name="endDate" required>

    <button type="submit">Submit</button>
</form>

<%
    Double completionPercentage = (Double) request.getAttribute("completionPercentage");
    if (completionPercentage != null) {
%>
<h2>Completion Percentage: <%= completionPercentage %> %</h2>
<%
    }
%>
