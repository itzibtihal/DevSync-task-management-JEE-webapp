<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.youcode.DevSync.domain.entities.Task" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.stream.Collectors" %>
<%@ page import="org.youcode.DevSync.domain.entities.Tag" %>
<%@ page import="java.util.List" %>
<%@ page import="org.youcode.DevSync.domain.enums.StatusTask" %>

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
<%
  LocalDateTime now = LocalDateTime.now();
  LocalDateTime duDate = task.getDueDate();
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

    input,
    textarea,
    button {
      padding: 10px 15px; /* Adjusted padding for better size */
      border-radius: 35px !important; /* Ensures rounded corners */
      margin: 5px 0;
      box-sizing: border-box;
      border: 1px solid #ccc;
      font-size: 18px !important;
      font-weight: 300;
      width: 100%; /* Ensures full width */
    }

    input:focus,
    textarea:focus {
      box-shadow: none !important;
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

    button:focus {
      box-shadow: none !important;
      outline-width: 0;
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
        <form class="form-card" action="/DevSync/UserTasks?action=update" method="post" enctype="multipart/form-data">
          <h1><%= task.getTitle() %></h1>
          <div class="row justify-content-between text-left">
            <div class="form-group col-sm-6 flex-column d-flex">
              <label class="form-control-label px-3">Token Used</label>
              <input type="text" name="tokenUsed" value="<%= task.isTokenUsed() %>" readonly>
            </div>



            <div class="form-group col-sm-6 flex-column d-flex">
              <label class="form-control-label px-3">Status</label>
              <input type="text" id="statusInput" name="status" value="<%= task.getStatus() %>" readonly>

              <% if (now.isBefore(duDate)) { %>
              <button type="button" class="btn btn-primary" id="editStatusButton">
                <i class="fa fa-pencil"></i> Edit
              </button>
              <% } else { %>
              <span class="text-muted">Status cannot be modified after the due date.</span>
              <% } %>
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

<!-- Modal for Editing Status -->
<div class="modal fade" id="editStatusModal" tabindex="-1" role="dialog" aria-labelledby="editStatusModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="editStatusModalLabel">Edit Status</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <form id="editStatusForm">
          <div class="form-group">
            <label class="form-control-label px-6" >Status</label>
            <select name="status" required>
              <option value="NOT_STARTED" <%= ((Task) request.getAttribute("task")).getStatus() == StatusTask.NOT_STARTED ? "selected" : "" %>>Not Started</option>
              <option value="IN_PROGRESS" <%= ((Task) request.getAttribute("task")).getStatus() == StatusTask.IN_PROGRESS ? "selected" : "" %>>In Progress</option>
              <option value="COMPLETED" <%= ((Task) request.getAttribute("task")).getStatus() == StatusTask.COMPLETED ? "selected" : "" %>>Completed</option>
              <option value="OVERDUE" <%= ((Task) request.getAttribute("task")).getStatus() == StatusTask.OVERDUE ? "selected" : "" %>>Overdue</option>
            </select>
          </div>
          <input type="hidden" name="taskId" value="<%= ((Task) request.getAttribute("task")).getId() %>">
        </form>


      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" onclick="submitStatusUpdate()">Save changes</button>
      </div>
    </div>
  </div>
</div>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.bundle.min.js"></script>
<script>
  document.addEventListener('DOMContentLoaded', function () {
    const dueDate = new Date("<%= task.getDueDate() %>"); // Get the due date from the server
    const now = new Date();
    const editButton = document.getElementById('editStatusButton');

    if (now > dueDate) {
      editButton.disabled = true; // Disable the button if the due date has passed
      editButton.title = "You cannot modify the status after the due date."; // Optional tooltip
      editButton.classList.add('disabled'); // Optionally add a Bootstrap class to show it's disabled
    } else {
      editButton.addEventListener('click', function () {
        $('#editStatusModal').modal('show'); // Open the modal if the due date is not passed
      });
    }
  });
  function submitStatusUpdate() {
    const form = document.getElementById('editStatusForm');
    const newStatus = form.querySelector('select[name="status"]').value;
    const taskId = form.querySelector('input[name="taskId"]').value;

    // Perform an AJAX request to update the status
    $.ajax({
      url: '/DevSync/UserTasks?action=updateStatus',
      type: 'POST',
      data: {
        status: newStatus,
        taskId: taskId
      },
      success: function(response) {
        // Update the status input field with the new status
        document.getElementById('statusInput').value = newStatus;
        // Close the modal
        $('#editStatusModal').modal('hide');
      },
      error: function(xhr) {
        alert('Failed to update status: ' + xhr.responseText);
      }
    });
  }

</script>
</body>

</html>
