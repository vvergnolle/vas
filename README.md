# Vas

The primary goal is to offer a simple way to keep a list of addresses (with latitude and longitude) and then search for stations around 
the one that you select.

This is a school project which mean **Velib and Autolib Stations** and can be translated just as is in French by **going**.

###Technologies

This projet was built using :

* JBoss Undertow
* Apache Wink
* Opendata paris Web Service (for stations geolocations)

###Design

**Security:**

The security layer of the application is based on the Undertow AuthenticationMechanism class which integrate seemless with Servlet api and so with Jaxrs.

**REST:**

The REST layer is handled by Jaxrs and more precisly by Apache Wink.

**ORM:**

The database layer is handled by OrmLite.

**Domain:**

The database domain object are User and Address.

###Projects

**vas-auth:**

Contains Undertow security class implementation in order to log a user with HTTP Basic digest.
It also contains a Logout servlet that just call the SecurityContext#logout method.

**vas-boot:**

Little bootstrap project which just a main class that is responsible to boot the application.
As the application work standalone, you can pass these parameters (as java parameters: -Dxxx) in order to configure VAS:

vas.conf: A java properties file [*]
vas.fixture.user: A yaml file to populate users at boot time (check *vas-boot/src/main/resources/user.fixture.yaml* file to see the default entries)

**vas-domain-repository**:

Where domain classes and repository lives.

**vas-http-resource**:

Annotation driven HTTP request. (Only GET support) 

###Build & Run

To build the project

> mvn clean package

Then, run the application with

> java -Dvas.conf=vas-boot/vas.conf -jar vas-boot/target/vas-boot.jar


###Login

The login mechanism use HTTP Basic.

###Application urls

/vas/rest/address : Address CRUD
/vas/admin/jaxrs : Apache Wink AdminServlet
/admin/logs : Logback logs

###Database

The application use h2 as the primary database engine. You can override by passing a new configuration file to the command line:

> java -Dvas.conf=<path/to/your/conf> -jar vas-boot/target/vas-boot.jar

*H2 choice was made because of the POC nature of this project*

###vas.conf file

##### Db
vas.db.url=<jdbc url>
vas.db.user=<user>
vas.db.pwd=<password>

##### Admin
vas.admin.logs=true|false
vas.admin.jaxrs=true|false

##### Default stuff
vas.user.logout.default=true|false

###Links

Undertow: http://undertow.io
Apache Wink: https://wink.apache.org
Opendata paris: http://opendata.paris.fr

