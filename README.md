Neo4j Extension - Spring
================================

A simple Neo4j extension with Spring integration.

groupId: com.neo4j

artifactId: neo4j-extensions


Parent POM

groupId: org.neo4j

artifactId: neo4j-enterprise


Modules / Artifacts

	neo4j-extensions-spring

Neo4j unmanaged extension with Spring integration.


	Setup
Add the profile in settings.xml to your local maven settings.
Update the path to point to your Neo4j server plugins directory (default on server is NEO4J_HOME/plugins).

	Configuration
In neo4j-server.properties set:

org.neo4j.server.thirdparty_jaxrs_classes=org.neo4j.extensions.java.rest=/extensions-java
org.neo4j.server.thirdparty_jaxrs_classes=org.neo4j.extensions.spring.rest=/extensions-spring


	User Create

curl -v -X POST -H "Accept: application/json" http://localhost:7474/extensions-java/user/create?indexingOn=false

Change the parameter indexingOn=false to turn off indexing.
