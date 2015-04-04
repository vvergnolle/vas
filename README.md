# Vas

The primary goal is to offer a simple way to keep a list of addresses (with latitude and longitude) and then search for stations around 
the one that you select.

This is a school project that mean **Velib and Autolib Stations** and can be translated just as is in French by **going**.

###Technologies

This projet was built using :

* JBoss Undertow
* Apache Wink
* Opendata paris Web Service (for stations geolocations)

###Design

**Security:**

The security layer of the application is based on the Undertow AuthenticationMechanism class.

**REST:**

The REST layer is handled by Jaxrs (vas-jaxrs) and more precisly by Apache Wink.

**ORM:**

The database layer is handled by OrmLite (vas-domain-repository).

**Domain:**

The database domain object are User and Address.

###Projects

**vas-auth:**

Contains Undertow security class implementation in order to log a user with HTTP Basic digest.
It also contains a Logout servlet that just call the SecurityContext#logout method.

**vas-boot:**

Little bootstrap project which just a main class that is responsible to boot the application.
As the application work standalone, you can pass these parameters (as java parameters: -Dxxx) in order to configure VAS:

> vas.conf: A java properties file [*]

> vas.fixture.user: A yaml file to populate users at boot time

(check *vas-boot/src/main/resources/user.fixture.yaml* file to see the default entries)

**vas-core**:

Define application basics (base services).

**vas-inject**:

Provide dependency injection.

**vas-domain-repository**:

Where domain classes and repository lives.

**vas-jaxrs-address**:

Restful endpoint for addresses.

**vas-jaxrs-stations-around**:

Restful endpoint for stations around an address.

**vas-jaxrs**:

Rest support via JAXRS (Apache Wink).

**vas-exceptions**:

Define behaviors when some exceptions are raised.

**vas-opendata-paris-client**:

Provide interfaces (use vas-http-resource) in order to call the Opendata web services.

**vas-opendata-paris-proxy**:

Define a simple proxy (debug) and a caching system for the Opendata web services responses.

**vas-server**:

Define debug endpoints.

**vas-http-resource**:

Annotation driven HTTP request. (Only GET support) 

*Other projects descriptions coming soon.*

###Build & Run

To build the project

> mvn clean package

Then, run the application with

> java -Dvas.conf=vas-boot/vas.conf -jar vas-boot/target/vas-boot.jar

##Login

The login mechanism use HTTP Basic.

In order to login, just pass a valid HTTP Basic digest to the server at any resources.

##Cors

Since the 0.2-SNAPSHOT version the server is CORS compliant.

You can consume the VAS web services even if you aren't on the same domain.

*Currently the support is very basic, you can't whilelist servers.*

###Debug urls

**/vas/admin/jaxrs** : Apache Wink AdminServlet

*(Enabled only if the vas.admin.jaxrs application property is set to true)*

**/admin/logs** : Display logs

*(Enabled only if the vas.admin.logs application property is set to true)*

##Applications urls

**/vas/rest/address** : Address resource

**/vas/rest/stations/around/{address id}** : Stations around resource

**/vas/rest/users/infos** : Users infos resources (logged user informations)

##Database

The application use h2 as the primary database engine (check vas-boot/vas.conf file). You can override this by passing a new configuration file as a JVM parameter:

> java -Dvas.conf=<path/to/your/conf> -jar vas-boot/target/vas-boot.jar

### Application configuration file (vas.conf)

#### Database

> vas.db.url=jdbc url

> vas.db.user=user

> vas.db.pwd=password

#### Admin

> vas.admin.logs=true|false

> vas.admin.jaxrs=true|false

#### Logout

> vas.user.logout.default=true|false

###Links

**Undertow**: http://undertow.io

**Apache Wink**: https://wink.apache.org

**Opendata paris**: http://opendata.paris.fr
