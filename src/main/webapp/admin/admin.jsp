<%@ page import="org.youcode.DevSync.domain.entities.User" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 01/10/2024
  Time: 12:22
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
            <a href="#" class="active">
                    <span class="material-icons-sharp">
                        dashboard
                    </span>
                <h3>Dashboard</h3>
            </a>
            <a href="cruduser?action=listAll">
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
            <a href="Requests.html">
                    <span class="material-icons-sharp">
                        receipt_long
                    </span>
                <h3>Requests</h3>
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
            <a href="#">
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
        <h1>Dashboard</h1>
        <!-- Analyses -->
        <div class="analyse">
            <div class="sales">
                <div class="status">
                    <div class="info">
                        <h3>Active Users</h3>
                        <h1>$65,024</h1>
                    </div>
                    <div class="progresss">
                        <svg>
                            <circle cx="38" cy="38" r="36"></circle>
                        </svg>
                        <div class="percentage">
                            <p>+81%</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="visits">
                <div class="status">
                    <div class="info">
                        <h3>Total Tasks</h3>
                        <h1>24,981</h1>
                    </div>
                    <div class="progresss">
                        <svg>
                            <circle cx="38" cy="38" r="36"></circle>
                        </svg>
                        <div class="percentage">
                            <p>-48%</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="searches">
                <div class="status">
                    <div class="info">
                        <h3>Token Usage</h3>
                        <h1>14,147</h1>
                    </div>
                    <div class="progresss">
                        <svg>
                            <circle cx="38" cy="38" r="36"></circle>
                        </svg>
                        <div class="percentage">
                            <p>+21%</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- End of Analyses -->

        <!-- New Users Section -->
        <div class="new-users">
            <h2>Recent Managers</h2>
            <div class="user-list">
                <%
                    List<User> recentUsers = (List<User>) request.getAttribute("recentUsers");
                    if (recentUsers == null || recentUsers.isEmpty()) {
                %>
                <p>No user found</p>
                <%
                } else {
                    for (User recentUser : recentUsers) {
                %>
                <div class="user">
                    <img src="img/profile.png"> <!-- Add actual image path -->
                    <h2><%= recentUser.getFirstName() %> <%= recentUser.getLastName() %></h2>
                    <p><%= recentUser.getEmail() %></p>
                </div>
                <%
                        }
                    }
                %>
                <div class="user">
                    <img src="img/plus.png">
                    <h2>More</h2>
                    <a href="cruduser?action=add">New Artists</a>
                </div>
            </div>
        </div>


        <!-- End of New Users Section -->

        <!-- Recent Orders Table -->
        <div class="recent-orders">
            <h2>Recent Tasks</h2>
            <table>
                <thead>
                <tr>
                    <th>Task Name</th>
                    <th>User Name</th>
                    <th>Deadline</th>
                    <th>Status</th>
                    <th></th>
                </tr>
                </thead>
                <tbody></tbody>
            </table>
            <a href="#">Show All</a>
        </div>
        <!-- End of Recent Orders -->

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

            <div class="profile">
                <div class="info">
                    <p>Hey, <b>Reza</b></p>
                    <small class="text-muted">Admin</small>
                </div>
                <div class="profile-photo">
                    <img src="img/profile.png">
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
