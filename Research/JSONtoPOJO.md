 # JSON parsing with GSON and Jackson

Differences:
One difference I found on the serializing Java object to JSON string is that by default Jackson write out the properties whose value is null.
Gson is just the opposite. There is a switch to change the Jackson default as shown in the code.
Source: http://www.doublecloud.org/2015/03/gson-vs-jackson-which-to-use-for-json-in-java/


GSON:
Third-party
Gson is a Java library that can be used to convert Java Objects into their JSON representation. It
can also be used to convert a JSON string to an equivalent Java object. Gson can work with arbitrary Java objects including pre-existing objects that you do not have source-code of.
Source code: https://github.com/google/gson

Jackson:
Third-party

Source code: https://github.com/FasterXML/jackson


JSONObject and JSONArray:
JavaSDK

When you are working with JSON data in Android, you would use JSONArray to parse JSON which starts with the array brackets.
Arrays in JSON are used to organize a collection of related items
Source: https://stackoverflow.com/questions/28221555/how-does-okhttp-get-json-string