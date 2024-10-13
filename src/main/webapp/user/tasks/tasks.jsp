<%@ page import="org.youcode.DevSync.domain.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page import="org.youcode.DevSync.domain.entities.Task" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Sharp" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.all.min.js"></script>
    <link rel="stylesheet" href="css/dash.css">
    <title> Dashboard | DevSync </title>
</head>

<body>

<div class="container">
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
            <a href="#">
                <span class="material-icons-sharp">dashboard</span>
                <h3>Dashboard</h3>
            </a>

            <a href="/DevSync/UserTasks" class="active">
                <span class="material-icons-sharp">receipt_long</span>
                <h3>Tasks</h3>
                <span class="message-count" style="color: white"><%= request.getAttribute("notStartedCount") %></span>
            </a>

            <a href="/DevSync/MyTask?action=listAll">
                <span class="material-icons-sharp">inventory</span>
                <h3>My Tasks</h3>
            </a>

            <a href="requests?action=listAll">
                <span class="material-icons-sharp">mail_outline</span>
                <h3>Requests</h3>
            </a>

            <a href="requests?action=listRejected">
                <span class="material-icons-sharp">report_gmailerrorred</span>
                <h3>Rejected Req</h3>
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

    <main>
        <h1>Tasks</h1>

        <%
            String successMessage = request.getParameter("success");
            String errorMessage = request.getParameter("error");
        %>

        <% if (successMessage != null) { %>
        <div style="color: green;">
            <strong>Success:</strong> <%= successMessage %>
        </div>
        <% } %>

        <% if (errorMessage != null) { %>
        <div style="color: red;">
            <strong>Error:</strong> <%= errorMessage %>
        </div>
        <% } %>

        <div class="recent-orders">
            <h2>Recent Tasks</h2>
            <table>
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Start Date</th>
                    <th>Due Date</th>
                    <th>Status</th>
                    <th></th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <%
                    List<Task> tasks = (List<Task>) request.getAttribute("tasks");
                    User loggedUser = (User) session.getAttribute("user");
                    if (tasks != null) {
                        for (Task task : tasks) {
                %>
                <tr>
                    <td><%= task.getTitle() %></td>
                    <td><%= task.getStartDate() %></td>
                    <td><%= task.getDueDate() %></td>
                    <td><%= task.getStatus() %></td>
                    <td><a href="/DevSync/UserTasks?action=details&taskId=<%= task.getId() %>">Details</a></td>
                    <td>
                        <% if (loggedUser != null && !task.getCreatedBy().getId().equals(loggedUser.getId())) { %>
                        <a href="#" onclick="showTokenChoice('<%= loggedUser.getId() %>', '<%= task.getId() %>')" style="color: orange">Use Token</a>
                        <% } %>


                    </td>
                  </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>
    </main>

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

        <div class="user-profile">
            <div class="logo">
                <img src="img/Devsynclogo.png" id="logo-image">
                <h2>DevSync</h2>
                <p>task management</p>
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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
    function showTokenChoice(userId, taskId) {
        Swal.fire({
            title: 'Choose Token Type',
            text: "Select a token type to use:",
            showCancelButton: true,
            confirmButtonText: 'Daily Token',
            cancelButtonText: 'Monthly Token',
            focusCancel: true // Focus on the cancel button
        }).then((result) => {
            let tokenType; // Declare tokenType variable
            if (result.isConfirmed) {
                tokenType = 'DAILY'; // Daily Token selected
            } else if (result.isDismissed) {
                tokenType = 'MONTHLY'; // Monthly Token selected
            }

            if (tokenType) {
                // Create a form programmatically to submit
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '/DevSync/UserTasks?action=useToken';

                // Add inputs to the form
                const userIdInput = document.createElement('input');
                userIdInput.type = 'hidden';
                userIdInput.name = 'userId';
                userIdInput.value = userId;
                form.appendChild(userIdInput);

                const taskIdInput = document.createElement('input');
                taskIdInput.type = 'hidden';
                taskIdInput.name = 'taskId';
                taskIdInput.value = taskId;
                form.appendChild(taskIdInput);

                const tokenTypeInput = document.createElement('input');
                tokenTypeInput.type = 'hidden';
                tokenTypeInput.name = 'tokenType';
                tokenTypeInput.value = tokenType; // Use the determined token type
                form.appendChild(tokenTypeInput);

                document.body.appendChild(form); // Append the form to the body
                form.submit(); // Submit the form
            }
        });
    }



</script>
</body>

</html>