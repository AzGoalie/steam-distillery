# Steam Distillery

Steam Distillery is a sample application designed to experiment with different languages and
frameworks. Each branch of this repository is a different implementation of the application that
meets all the following features.

## Features

* Pull a list of all steam apps from https://api.steampowered.com/ISteamApps/GetAppList/v2/ and
  fetch the details of each app from https://store.steampowered.com/api/appdetails/?appids=${APP_ID}
* Store the details of each app in a database.
* Schedule a task to update the app database every hour. The task should not run if the previous
  task is still processing.
* Expose a GraphQL endpoint to query the database.

## Implementations planned
* Java / Spring-Boot
* JavaScript/Typescript
* Clojure