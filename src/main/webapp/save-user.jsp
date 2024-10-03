<!-- addUser.jsp -->
<%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 02/10/2024
  Time: 15:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="org.youcode.DevSync.modals.User" %>
<%@ page import="org.youcode.DevSync.modals.Role" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit User</title>
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
            border-radius: 30px !important
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
            border-radius: 8px !important;
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
                <h1 class="blue-text">Edit User</h1>
                <%
                    User user = (User) request.getAttribute("user");
                %>
                <form class="form-card" action="cruduser" method="post">
                    <input type="hidden" name="_method" value="put">
                    <input type="hidden" name="id" value="<%= user.getId() %>">
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-12 flex-column d-flex">
                            <label class="form-control-label px-3">Username<span class="text-danger"> *</span></label>
                            <input type="text" id="username" name="username" value="<%= user.getUsername() %>">
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-12 flex-column d-flex">
                            <label class="form-control-label px-3">Password<span class="text-danger"> *</span></label>
                            <input type="password" id="password" name="password" value="<%= user.getPassword() %>">
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-12 flex-column d-flex">
                            <label class="form-control-label px-3">First Name<span class="text-danger"> *</span></label>
                            <input type="text" id="firstName" name="firstName" value="<%= user.getFirstName() %>">
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-12 flex-column d-flex">
                            <label class="form-control-label px-3">Last Name<span class="text-danger"> *</span></label>
                            <input type="text" id="lastName" name="lastName" value="<%= user.getLastName() %>">
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-12 flex-column d-flex">
                            <label class="form-control-label px-3">Email<span class="text-danger"> *</span></label>
                            <input type="email" id="email" name="email" value="<%= user.getEmail() %>">
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-12 flex-column d-flex">
                            <label class="form-control-label px-3">Role<span class="text-danger"> *</span></label>
                            <select id="role" name="role">
                                <option value="USER" <%= user.getRole() == Role.USER ? "selected" : "" %>>User</option>
                                <option value="MANAGER" <%= user.getRole() == Role.MANAGER ? "selected" : "" %>>Manager</option>
                                <option value="ADMIN" <%= user.getRole() == Role.ADMIN ? "selected" : "" %>>Admin</option>
                            </select>
                        </div>
                    </div>
                    <div class="row justify-content-center">
                        <div class="form-group col-sm-6">
                            <button type="submit" class="btn-block" style="background-color: #38d39f;">Update</button>
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