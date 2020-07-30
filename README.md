# Weather Forecast CLI program

A simple CLI program that gives out weather forecasts for user inputted locations.

## Instruction to run the application

##### Run the program from command line Using Gradle
  - Install [Java](https://www.java.com/en/).
  - Install [Gralde](https://gradle.org/install/).
  - Open the terminal and navigate to the project folder.
  - Execute the command `gradle build`
  - Execute the command `gradle --console plain run`

 
## Instruction to run the tests
  - Install [Java](https://www.java.com/en/).
  - Install [Gralde](https://gradle.org/install/).
  - Open the terminal and navigate to the project folder.
  - Execute the command `gradle build`.
  - Execute the command `gradle test`.

## Notes and limitations
###Notes
  - Task 1 and Task 2 are implemented and works as expected.
  - Task3.2 enables the user to get the weather forecast by giving latitude and longitude, city name, or the default location.
  - The output is a hash map that provides summary for the current weather and also summary of how the weather is going to be throughout the day today.
 
 ###Limitations
 - This version does not provide for the weather forecast for the user's current location.
 - The program does not perform input validation for the given latitude and longitude.
 - More test cases is required to ensure the program functions as expected, for example testing that the latitude and longitude for a given sity name is correct.
  
  
  
 
