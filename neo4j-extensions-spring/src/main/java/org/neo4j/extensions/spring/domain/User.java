package org.neo4j.extensions.spring.domain;

import java.io.IOException;
import java.util.Collection;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * A User has been authenticated and owns and has access to information.
 * 
 * 
 * @author bradnussbaum
 * @version 1.0.0
 * 
 * @since 1.0.0
 * 
 */
@NodeEntity
@JsonAutoDetect
public class User {

    /**
     * The Node ID is volatile.
     */
    @GraphId
    private Long id;

    /**
     * The version always starts at 1.
     */
    private Integer version = 1;

    /**
     * Auto-generated unique identifier.
     */
    @Indexed(indexType = IndexType.UNIQUE, indexName = "uids", numeric = false)
    private String uid;

    @CreatedDate
    private Long createdTime;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    @Indexed(indexType = IndexType.SIMPLE, indexName = "user_exact")
    private Long lastModifiedTime;

    @LastModifiedBy
    private String lastModifiedBy;

    private String type;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "user_fulltext")
    private String username;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "user_fulltext")
    private String email;
    
    private String password;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "user_fulltext")
    private String name;

    private Boolean active;

    private Boolean validated;

    @Indexed(indexType = IndexType.UNIQUE, indexName = "identifiers", numeric = false)
    private String identifier;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
