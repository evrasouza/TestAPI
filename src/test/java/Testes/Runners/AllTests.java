package Testes.Runners;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import tests.Users.CreateUserTest;
import tests.Users.UpdateUserTest;

@RunWith(Suite.class)
@SuiteClasses({
	CreateUserTest.class,
	UpdateUserTest.class
})
public class AllTests {

}
