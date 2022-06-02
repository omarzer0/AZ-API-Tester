# AZ-API-Tester
## App Demo
[Download the app](https://github.com/omarzer0/AZ-API-Tester/releases/download/pre-release/AZ-API-Tester.apk)
 <table align="center">
   <tr>
      <td><video src="https://user-images.githubusercontent.com/55766997/171704634-154403da-5769-4569-9301-acd8ee392f10.mp4"> </td>
   </tr>
 </table>
 
## About: 
A task for Instabug Internship. The idea is simple, create an app to test Rest API's endpoints that support GET and POST requests

## Requirements (✔️ Done):
- Enter URL to connect to
- Add request headers which numbers are dynamic
- App must support GET/POST:
  - For POST request type, App must provide a way to enter request body as string
- After request completion the app needs to display the following:
  - Response code
  - Error if any
  - Request/Response headers
  - Request body or query parameters depending on request type/Response body
- If the device is offline then app must show an error message and do not make the call

## Bouns (✔️ Done):
- Requests alongside their responses should be cached
- App must allow to view cached requests and responses
- App must allow sorting cached requests/responses by their execution time.
- App must allow filtering cached requests/responses by request type.
- Your app must handle configuration changes like screen rotation
- Hint: check [httpbin.org](httpbin.org)

## Extra (✔️ Done):
- Night mode support 🤩🤩
- RTL support

## Important Notes 📝📝:
App must not use any third party libraries. (Retrofit,volley, coroutines, room….etc) considered as 3rd parties (NOT ALLOWED)


## App Keynotes 📝:
- MVI Clean Architecture 🚧🏗️
- Singleton pattern to provide the same instance for Database and Network Helper.
- States to describe screens state, easily observe data changes, handle configuration changes, and separate UI from app logic.
- Actions to easily communicate with view models and Events to deliver events back to UI.
- AZExecutors (a wrapper around Executors) to do background thread work neatly.
- Dynamically add views and save their data on configuration changes.
- Repository to group different data sources and provide a single interface for view models to get the data.
- SQLite to persist data locally (Room is considered a third-party library and not allowed).
- Manually provide objects (Dagger is considered a third party library and not allowed )
- Greate UI with little layout animations
- More to explore and more to be added ... 🤩

<!--  ## App Preview
 <table align="center">
     <tr>
      <td> <img src="https://github.com/omarzer0/AZ-API-Tester/blob/main/assets/0.jpg" height="400" width="200">
      <td> <img src="https://github.com/omarzer0/AZ-API-Tester/blob/main/assets/1.jpg" height="400" width="200">
      <td> <img src="https://github.com/omarzer0/AZ-API-Tester/blob/main/assets/2.jpg" height="400" width="200">
      <td> <img src="https://github.com/omarzer0/AZ-API-Tester/blob/main/assets/3.jpg" height="400" width="200">
     </tr>
     <tr>
      <td> <img src="https://github.com/omarzer0/AZ-API-Tester/blob/main/assets/4.jpg" height="400" width="200">
      <td> <img src="https://github.com/omarzer0/AZ-API-Tester/blob/main/assets/5.jpg" height="400" width="200">
      <td> <img src="https://github.com/omarzer0/AZ-API-Tester/blob/main/assets/6.jpg" height="400" width="200">
      <td> <img src="https://github.com/omarzer0/AZ-API-Tester/blob/main/assets/7.jpg" height="400" width="200">
    </tr>
 </table>
  -->




