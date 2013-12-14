Neo4j Extensions
========================

Neo4j unmanaged extensions with regular Java API and Spring integration.

Parent POM
----------

	<groupId>org.neo4j</groupId>
	<artifactId>neo4j-extensions</artifactId>
	<version>0.1.0-SNAPSHOT</version>
	<type>pom</type>


Modules / Artifacts
-------------------

Neo4j unmanaged extension with Java API integration.

	<!-- neo4j-extensions-java -->
	<groupId>org.neo4j</groupId>
	<artifactId>neo4j-extensions-java</artifactId>
	<version>0.1.0-SNAPSHOT</version>
	<type>pom</type>

Neo4j unmanaged extension with Spring integration.

	<!-- neo4j-extensions-spring -->
	<groupId>org.neo4j</groupId>
	<artifactId>neo4j-extensions-spring</artifactId>
	<version>0.1.0-SNAPSHOT</version>
	<type>pom</type>


Setup
-----

Add the profile in settings.xml to your local maven settings.
Update the path to point to your Neo4j server plugins directory (default on server is NEO4J_HOME/plugins).


Configuration
-------------
In neo4j-server.properties set:

	org.neo4j.server.thirdparty_jaxrs_classes=org.neo4j.extensions.java.rest=/extensions-java
	org.neo4j.server.thirdparty_jaxrs_classes=org.neo4j.extensions.spring.rest=/extensions-spring


REST API
--------

Create User with indexing off (default is on).

	curl -v -X POST -H "Accept: application/json" http://localhost:7474/extensions-java/user/create?indexingOn=false

