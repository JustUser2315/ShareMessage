"Share Message" is a Java Spring Boot application that allows users to create and share messages. 
The application features a message board where users can view, edit and delete their own messages, as well as like or dislike messages posted by other users.

The application has two main roles: admin and user. Admins have additional functionalities, such as the ability to delete users and their messages, view user information. 
Admins can also deprive users of their roles and disable their accounts.

To ensure the security of the application, Spring Security has been implemented to provide authentication and authorization for each role. 
In addition, the application uses Google reCAPTCHA to prevent spam and improve security.

To help users activate their accounts after registration, the application uses a mail sending function. 
The application also uses PostgreSQL as the database management system and database changelog from Liquibase to manage database changes.

For the frontend, the application uses Thymeleaf with Bootstrap. 

Project also includes JUnit testing. 
The testing is performed using a Docker container.
