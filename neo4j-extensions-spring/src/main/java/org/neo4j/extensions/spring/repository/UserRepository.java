package org.neo4j.extensions.spring.repository;

import org.neo4j.extensions.spring.domain.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Defines a complete data access contract for a
 * {@link org.neo4j.extensions.spring.domain.User}.
 *
 * @author bradnussbaum
 * @version 1.0.0
 * @see org.neo4j.extensions.spring.domain.User
 * @since 1.0.0
 */
@Repository
public interface UserRepository extends GraphRepository<User> {

    @Query("START n=node:user_fulltext(name={0}) RETURN n;")
    User findByName(String name);

    @Query("START n=node:user_fulltext(email={email}) RETURN count(n) > 0;")
    boolean hasByEmail(@Param("email") final String email);

    @Query("START n=node:user_fulltext(email={0}) RETURN n;")
    User findByEmail(String email);

    @Query("START n=node:user_fulltext(username={0}) RETURN n;")
    User findByUsername(String username);

    @Query("MATCH (u:User)-[:FRIEND_OF]-(f:User) WHERE u.username={username} RETURN f ")
    User[] findFriends(String username);

}
