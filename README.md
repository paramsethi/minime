minime
======

This webapp can be used via REST API, or UI test page at http://mini.me/main.jsp or http://localhost:8080/main.jsp

Dependecies:
--------------------------------------------------------------------
	Cassandra 1.2.X
	Java 1.6.x+
	Maven
	Tomcat 7.x

To setup the application:
--------------------------------------------------------------------
1) Ensure your Tomcat and Cassandra instances running.
2) Execute the cql script on your cassandra - minime/src/main/cassandra/cql/minime.cql
3) App loads with these default values:

app_url=http://mini.me/
cassandra_seed=localhost
cassandra_cluster_name=Dev Cluster
cassandra_keyspace=minime
connection_pool=MyConnectionPool
cassandra_port=9160
cassandra_version=3.0.0
cql_version=1.2.16

Change these values if required with respect to your environment and bounce the tomcat.

The properties can be change at: <APP_ROOT>/src/main/resources/minime.properties

Step 3: Go to minime project directory and Run pom.xml using "mvn compile package" command. If the cassandra setup is correct, all tests should pass.

Step 4: Copy minime/target/ROOT.war to TOMCAT_ROOT/webapps directory. Open http://localhost:8080/main.jsp for verification. Refer API documentation on how to run API calls.

--------------------------------------------------------------------

MiniMe REST API Documentation

/min
-----
This API takes original url as input parameter and returns the short url.

	API 
		http://mini.me/min
		http://localhost:8080/min
	Parameter 
		origurl : Long URL. If using HTTP Get it's recommended to encode this parameter's value. 

		HTTP Method
			Get 
			Post 
			
For example: http://localhost:8080/min?origurl=http://www.apple.com/iphone/?sid=http://pixar.com
 
Corresponding API response: 
 
	<url>
		<origUrl>http://www.apple.com/iphone/?sid=http://pixar.com</origUrl>
		<shortUrl>http://mini.me/k1qplm</shortUrl>
		<urlHash>k1qplm</urlHash>
	</url>
	
/unmin
-------
This API takes short url as input and returns the original url as response.
	API 
		http://mini.me/unmin
        http://localhost:8080/unmin
	Parameter 
		shorturl : Short URL. The value can be either just the hash or the shortUrl returned by min api call. For example, it can be http://mini.me/foobar or just "foobar".
		HTTP Method
			Get 
			Post 
			
For example: http://localhost:8080/unmin?shorturl=k1qplm
			
API response: 
 
	<url>
		<origUrl>http://www.apple.com/iphone/?sid=http://google.com</origUrl>
		<shortUrl>http://mini.me/k1qplm</shortUrl>
		<urlHash>k1qplm</urlHash>
	</url>
 
/stats
--------
This API call is used to check system health, record size and various other system stats.

API 
	http://mini.me/stats
	http://localhost:8080/stats

	HTTP Method
		Get 
		Post 
 
Sample response: 
 
	<statsEntity> 
		<timeTakenInMillis>1321</timeTakenInMillis>
		<totalUrls>1838265625</totalUrls>
		<freeMemory>56582056</freeMemory>
		<totalMemory>85000192</totalMemory>
	</statsEntity>
 
 
 Error Handling
 ---------------
 In case of validation failures and errors, app will return an error response. See {@link ErrorHandler.java} with error details.

 Sample Error Response: 
 
	<errorEntity> 
		<state>error</state> 
		<errorCode>-4</errorCode> 
		<errorMessage> 
		    URL already shortened. Remove custom alias parameter to see the existing url.
		</errorMessage> 
	</errorEntity>


Algorithms
--------------------------------------------------------------------

Random Hash Shortening Algorithm
  ----------
  The basic strategy is to have a array of allowed characters (char[] allowedHashCharacters) Here it's A-Z, a-z and 0-9 characters
  which is in total 62 characters. Using random function generate 6 different integers between 0-61 range. These are then used as
  indexes of array - to pick random chars (from char[] allowedHashCharacters) and create a 6 digit hash.
   
  For example
  -----------
  If random function returns [0,5,3,29,17,33]. We pick the 0th, 5th, 3rd, etc index values from char[] allowedHashCharacters.
  The corresponding hash will be "aed3p7".
   
  Using 6 hash chars, with each place can be chosen in 62 ways, the total number of possible hashes are 62^6 ~ 56 Billion.
   
  To scale the algorithm more than 56 Billion
  --------------------------------------------
  The functionality can scale either by adding one extra character to hash which will increase the number of urls,
  or by adding more url supported ascii characters.
   
  0 char = 1 URLs
  1 char = 62 URLs
  2 chars = 3,844 URLs
  3 chars = 238,328 URLs
  4 chars = 14,776,336 URLs
  5 chars = 916,132,832 URLs
  6 chars ~ 56,800,235,580 URLs
  7 chars ~ 3,521,614,606,000 URLs
  8 chars ~ 218,340,105,600,000 URLs
  9 chars ~ 13,537,708,655,000,000 URLs
  10 chars ~ 839,299,365,900,000,000 URLs
  11 chars ~ 52,036,560,680,000,000,000 URLs
 
 Sequential Url Shortening Algorithm
  ----------
  The basic strategy is to have a sequential counter and convert the generated number to corresponding base 62 number using A-Z, a-z and 0-9 characters.
  The counter starts from 10,000 to ensure the numbers are not single digit.
 
  For example
  -----------
  If counter = 10,000, the corresponding base 62 number is "cCs".
  If counter = 12324324, the corresponding base 62 value is "QJh1".
 
  To scale the algorithm
  ----------------------
  As the numbers are generated sequentialy, there is no possibility of collisions. The algorithm can scale with no limitation.
  For example: 100,000,000,000'th url will have hash = bLj9OjO
 
  To scale in a distributed application
  --------------------------------------
  In a distributed application, the challenge is to maintain counters thread safe way. The counter READ and INCREMENT has to be one atomic process.
  This can NOT be done via -
  1) JVM in-memory (because multple instances hitting the same counter)
  2) Cassandra 'counter' column, or memcache (because read and increment of counter are not atomic)
 
  Solution:
  Use transaction based solution (mysql/oracle's auto_increment columns) to maintain counter value - ensuring read and increment as atomic.
 