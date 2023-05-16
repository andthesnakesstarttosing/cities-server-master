# **Cities List**

A simple application for storing information about cities.

## Requirements:

    Java 19
    Maven 3.8.1

## Running the Application

1. Clone the repository:

    `git clone https://github.com/andthesnakesstarttosing/city-server`
2. Navigate to the project directory:

   `cd city-server`
3. Build the project using Maven:

   `mvn clean install`
4. Run the application using the command::

   `java -jar target/city-server-1.0.0.jar`

## Using the Application

### Adding a City

To update information about an existing city, send a PUT request to the `/api/v1/cities/{id}` endpoint with a JSON body 
containing the updated city information in the following format:

    {
        "name": "City Name",
        "photoUrl": "http://example.com/photo.jpg"
    }

### Retrieving a List of Cities

To retrieve a list of all cities, send a GET request to the http://localhost:8080/api/v1/cities endpoint.

### Swagger API Documentation

You can access Swagger API documentation for this application by navigating to http://localhost:8080/swagger-ui.html in 
your web browser. This documentation provides a detailed overview of all available endpoints and their expected input and output formats.

### Authors

* Artyom Blinov - artsblinov@gmail.com