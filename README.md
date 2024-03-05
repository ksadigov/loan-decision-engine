# Loan Decision Engine

## Overview
This project implements a loan decision engine that evaluates loan applications based on the applicant's code, requested loan amount, and requested period, providing a decision and an approved amount.

## Build - Used Technologies

- **Java**: We use version 17 for many parts of the project.
- **Spring Boot**: We use version 3.2.3 for the project's backend.
- **Build Tools**: We use Maven to manage our project's building blocks and keep everything organized.
- **Additional Tools**:
    - Lombok helps us write less code by hand.
    - JUnit5 tests our code to make sure everything works well with Java 17.
    - Thymeleaf is utilized for creating dynamic HTML views.

## Installation & Running the Application

To set up and run the project, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE and ensure that Java 17 is set as the SDK.
3. Use Maven to build the project by running `mvn clean install`.
4. Start the application by running `java -jar target/loan-decision-engine-0.0.1-SNAPSHOT.jar`.
5. Access the application UI through your web browser at http://localhost:8080/loan-application. This address will take you to the loan application page where you can interact with the UI.
