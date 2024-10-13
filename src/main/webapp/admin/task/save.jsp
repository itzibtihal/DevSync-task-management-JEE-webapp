<%@ page import="org.youcode.DevSync.domain.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page import="org.youcode.DevSync.domain.entities.Tag" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DevSync</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.0.3/css/font-awesome.css">
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/habibmhamadi/multi-select-tag@3.1.0/dist/css/multi-select-tag.css">
    <style>
        body {
            color: #000;
            overflow-x: hidden;
            height: 100vh;
            background-image: url("https://wallpapercave.com/wp/wp2351071.jpg");
            background-repeat: no-repeat;
            background-size: cover;
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
            margin: 5px 0;
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
</head>

<body>
<div class="container-fluid px-1 py-5 mx-auto">
    <div class="row d-flex justify-content-center">
        <div class="col-xl-7 col-lg-8 col-md-9 col-11 text-center">
            <div class="card">
                <form id="taskForm" action="crudtask" method="post" class="form-card">
                    <h1><em>Save new task</em></h1>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-12 flex-column d-flex">
                            <label class="form-control-label px-6">Title</label>
                            <input type="text" name="title" required>
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-12 flex-column d-flex">
                            <label class="form-control-label px-6">Description</label>
                            <textarea name="description" cols="10" rows="3" required></textarea>
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Tags</label>
                            <select class="filter-multi-select" multiple id="tags" name="tags" required>
                                <%
                                    List<Tag> tags = (List<Tag>) request.getAttribute("tags");
                                    if (tags != null) {
                                        for (Tag tag : tags) {
                                %>
                                <option value="<%= tag.getId() %>"><%= tag.getName() %></option>
                                <%
                                    }
                                } else {
                                %>
                                <option disabled>No tags available</option>
                                <%
                                    }
                                %>
                            </select>
                        </div>
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Assigned To</label>
                            <select name="assignedUser" class="form-control" required>
                                <option value="">Select an option</option>
                                <%
                                    List<User> users = (List<User>) request.getAttribute("users");
                                    if (users != null) {
                                        for (User user : users) {
                                %>
                                <option value="<%= user.getId() %>"><%= user.getUsername() %></option>
                                <%
                                    }
                                } else {
                                %>
                                <option disabled>No users available</option>
                                <%
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                    <div class="row justify-content-between text-left">
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Starting date</label>
                            <input type="datetime-local" name="startDate" id="starting_date" required>
                        </div>
                        <div class="form-group col-sm-6 flex-column d-flex">
                            <label class="form-control-label px-3">Ending date</label>
                            <input type="datetime-local" name="dueDate" id="ending_date" required>
                        </div>
                    </div>
                    <%
                        User loggedInUser = (User) session.getAttribute("user");
                    %>
                    <input type="hidden" name="createdBy" value="<%= (loggedInUser != null) ? loggedInUser.getId() : "" %>">
                    <div class="row justify-content-center">
                        <div class="form-group col-sm-6">
                            <button type="submit" class="btn-block" style="background-color: #38d39f;">Save</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.bundle.min.js"></script>
<script src="https://cdn.jsdelivr.net/gh/habibmhamadi/multi-select-tag@3.1.0/dist/js/multi-select-tag.js"></script>
<script>
    new MultiSelectTag('tags', {
        rounded: true,
        shadow: true,
        placeholder: 'Search',
        tagColor: {
            textColor: '#327b2c',
            borderColor: '#92e681',
            bgColor: '#38d39f',
        },
        onChange: function(values) {
            console.log(values);
        }
    });

    // Set the minimum date to today + 3 days
    const today = new Date();
    today.setDate(today.getDate() + 3);
    const minDate = today.toISOString().slice(0, 16);
    document.getElementById('starting_date').min = minDate;
    document.getElementById('ending_date').min = minDate;

    document.getElementById('taskForm').addEventListener('submit', function(event) {
        const tags = document.getElementById('tags').selectedOptions;
        if (tags.length < 1) {
            event.preventDefault();
            alert("Please select at least one tag.");
        }


        const startDate = new Date(document.getElementById('starting_date').value);
        const endDate = new Date(document.getElementById('ending_date').value);
        const now = new Date();

        if (startDate < now || endDate < now) {
            event.preventDefault();
            alert("Start and end dates must be in the future.");
        }
    });
</script>
</body>

</html>
