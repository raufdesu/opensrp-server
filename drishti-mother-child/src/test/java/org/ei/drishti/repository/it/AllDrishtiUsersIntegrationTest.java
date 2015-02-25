package org.ei.drishti.repository.it;

import org.ei.drishti.domain.DrishtiUser;
import org.ei.drishti.repository.AllDrishtiUsers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-applicationContext-drishti.xml")
public class AllDrishtiUsersIntegrationTest {
    @Autowired
    private AllDrishtiUsers users;

    @Before
    public void setUp() throws Exception {
        users.removeAll();
    }

    @Test
    public void shouldFetchUserByUsername() {
        DrishtiUser firstUser = new DrishtiUser("user 1", "password 1", "salt", asList("ROLE_USER"), true);
        DrishtiUser secondUser = new DrishtiUser("user 2", "password 2", "salt", asList("ROLE_USER"), true);
        users.add(firstUser);
        users.add(secondUser);

        DrishtiUser user = users.findByUsername("user 1");
        assertEquals(firstUser, user);

        user = users.findByUsername("user 3");
        assertNull(user);
    }

    @Test
    public void shouldReturnNullWhenUsernameIsNull() {
        DrishtiUser firstUser = new DrishtiUser("user 1", "password 1", "salt", asList("ROLE_USER"), true);
        DrishtiUser secondUser = new DrishtiUser("user 2", "password 2", "salt", asList("ROLE_USER"), true);
        users.add(firstUser);
        users.add(secondUser);

        DrishtiUser user = users.findByUsername(null);

        assertNull(user);
    }
}
