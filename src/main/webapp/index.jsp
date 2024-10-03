<%--
  Created by IntelliJ IDEA.
  User: Youcode
  Date: 30/09/2024
  Time: 17:18
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>DevSync - V 1.0.0</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <link href="https://fonts.googleapis.com/css?family=Poppins:600&display=swap" rel="stylesheet">
    <script src="https://kit.fontawesome.com/a81368914c.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>
<body>
<img class="wave" src="img/wave.png">
<div class="container">
    <div class="img">
        <img src="img/bg.svg">
    </div>
    <div class="login-content">
        <form action="login" method="post">
            <img src="img/avatar.svg">
            <h2 class="title">Welcome</h2>

            <!-- Username Input -->
            <div class="input-div one">
                <div class="i">
                    <i class="fas fa-user"></i>
                </div>
                <div class="div">
                    <h5>Username</h5>
                    <input type="text" class="input" name="username" required>
                </div>
            </div>

            <!-- Password Input -->
            <div class="input-div pass">
                <div class="i">
                    <i class="fas fa-lock"></i>
                </div>
                <div class="div">
                    <h5>Password</h5>
                    <input type="password" class="input" name="password" required>
                </div>
            </div>

            <a href="#">Forgot Password?</a>

            <!-- Submit Button -->
            <input type="submit" class="btn" value="Login">
        </form>

        <!-- Error Message Display -->
        <c:if test="${not empty errorMessage}">
            <div style="color: red; margin-top: 10px;">${errorMessage}</div>
        </c:if>
    </div>
</div>
<script type="text/javascript" src="js/main.js"></script>
</body>
</html>
