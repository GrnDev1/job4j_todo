# To do list
A simple to do list app to manage your tasks

### The main logic of the App:

* Displaying all / completed / new tasks
* When you click on a task, you are taken to a page with a detailed description of the task
* Edit / update / delete task
* If you clicked on the "done" button, the task is moved to the completed state
* The "edit" button takes the user to a separate page for editing
* The "delete" button deletes the task and goes to the list of all tasks


### Used technologies:
* Java 17
* Maven
* PostgreSQL
* Spring Boot
* Lombok
* Junit 5
* AssertJ
* Mockito
* Liquibase
* Hibernate
* H2database
* Thymeleaf
* Bootstrap
* Checkstyle
* JoCoCO

### How to run the project:

- To run the project, you need to clone the project from this repository;
- Then you need to create a local database "todo";
- Specify the login and password for the database you created in the db/liquibase.properties file;
- Run liquibase to pre-create and autofill tables;
- Launch the application through the Main class, located in the folder src\main\java\ru\job4j\todo\Main;
- Open the page http://localhost:8080/index in the browser;

---

## Application screenshots

### Home page

![home_page.png](img/home_page.png)

### All tasks

![all_page.png](img/all_page.png)

### Completed tasks

![completed_page.png](img/completed_page.png)

### New tasks

![new_page.png](img/new_page.png)

### Add task

![create_page.png](img/create_page.png)

### Select new task

![one_page.png](img/one_page.png)

### Select done task

![done_page.png](img/done_page.png)

### Edit task

![edit_page.png](img/edit_page.png)

### Error page
![error_page.png](img/error_page.png)