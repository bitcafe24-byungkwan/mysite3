package com.cafe24.mysite.scenario;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.cafe24.mysite.controller.api.GuestbookControllerTest;
import com.cafe24.mysite.controller.api.UserControllerTest;

import junit.framework.Test;
import junit.framework.TestSuite;

@RunWith(Suite.class)
@SuiteClasses({
	UserControllerTest.class,
	GuestbookControllerTest.class,
})
public class MemberOrderScenarioTest {
	public static Test suite() {
		return new TestSuite("User order senario test");
	}
}
