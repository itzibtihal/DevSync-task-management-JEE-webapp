<%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 04/10/2024
  Time: 12:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="org.youcode.DevSync.domain.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page import="org.youcode.DevSync.domain.entities.Task" %><%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 01/10/2024
  Time: 23:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Sharp" rel="stylesheet">
    <link rel="stylesheet" href="css/dash.css">
    <title> Dashboard | DevSync </title>
</head>

<body>

<div class="container">
    <!-- Sidebar Section -->
    <aside>
        <div class="toggle">
            <div class="logo">
                <img src="img/Devsynclogo.png" id="logo-image">
                <h2>Dev<span style="color: #38d39f;">Sync</span></h2>
            </div>
            <div class="close" id="close-btn">
                    <span class="material-icons-sharp">
                        close
                    </span>
            </div>
        </div>

        <div class="sidebar">
            <a href="/DevSync/admin" >
                    <span class="material-icons-sharp">
                        dashboard
                    </span>
                <h3>Dashboard</h3>
            </a>
            <a href="user-crud?action=list" >
                    <span class="material-icons-sharp">
                        person_outline
                    </span>
                <h3>Users</h3>
            </a>
            <a href="/DevSync/TagCrud" >
                    <span class="material-icons-sharp">
                        business
                    </span>
                <h3>Tags</h3>
            </a>
            <a href="/DevSync/crudtask?action=listAll" class="active">
                    <span class="material-icons-sharp">
                        receipt_long
                    </span>
                <h3>Tasks</h3>
            </a>

            <a href="#">
                    <span class="material-icons-sharp">
                        mail_outline
                    </span>
                <h3>Notif</h3>
                <span class="message-count">27</span>
            </a>
            <a href="projects.html">
                    <span class="material-icons-sharp">
                        inventory
                    </span>
                <h3>Projects</h3>
            </a>
            <a href="pendingproject.html">
                    <span class="material-icons-sharp">
                        report_gmailerrorred
                    </span>
                <h3>Pending Proj</h3>
            </a>
            <a href="#">
                    <span class="material-icons-sharp">
                        settings
                    </span>
                <h3>Settings</h3>
            </a>
            <a href="/DevSync/logout">
                    <span class="material-icons-sharp">
                        logout
                    </span>
                <h3>Logout</h3>
            </a>
        </div>
    </aside>
    <!-- End of Sidebar Section -->

    <!-- Main Content -->

    <main>
        <h1>Tasks</h1>
        <div class="recent-orders">
            <h2>All Tasks</h2>
            <a href="crudtask?action=add">Add new Task</a>
            <table>
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Due date</th>
                    <th>Assigned User</th>
                    <th>Status</th>
                    <th>Details</th>
                    <th>Delete</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<Task> tasks = (List<Task>) request.getAttribute("tasks");
                    if (tasks != null && !tasks.isEmpty()) {
                        for (Task task : tasks) {
                %>
                <tr>
                    <td><%= task.getTitle() %></td>
                    <td><%= task.getDueDate() %></td>
                    <td><%= task.getAssignedUser() != null ? task.getAssignedUser().getUsername() : "Unassigned" %></td>
                    <td><%= task.getStatus() %></td>
                    <td><a href="/DevSync/crudtask?action=get&id=<%= task.getId() %>">Details</a></td>
                    <td><a href="/DevSync/crudtask?action=delete&id=<%= task.getId() %>" onclick="return confirm('Are you sure you want to delete this task?');">Delete</a></td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="7">No tasks found</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

    </main>

    <!-- End of Main Content -->

    <!-- Right Section -->
    <div class="right-section">
        <div class="nav">
            <button id="menu-btn">
                    <span class="material-icons-sharp">
                        menu
                    </span>
            </button>
            <div class="dark-mode">
                    <span class="material-icons-sharp active">
                        light_mode
                    </span>
                <span class="material-icons-sharp">
                        dark_mode
                    </span>
            </div>

            <%
                User loggedInUser = (User) session.getAttribute("user");
            %>

            <div class="profile">
                <div class="info">
                    <p>Hey, <b><%= (loggedInUser != null) ? loggedInUser.getUsername() : "Guest" %></b></p>
                    <small class="text-muted"><%= (loggedInUser != null) ? loggedInUser.getRole().toString() : "Guest" %></small>
                </div>
                <div class="profile-photo">
                    <img src="img/profile.png" alt="Profile Photo">
                </div>
            </div>

        </div>
        <!-- End of Nav -->

        <div class="user-profile">
            <div class="logo">
                <img src="img/Devsynclogo.png" id="logo-image">
                <h2>DevSync</h2>
                <p> task management</p>
            </div>
        </div>

        <div class="reminders">
            <div class="header">
                <h2>Reminders</h2>
                <span class="material-icons-sharp">
                        notifications_none
                    </span>
            </div>

            <div class="notification">
                <div class="icon">
                        <span class="material-icons-sharp">
                            volume_up
                        </span>
                </div>
                <div class="content">
                    <div class="info">
                        <h3>Workshop</h3>
                        <small class="text_muted">
                            08:00 AM - 12:00 PM
                        </small>
                    </div>
                    <span class="material-icons-sharp">
                            more_vert
                        </span>
                </div>
            </div>

            <div class="notification deactive">
                <div class="icon">
                        <span class="material-icons-sharp">
                            edit
                        </span>
                </div>
                <div class="content">
                    <div class="info">
                        <h3>Workshop</h3>
                        <small class="text_muted">
                            08:00 AM - 12:00 PM
                        </small>
                    </div>
                    <span class="material-icons-sharp">
                            more_vert
                        </span>
                </div>
            </div>

            <div class="notification add-reminder">
                <div>
                        <span class="material-icons-sharp">
                            add
                        </span>
                    <h3>Add Reminder</h3>
                </div>
            </div>

        </div>

    </div>


</div>

<script src="orders.js"></script>
<script src="js/index.js"></script>
</body>

</html>


