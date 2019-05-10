# Exercise 1

Purpose: Understand how to setup a spring boot project and how this can be executed. Also understand the power of spring boot starter auto-configuration  and "executable jar" features.



## Description

You wish to create a simple Library Management System where users can add or find details about resources.  A resource can be of media type book, a magazine or DVD. The data that can be saved into the system are the following:

```json
{
   "tracking_id": "trucking id added as a sticker on the media resource",
   "type": "media type (book or magazine or dvd)",
   "name": "media name, i.e. title of book",
   "author": "author of book",
   "abstract": "abstract of media/description",
   "url": "download url"
}
```

You should create a java web application that allows adding into elastic search such information and allowing this information to be accessed using the tracking id or the type of the book or the name of the book.

## Requirements 

You are requested to use Lombok during development as part of the exercise.

> Note: You are not requested to implement any free text functionality or implement the rest methods to actually download the resource.

> Note: The proposed solution uses also Mapstruct to decouple view from domain object. You are advised to also try to use this methodology, but as a second step 