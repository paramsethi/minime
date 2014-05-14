minime
======

This webapp can be used via REST API, or UI test page at http://mini.me/testapi.jsp

The dependecies for this application are:
	Cassandra 1.2.X
	Java 1.6.x+
	Maven
	Tomcat 7.x

Before running the web application, follow below mentioned steps:

Prerequisites:
	Tomcat and Cassandra instances running.

-- tell what this block makes up for project

Step 1: Run below cql in your cassandra instance. The supported cql version is 3.0.0 and suported Cassandra version is 1.2.16:

On terminal, first go to cassandra installation root directory and then run 

bin/cqlsh


CREATE KEYSPACE minime WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 }; 
USE minime; 
CREATE TABLE url_mapping (url_hash text PRIMARY KEY, orig_url text); 
CREATE INDEX ON url_mapping (orig_url); 

Step 2: Update <APP_ROOT>/src/main/resources/minime.properties file with correct Cassandra parameters
The default values are:
app_url=http://mini.me/
cassandra_seed=localhost
cassandra_cluster_name=Dev Cluster
cassandra_keyspace=minime
connection_pool=MyConnectionPool
cassandra_port=9160
cassandra_version=3.0.0
cql_version=1.2.16

Step 3: Go to minime project directory and Run pom.xml using "mvn compile package" command. If the cassandra setup is correct, all tests should pass.

Step 4: Copy minime/target/ROOT.war to TOMCAT_ROOT/webapps directory. Open http://localhost:8080/testapi.jsp for verification. Refer API documentation on how to run API calls.

--------------------------------------------------------------------

MiniMe API Documentation 

Min - This API takes origurl as input parameter and returns the mini url for the same. 

	API 
		http://mini.me/min
		http://localhost:8080/min
	Parameter 
		origurl : Long URL. If using HTTP Get it's recommended to encode this parameter's value. 
		customalias : Custom alias to which long url should be shortened. (Optional). If customalias is already created for another url, the api response will return error.
		
		format : Response format. (optional - by default "xml") Currently supported format is XML. 
		HTTP Method 
			Get 
			Post 
			
For example: http://localhost:8080/min?origurl=http://www.apple.com/iphone/?sid=http://google.com
 
Corresponding API response: 
 
	<url>
		<status>success</status>
		<origUrl>http://www.apple.com/iphone/?sid=http://google.com</origUrl>
		<shortUrl>http://mini.me/k1qplm</shortUrl>
		<urlHash>k1qplm</urlHash>
	</url>
	
Unmin - This API takes shorturl as input and returns the original url as response. 
	API 
		http://mini.me/unmin 
	Parameter 
		shorturl : Short URL. The value can be either just the hash or the shortUrl returned by min api call. For example, it can be http://mini.me/aaaaa or just "aaaaa". 
		format : Response format. (optional - by default "xml") Currently supported format is XML. 
		HTTP Method 
			Get 
			Post 
			
For example: http://localhost:8080/unmin?shorturl=k1qplm
			
API response: 
 
	<url>
		<status>success</status>
		<origUrl>http://www.apple.com/iphone/?sid=http://google.com</origUrl>
		<shortUrl>http://mini.me/k1qplm</shortUrl>
		<urlHash>k1qplm</urlHash>
	</url>
 
Stats 

This API call is used to check system health and various system stats. Also, it generates 5,000,000 different hashes and returns time taken for the same.

API 
	http://mini.me/stats 
Parameter 
	format : Response format. (optional - by default "xml") Currently supported format is XML. 
	HTTP Method 
		Get 
		Post 
 
Sample response: 
 
	<statsEntity> 
		<freeMemory>56582056</freeMemory> 
		<startTime>1400040513089</startTime> 
		<testUrlRan>5000000</testUrlRan> 
		<timeTakenInMS>1321</timeTakenInMS> 
		<totalMemory>85000192</totalMemory> 
		<totalUrls>1838265625</totalUrls> 
		<urlNum>5000000</urlNum> 
	</statsEntity> 
 
 
 Error Handling
 
 Sample Error Response: 
 
	<errorEntity> 
		<state>error</state> 
		<errorCode>-4</errorCode> 
		<errorMessage> 
		URL already shortened. Remove custom alias parameter to see the existing url. 
		</errorMessage> 
	</errorEntity> 
 
 
Error Types: 
 
errorCode: -1 
errorMessage: "An application error has occurred."; 
 
errorCode: -2 
errorMessage: "URL does not exist."; 
 
errorCode: -3 
errorMessage:  "Custom alias already exists. Please choose different."; -- not choose.. Please provide? a different alias
 
errorCode: -4 
errorMessage: "URL already shortened. Remove custom alias parameter to see the existing url.";
 
errorCode: -5 
errorMessage:  "Missing required parameter. The expected format of API, for shortening: http://mini.me/min/<LONG_URL> and For getting original: http://mini.me/unmin/http://mini.me/<url_hash>."; 
 