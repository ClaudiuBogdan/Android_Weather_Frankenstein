# About this app:

The main functionality of the app is to display weather conditions based on user location. The app requires access to the Internet and needs location permission. One cool feature the switch between Celsius and Fahrenheit degrees. 

# App architecture

The GUI architecture is Model-View-Controller (MVC). The controller is represented by the main fragment, the view is implemented in the fragment layout and is controlled by the fragment. The model is represented by different classes such as WeatherData.

To offer a good UX, the app saves the network request into the database so it will always display weather data even if there isn't a network connection. It also offers support for portrait, landscape and multi-window mode.
The app has only two external dependencies: OKHTTP for network request and GSON for JSON parser.

## Keywords: Multithreading, Networking, Local Storage, Design Patterns, MVC, OkHTTP

![Celsius](https://i.imgur.com/h2uYHeZ.png)

![Fahrenheit](https://i.imgur.com/0DFWbSQ.png)

