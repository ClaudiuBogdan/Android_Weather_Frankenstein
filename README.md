# About this app:

The main functionality of the app is to display weather conditions based on user location. The app requires acces to the Internet and needs location permission. One cool feature is the switch between Celsius and Fahrenheit degrees. 

#App architecure

The app architecture is Model-View-Controller (MVC). The contoller is represented by the main fragment, the view is implemented in the fragment layout and is controlled by the fragment. The model is represented by the different classes such as WeatherData.

To offer a good UX, the app saves the network request into the database so it will always displays weather data even if there isn't an network connection. It also offer support for portrait, landscape and multi-window mode.
The app has only two external dependencies: OKHTTP for network request and GSON for JSON parse.

Keywords: Multithreading, Networking, Local Storage, Design Patterns, MVC, OkHTTP

[Imgur](https://i.imgur.com/h2uYHeZ.png)
[Imgur](https://i.imgur.com/0DFWbSQ.png)
