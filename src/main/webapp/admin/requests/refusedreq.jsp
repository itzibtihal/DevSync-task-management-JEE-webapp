<%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 14/10/2024
  Time: 02:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="org.youcode.DevSync.domain.entities.User" %>
<%@ page import="org.youcode.DevSync.domain.entities.Request" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Sharp" rel="stylesheet">
    <link rel="stylesheet" href="css/dash.css">
    <style>
        .tag-input {
            border: 36px solid #ffffff;
            padding: 10px;
            width: calc(100% - 92px);
            box-sizing: border-box;
            border-radius: 38px;
        }

        .add-tag button {
            padding: 10px 20px;
            background-color: #38d39f;
            color: white;
            border: none;
            cursor: pointer;
            border-radius: 38px;
        }

        .add-tag button:hover {
            background-color: #2e9c7a;
        }
    </style>
    <title>Dashboard | DevSync</title>
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
                <span class="material-icons-sharp">close</span>
            </div>
        </div>

        <div class="sidebar">
            <a href="/DevSync/admin">
                <span class="material-icons-sharp">dashboard</span>
                <h3>Dashboard</h3>
            </a>
            <a href="/DevSync/cruduser?action=listAll">
                <span class="material-icons-sharp">person_outline</span>
                <h3>Users</h3>
            </a>
            <a href="/DevSync/TagCrud">
                <span class="material-icons-sharp">business</span>
                <h3>Tags</h3>
            </a>
            <a href="/DevSync/crudtask?action=listAll">
                    <span class="material-icons-sharp">
                         receipt_long
                    </span>
                <h3>Tasks</h3>
            </a>
            <a href="/DevSync/UsersRequest?action=not-accepted" >
                <span class="material-icons-sharp">mail_outline</span>
                <h3>Requests</h3>
            </a>
            <a href="/DevSync/UsersRequest?action=refused" class="active">
                    <span class="material-icons-sharp">
                        report_gmailerrorred
                    </span>
                <h3>Refused Req</h3>
            </a>
            <a href="#">
                <span class="material-icons-sharp">settings</span>
                <h3>Settings</h3>
            </a>
            <a href="/DevSync/logout">
                <span class="material-icons-sharp">logout</span>
                <h3>Logout</h3>
            </a>
        </div>
    </aside>
    <!-- End of Sidebar Section -->

    <main>
        <h1>Refused Requests</h1>

        <div class="recent-orders">
            <h2>All Refused Requests</h2>
            <table>
                <thead>
                <tr>
                    <th>Task Title</th>
                    <th>User Name</th>
                    <th>Token Type</th>
                    <th>Request Created At</th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<Request> refusedRequests = (List<Request>) request.getAttribute("requests");
                    if (refusedRequests != null && !refusedRequests.isEmpty()) {
                        for (Request req : refusedRequests) {
                            User user = req.getUser(); // Assuming Request has a User reference
                %>
                <tr>
                    <td><%= req.getTask().getTitle() %></td>
                    <td><%= user != null ? user.getUsername() : "Unknown User" %></td>
                    <td><%= req.getTokenType() %></td>
                    <td><%= req.getTimestamp() %></td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="4">No refused requests found.</td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>

    </main>

    <!-- Right Section -->
    <div class="right-section">
        <div class="nav">
            <button id="menu-btn">
                <span class="material-icons-sharp">menu</span>
            </button>
            <div class="dark-mode">
                <span class="material-icons-sharp active">light_mode</span>
                <span class="material-icons-sharp">dark_mode</span>
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
                <p>Task management</p>
            </div>
        </div>

        <div class="reminders">
            <div class="header">
                <h2>Reminders</h2>
                <span class="material-icons-sharp">notifications_none</span>
            </div>

            <div class="notification">
                <div class="icon">
                    <span class="material-icons-sharp">volume_up</span>
                </div>
                <div class="content">
                    <div class="info">
                        <h3>Workshop</h3>
                        <small class="text_muted">08:00 AM - 12:00 PM</small>
                    </div>
                    <span class="material-icons-sharp">more_vert</span>
                </div>
            </div>

            <div class="notification deactive">
                <div class="icon">
                    <span class="material-icons-sharp">edit</span>
                </div>
                <div class="content">
                    <div class="info">
                        <h3>Workshop</h3>
                        <small class="text_muted">08:00 AM - 12:00 PM</small>
                    </div>
                    <span class="material-icons-sharp">more_vert</span>
                </div>
            </div>

            <div class="notification add-reminder">
                <div>
                    <span class="material-icons-sharp">add</span>
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
