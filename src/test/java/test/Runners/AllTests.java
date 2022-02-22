package test.Runners;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.Users.CreateUserTest;
import tests.Users.ListUsers;
import tests.Users.UpdateUserTest;

@RunWith(Suite.class)
@SuiteClasses({
	CreateUserTest.class,
	UpdateUserTest.class,
	ListUsers.class
})
public class AllTests {

}
