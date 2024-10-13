# DevSync Project

## Project Context
DevSync was born from a collaborative effort to address the shortcomings of existing task management tools. Driven by a commitment to innovation, the development team chose **JAKARTA EE** to build a robust platform. DevSync's features, such as advanced search with tags, scheduling constraints, and automated status updates, were carefully crafted based on user feedback. The goal of the project is to make DevSync a catalyst for improved collaboration and project success, simplifying task management for individuals, team leaders, and managers in dynamic work environments.

---

## Version 1.0.0

### Features:

### Development Environment Setup:
- Evaluate the development environment needs (IDE, application server, database, etc.) for the JEE project. 
- Set up and configure the development environment on developers' machines. 
- Design a robust JEE architecture to meet application needs and establish a well-organized project structure to facilitate code management.

### CRUD Operations for User Class:
- Introduce basic CRUD functionalities for the User class.
  
  **Attributes:**
  - id
  - username
  - password
  - firstName
  - lastName
  - email
  - manager

### Technologies Used:
- Maven
- JAKARTA EE
- Hibernate
- JPA
- Servlets
- JSP
- Tomcat/JBoss/GlassFish

---

## Version 1.1.0

### Features:
- Ensure tasks cannot be created in the past.
- Users must enter multiple tags for tasks.
- Restrict task scheduling to 3 days in advance.
- Enforce that tasks must be completed before their deadline.
- Users can assign additional tasks only to themselves, not to others.
- Users receive 2 tokens per day to replace tasks assigned by their manager and 1 token per month for task deletion.
- Deleting a task created by the same user does not affect tokens.

After implementing these features, deploy the application to your local repository and prepare for the next version.

---

## Version 1.2.0

### Features:
- When a manager replaces a task, they must assign it to another user. This task can no longer be modified or deleted using tokens.
- If a manager does not respond to a task change request within 12 hours, the user will have double tokens the next day.
- Every 24 hours, the application must mark tasks as incomplete if they haven't been completed and are past the deadline.
- Allow managers to view an overview of all tasks assigned to their employees, including a completion percentage filtered by week, month, and year, along with the number of tokens used.

After implementing these new features, deploy your application to your local repository.

---

## Deployment Instructions
1. Set up the required development environment and ensure all necessary dependencies are installed.
2. Build the project using Maven and ensure all configurations are in place for your selected application server (Tomcat, JBoss, or GlassFish).
3. Deploy the application to your local repository for testing and verification.

---

## Contact
For any questions or issues, please contact the DevSync development team at [support@devsync.com](mailto:ibtihalboukhanchouch@gmail.com).
