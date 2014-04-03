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


Simple Build
------------

To build the jar artifacts simply run:

	mvn clean package

You will need to copy the following jars from your build output directory to your Neo4j plugins directory:

	neo4j-extensions-java/target/neo4j-extensions-java-all.jar
	neo4j-extensions-spring/target/neo4j-extensions-spring-all.jar

Advanced Build
--------------

To have maven copy build artifacts to your local Neo4j cluster, modify your local maven settings with the profiles provides here:

	https://github.com/AtomRain/neo4j-extensions/blob/master/settings.xml

Update the path to point to your each Neo4j server plugins directory in your cluster (default on each server is NEO4J_HOME):

	<neo4j.home>/PATH/TO/NEO4J_HOME</neo4j.home>

To build and deploy to your local Neo4j cluster:

	mvn clean package -Pdeploy


REST Configuration
------------------
In neo4j-server.properties set:

	org.neo4j.server.thirdparty_jaxrs_classes=org.neo4j.extensions.java.rest=/extensions-java
	org.neo4j.server.thirdparty_jaxrs_classes=org.neo4j.extensions.spring=/extensions-spring


Check status of java endpoint:

	curl -v -X GET http://localhost:7474/extensions-java/status

Create User with indexing off (default is on):

	curl -v -X POST -H "Accept: application/json" http://localhost:7474/extensions-java/user/create?indexingOn=false


Check status of spring endpoint:

	curl -v -X GET http://localhost:7474/extensions-spring/status

Create User with indexing off (default is on):

	curl -v -X POST -H "Accept: application/json" http://localhost:7474/extensions-spring/user/create?indexingOn=false

