# GSON research

## How to create a POJO for parsing a json string?

Mapping JSON into POJO using Gson

Answer: Using annotations:

@SerializedName: An annotation that indicates this member should be serialized to JSON with the provided name value as its field name.
Source: https://google.github.io/gson/apidocs/com/google/gson/annotations/SerializedName.html

Example:

public class MyClass {
   @SerializedName("name") String a;
   @SerializedName(value="name1", alternate={"name2", "name3"}) String b;
   String c;

   public MyClass(String a, String b, String c) {
     this.a = a;
     this.b = b;
     this.c = c;
   }
 }

Source: https://stackoverflow.com/questions/27039511/mapping-json-into-pojo-using-gson

Answer 2: fromJson() -> Convert JSON to Java object

Gson gson = new Gson();

	// 1. JSON to Java object, read it from a file.
	Staff staff = gson.fromJson(new FileReader("D:\\file.json"), Staff.class);

	// 2. JSON to Java object, read it from a Json String.
	String jsonInString = "{'name' : 'mkyong'}";
	Staff staff = gson.fromJson(jsonInString, Staff.class);

	// JSON to JsonElement, convert to String later.
	JsonElement json = gson.fromJson(new FileReader("D:\\file.json"), JsonElement.class);
    String result = gson.toJson(json);

How to convert Java object to / from JSON
Source: https://www.mkyong.com/java/how-do-convert-java-object-to-from-json-format-gson-api/