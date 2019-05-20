# Exercise 1

Purpose: Gain experience with Spring Data and Spring Data Rest. 



## Description

You wish to extend the simple Library Management System and add the same information added into elasticsearch into postgresql database.  This means that it has been decided to support two databases in parallel. 

Postgresql will not be exposed over web. Only method supported over web will be the post method synch which will synchronize all data from postgresql to elastic search.

From elasticSearch repositories it has been decided to expose over rest all the get methods but hide/disable the database modifying methods and allow modification only via the existing controller.

It is decided to use flyway as a migration tool for JPA database.



