package org.neo4j.extensions.spring.rest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.extensions.spring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.ws.rs.core.Response;

/**
 * The User controller test.
 *
 *
 * @author bradnussbaum
 * @version 0.1.0
 *
 * @since 0.1.0
 *
 */
@Configurable
@TransactionConfiguration(defaultRollback = false)
@ContextConfiguration(locations = "classpath*:META-INF/spring/test-springContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCreate() {
        Response response = userController.create(userRepository, false);
        FriendResult result = (FriendResult) response.getEntity();
        Assert.assertNotNull(result);
        Assert.assertTrue(result.getFriends().size() > 0);
    }

}
