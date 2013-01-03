package com.jasonwjones.hyperpipe;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class HyperpipeTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	@Ignore
	public void testMain() {
		String[] args = { "-sqlDriver", "org.h2.Driver", "-sqlUrl", "jdbc:h2:mem:test_mem",
				"-simulateEssbase", "-sqlQuery", "\"SELECT 1\"" };
		Hyperpipe.main(args);
		fail("Not yet implemented");
	}

	@Test
	public void testEssbase() {
		String[] args = { "-sqlDriver", "org.h2.Driver", "-sqlUrl",
				"jdbc:h2:tcp://localhost/~/s4me", "-sqlUser", "sa", "-sqlQuery",
				"\"SELECT * FROM TESTESS\"", "-apsServer", "http://prdashhy03:13080/aps/JAPI",
				"-olapServer", "prdashhy03", "-application", "Sample", "-database", "Basic",
				"-essUser", "hypuser", "-essPass", "hyppass" };
		Hyperpipe.main(args);
	}
}
