# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2Y0EMIYg1YiVBgA0xgFwwA8mgQBPGADNgSBABXKENcMAALVRgIuRhAtCQAR0CULxgQcPYAc0MIwx8ECAB3GDxDDihgIrR7GFUkNBVSjg4GrNKEBGjwwwAWAAZ+0s4YAFZBmEooaCiAW2AAazVuwwz6GGtvHUDVTwA6TEx0MChPVVwiNHb+vYBOTCzpwOwAYl6AZgAOACZbkBgXsgsuEwDogoYAEooLJIVQnORICBoTC2Y4wAC0AD5yJRrC4ANoABVcZAAKgBdGAAeh2lAAOmgAN4AIlpUDQwFmKGZTmZMGZtWZuHURWgHB5fIF-JQ8wCEv5AF9MMJrBjsWxONwoC4WWyOVz5VKhfpVKKoOLefzBTL-AhDUqNVxKGqcSIVC4QjC4ZQABRQr0WKBQ5JqMAASmVuNELtk8iUKnULhyYAAqgYoD69ZyUBHY4plGoolijDonAAxBocGBpyh5mA6bxZrmYHQhRbV9MwIpICJxdP6wzABBtjjeFAAD1hYA0efjhYxxZVohcNagecjbsM6MXuMoLgUw5QwFHpMW6AAouOVNgCEiN6rt9jURgXAN3gyWVz1MAcjz+eeoGmbU+0oAcOhHbx5CWNBmSVdAOEwWcC3UBdsSXd0YDQQJOnvaNiyQhNVCcEA2wsVdM3TPNc20OcUOLZwFBaDta20XCmnwmjkKIkijwsBRAgiH1gAE8IqMQzjCJdBimP4iJ13Qrdi0dLUXH9KdKChVRsKwZTnUfV08RgXV+2zPkXElQVhIiUkIGgsz+UVe89OLZ8wBcb5Bg-VkTINGBzKtfkrPCGy7L8hzYKOThDkeCBnheaBskMF4yzgc9pDgBQYAAGQgGFkVcl0VT3GAiRJClqQMdRETQLym25S0jWFU0xXtJyoCxXTtWM0DTIawUmrNC1JQddgnXazEFKcQo8p9HK8uDFI4QjBSsQIwsnGTci6uouMuKxZwKxGVc6wbED2WzcTdsIx9JuO7QYGgTDsIQNiUCxSbrCQHxPDgEIOGOJAh1UH07vkCNWyPBZmPOrkHtgQkTUGmBYWRxpoBCcBXpu3dtQASTRwCUHAVdXCgBH1Eva9b2RIr2qxVynAGABGLyv1UH96uZACgJXHzLHhxGxWRqIGhAdGibACL4NemMJPWni5BQWTwiEkSxLWujsWkqtlfkqMVEfTqnDmhpNO0zBOux6g926mHOYC5kgpC9BWtpx8GY8-par5w1LJE52YIauCooeJ5XkeFB0ABIEQQjqO5tirACp3a3gPxaRzyy89SXPSkqUq+okQZILoCQAAvBEkRcAAeJ3bPQTE2o60aVJLqBy8rtAa7r6DG8tib9ZQKbcsT1XrPrtBlsH1a5cTZNlbH4KJ52-NrsxA7K1KNX7tOnv0Eu1fCytqhlyCutHqwnCVoHzcnE+77lYDxexIh9sUw5ETS7LlAEJW96cacO-Nu5cf6UxQDeaqTd6bIGOIzfoLMmTMjZhzCU3NoAuHiMA7+4pg6-2npiDWRF-rcD4iJReAcV60VUPtUsIRZhWEMLrVihDj7LhCGAYINNp7N01HudhnCLYt0oMfG2So3bQLsG5T2H5cHRTDi8P6AJsANChllKcMAADi2YNDJzQgAkqGjs55yyNmYun924V2pt3f2E9G5u0xEbLBndrHj17oI3h41JrIDhFor85Dl5YwIbPIi88yF70ngfKhNDyybzPjvbw4TIl7X-rfOJ8g4ZPSvvgj6lAvqeEfhPZ+2hwZtihkA8xIC8GbhEdqcpEQv6gKvOA6mUDMQM2ZqzQsKDeRoOApgip2CpZRWvoQqaU5fFqB9JQva69SyHSrCY2GWh5BJOuikk+7o1FwmrhMvMdjuEOKEdqLZYAJmqDNggHSRyalOEZPyRZag-z4hZA83G0g-xM2+O8XogoijhB7CgVcA4JRGjBKABYQLerMiNA8gAclC2CMByRiJxu7GBL5pFMnudop5LzsxvI+V8n5-I-kAshQaXkoKEDgvJZzGF2Z4UUuhQqJFsjQ6xVeBwAA7LcfoKAhgvHPO8OAqUABs8BSKGAmTAFE6LmAp0MqVMkxjTEf3qRY5xMBa42N7l5OFULHL2McQMzV2rXENz1Qyg1EV+6TV+rxFAEz-HQVqPqrkU9qlBKuutUJglwnTLXhvEYaTgD1gSTq-eLD1mn23uki+z1Ak5Pbg-CNaAilgxbKU6sarwgNKqRst66y9x1NzRYxpVNIHiLaXKuBCDPxdN-D0wC6C4g5rzUM-NogZ7esTPaxWTq3U5lWUfWZThGILOzEYZhwTWHuledILGPCxobXxQuy2RaurYq5ASlwnzvkwBRanNFkinCYrucyedhL91spinFTwqRCglEBEgYEYB72dGKDAAAUhABomjJ0vDBSABYsrJGFX0USFM5UqQPLMeqjuVitXhMxF5bA1L71QDgBAQoUBfZbpQDu4aTdDkeKcE4xDZql66sQWh4AGGsM4bwxe1drVbWDycD+hoTrwmutXbUWj9HsPQA9QW7th854oDAAvf1w71DRPmVvOS8TFNUcjTO6N7oQ0ZMvi9a+Sa8kFOgum4AJTIbZqwT-RdN88QlrzWAiBd4q3tPgZ078jaubNr6W2stODIqdoNl68TRENGSYHaugNI6g0TubK-KGdYzDMB8LFKKxhHyjLzLC8wZZkscHsy0qN1nlwZayzlvLlaDm0ycMVsA2X4i5aaQ5rhR6JGwIGF7RByD3O9KTJOjgEBlgJcmJOOEHbZPUI00PB5etPWTVxqoCZCgqC2l0NwH0U3tD2AE5QBjwnM1melSjQbwAlsBBW2IP+1m9wTMy1Jk71KwQoDK451FLWXxvlc+zLrnmXAHaiEdu7Z3fCPRiMwMWjHZEjOCU4Z4yzHXZjW9mPM9gQ2bfQ9toTUAItydHdWbAsOVh1GwETL6qBovnYOUbfh7J3FjRuYyQ91hj2wLPTe+Rjw6PRxfbHSo3hEBHlgMAbAaHCCdxlbogyxV06Z2zrnakxh7DEaNkZEaHiJvEW4HgaQAAhKZgTRkgA12uHXWPqGjoN-z7XuvYsyHulQmAZZbSWdS2JqhVXglPeRAVyahCPdWcqz7hrLSnM1vGO1+tbnObdfSIbtoBPBfC5AKLvwAQf6ja9+xqnTXRNLpUpnmnWo6cM+Ea9qRnkmRsqAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
