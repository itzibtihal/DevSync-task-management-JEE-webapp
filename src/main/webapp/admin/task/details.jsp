<%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 08/10/2024
  Time: 15:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.youcode.DevSync.domain.entities.Task" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="org.youcode.DevSync.domain.entities.Tag" %>
<%@ page import="java.util.List" %>


<%
    Task task = (Task) request.getAttribute("task");
    if (task == null) {
        throw new IllegalStateException("Task not found in request attributes.");
    }

    LocalDateTime startDate = task.getStartDate();
    LocalDateTime dueDate = task.getDueDate();
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
            background-size: 100% 100%
        }

        .card {
            padding: 30px 40px;
            margin-top: 60px;
            margin-bottom: 60px;
            border: none !important;
            box-shadow: 0 6px 12px 0 rgba(0, 0, 0, 0.2);
            border-radius: 35px !important;
        }

        .blue-text {
            color: #38d39f
        }

        .form-control-label {
            margin-bottom: 0
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
            font-weight: 300
        }

        input:focus,
        textarea:focus {
            -moz-box-shadow: none !important;
            -webkit-box-shadow: none !important;
            box-shadow: none !important;
            border: 1px solid #38d39f;
            outline-width: 0;
            font-weight: 400
        }

        .btn-block {
            text-transform: uppercase;
            font-size: 15px !important;
            font-weight: 400;
            height: 43px;
            cursor: pointer
        }

        .btn-block:hover {
            color: #fff !important
        }

        button:focus {
            -moz-box-shadow: none !important;
            -webkit-box-shadow: none !important;
            box-shadow: none !important;
            outline-width: 0
        }
    </style>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.0.3/css/font-awesome.css">

</head>

<body>
<div class="container-fluid px-1 py-5 mx-auto">
    <div class="row d-flex justify-content-center">
        <div class="col-xl-7 col-lg-8 col-md-9 col-11 text-center">
            <div class="card">
                <form class="form-card" action="/DevSync/crudtask?action=update" method="post" enctype="multipart/form-data">
                    <h1><%= task.getTitle() %></h1>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Token Used</label>
                            <input type="text" name="tokenUsed" value="<%= task.isTokenUsed() %>" readonly>
                        </div>
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Status</label>
                            <input type="text" name="status" value="<%= task.getStatus() %>" readonly>
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Created By</label>
                            <input type="text" name="createdBy" value="<%= task.getCreatedBy().getUsername() %>" readonly>
                        </div>
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Assigned To</label>
                            <input type="text" name="assignedTo" value="<%= task.getAssignedUser() != null ? task.getAssignedUser().getUsername() : "Unassigned" %>" readonly>
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Starting date</label>
                            <input type="datetime-local" name="startDate" value="<%= formattedStartDate %>" readonly>
                        </div>
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Ending date</label>
                            <input type="datetime-local" name="endDate" value="<%= formattedDueDate %>" readonly>
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">

                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-12 flex-column d-flex">
                            <label class="form-control-label px-6">Tags</label>
                            <input type="text" name="tags" value="<%= tags %>" readonly>
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-12 flex-column d-flex">
                            <label class="form-control-label px-6">Description</label>
                            <textarea name="description" cols="10" rows="3" readonly><%= task.getDescription() %></textarea>
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="form-group col-sm-6">
                            <button type="button" class="btn-block" style="background-color: #38d39f;" onclick="window.history.back();">Retour</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.bundle.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
</body>

</html>