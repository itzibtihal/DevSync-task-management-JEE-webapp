<%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 13/10/2024
  Time: 23:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.youcode.DevSync.domain.entities.Request" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="org.youcode.DevSync.domain.entities.Tag" %>
<%@ page import="java.util.List" %>
<%@ page import="org.youcode.DevSync.domain.entities.User" %>

<%
    Request requestDetails = (Request) request.getAttribute("requestDetails");
    if (requestDetails == null) {
        throw new IllegalStateException("Request not found in request attributes.");
    }

    LocalDateTime createdAt = requestDetails.getTimestamp();
    LocalDateTime startDate = requestDetails.getTask().getStartDate();
    LocalDateTime dueDate = requestDetails.getTask().getDueDate();

    String formattedCreatedAt = createdAt != null ? createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
    String formattedStartDate = startDate != null ? startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
    String formattedDueDate = dueDate != null ? dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";

    List<Tag> tagsList = (List<Tag>) request.getAttribute("tags");
    String tags = tagsList != null ? tagsList.stream().map(Tag::getName).collect(Collectors.joining(", ")) : "";
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DevSync</title>
    <style>
        body {
            color: #000;
            overflow-x: hidden;
            height: 100vh;
            background-image: url("https://wallpapercave.com/wp/wp2351071.jpg");
            background-repeat: no-repeat;
            background-size: 100% 100%;
        }

        .card {
            padding: 30px 40px;
            margin-top: 60px;
            margin-bottom: 60px;
            border: none !important;
            box-shadow: 0 6px 12px 0 rgba(0, 0, 0, 0.2);
            border-radius: 35px !important;
        }

        .form-control-label {
            margin-bottom: 0;
        }

        input,
        textarea,
        button {
            padding: 8px 15px;
            border-radius: 35px !important;
            margin: 5px 0px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            font-size: 18px !important;
            font-weight: 300;
        }

        input:focus,
        textarea:focus {
            border: 1px solid #38d39f;
            outline-width: 0;
            font-weight: 400;
        }

        .btn-block {
            text-transform: uppercase;
            font-size: 15px !important;
            font-weight: 400;
            height: 43px;
            cursor: pointer;
        }

        .btn-block:hover {
            color: #fff !important;
        }
    </style>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
</head>

<body>
<div class="container-fluid px-1 py-5 mx-auto">
    <div class="row d-flex justify-content-center">
        <div class="col-xl-7 col-lg-8 col-md-9 col-11 text-center">
            <div class="card">
                <form class="form-card" action="/DevSync/crudtask?action=update" method="post" enctype="multipart/form-data">
                    <h1><%= requestDetails.getTask().getTitle() %></h1>
                    <p>the request is : <%= requestDetails.getStatus() %></p>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">User Name</label>
                            <input type="text" name="tokenUsed" value="<%= requestDetails.getUser().getUsername() %>" readonly>
                        </div>
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Token Type</label>
                            <input type="text" name="status" value="<%= requestDetails.getTokenType() %>" readonly>
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Created At</label>
                            <input type="text" name="createdAt" value="<%= formattedCreatedAt %>" readonly>
                        </div>
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Status</label>
                            <input type="text" name="assignedTo" value="<%= requestDetails.getTask().getStatus() %>" readonly>
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Starting Date</label>
                            <input type="datetime-local" name="startDate" value="<%= formattedStartDate %>" readonly>
                        </div>
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Ending Date</label>
                            <input type="datetime-local" name="endDate" value="<%= formattedDueDate %>" readonly>
                        </div>
                    </div>

                    <div class="row justify-content-between text-left">
                        <div class="form-group col-12 flex-column d-flex">
                            <label class="form-control-label px-6">Description</label>
                            <textarea name="description" cols="10" rows="3" readonly><%= requestDetails.getTask().getDescription() %></textarea>
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="form-group col-sm-6">
                            <button type="button" class="btn-block" style="background-color: #38d39f;" onclick="window.history.back();">Back to List Request</button>
                        </div>

                            <% if (!"ACCEPTED".equals(requestDetails.getStatus().toString())) { %>
                            <div class="form-group col-sm-6"><button type="button" class="btn-block" style="background-color: orange;" data-toggle="modal" data-target="#respondModal">Responding</button>

                        </div>
                        <% } %>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<!-- Modal -->
<div class="modal fade" id="respondModal" tabindex="-1" role="dialog" aria-labelledby="respondModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="respondModalLabel">Response</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            <div class="modal-body">
                <b><p>Task Title: <%= requestDetails.getTask().getTitle() %></p></b>
                <p>Current Request Status: <%= requestDetails.getStatus() %></p>

                <form action="/DevSync/UsersRequest?action=updateStatus" method="post">
                    <div class="form-group">
                        <label for="newStatus">Update Status</label>
                        <select name="status" id="newStatus" class="form-control" onchange="toggleUserSelection()">
                            <option value="IN_PROGRESS">In Progress</option>
                            <option value="ACCEPTED">Accept</option>
                            <option value="REFUSED">Refuse</option>
                        </select>
                    </div>

                    <div class="form-group" id="userSelection" style="display: none;">
                        <label for="assignedUser">Select New Assigned User</label>
                        <select name="assignedUser" id="assignedUser" class="form-control">
                            <%
                                List<User> users = (List<User>) request.getAttribute("users");
                                for (User user : users) {
                            %>
                            <option value="<%= user.getId() %>"><%= user.getUsername() %></option>
                            <%
                                }
                            %>
                        </select>
                    </div>

                    <input type="hidden" name="id" value="<%= requestDetails.getId() %>"> <!-- Request ID for updating status -->

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                        <button type="submit" class="btn btn-primary">Update Status</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    function toggleUserSelection() {
        const statusSelect = document.getElementById('newStatus');
        const userSelectionDiv = document.getElementById('userSelection');

        // Show or hide the user selection based on the selected status
        if (statusSelect.value === 'ACCEPTED') {
            userSelectionDiv.style.display = 'block';
        } else {
            userSelectionDiv.style.display = 'none';
        }
    }
</script>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>


</html>
