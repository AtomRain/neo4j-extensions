Neo4j Extensions
========================

Neo4j unmanaged extensions with regular Java API and Spring integration.

Parent POM
----------

	<groupId>org.neo4j</groupId>
	<artifactId>neo4j-extensions</artifactId>
	<version>2.0-SNAPSHOT</version>
	<type>pom</type>


Modules / Artifacts
-------------------

Neo4j unmanaged extension with Java API integration.

	<!-- neo4j-extensions-java -->
	<groupId>org.neo4j</groupId>
	<artifactId>neo4j-extensions-java</artifactId>
	<version>2.0-SNAPSHOT</version>
	<type>pom</type>

Neo4j unmanaged extension with Spring integration.

	<!-- neo4j-extensions-spring -->
	<groupId>org.neo4j</groupId>
	<artifactId>neo4j-extensions-spring</artifactId>
	<version>2.0-SNAPSHOT</version>
	<type>pom</type>


Build & Deploy
--------------

To have maven copy build artifacts to your local Neo4j instance, modify your local maven settings with the profiles provides here:

	https://github.com/AtomRain/neo4j-extensions/blob/master/settings.xml


Update the path to point to your Neo4j home directory:

	<neo4j.home>/PATH/TO/NEO4J_HOME</neo4j.home>


To build and deploy jar artifacts simply run:

	mvn clean package -Pdeploy,neo4j


The process of building will automatically copy dependencies to ${neo4j.home}/system/lib.


REST Configuration
------------------
In neo4j-server.properties set:

	org.neo4j.server.thirdparty_jaxrs_classes=org.neo4j.extensions.java=/extensions-java
	org.neo4j.server.thirdparty_jaxrs_classes=org.neo4j.extensions.spring=/extensions-spring


Test REST Endpoints
----------------------

Check status of java endpoint:

	curl -v -X GET http://localhost:7474/extensions-java/status

Create User with indexing off (default is on):

	curl -v -X POST -H "Accept: application/json" http://localhost:7474/extensions-java/user/create?indexingOn=false


Check status of spring endpoint:

	curl -v -X GET http://localhost:7474/extensions-spring/status

Create User with indexing off (default is on):

	curl -v -X POST -H "Accept: application/json" http://localhost:7474/extensions-spring/user/create?indexingOn=false

