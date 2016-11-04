package org.jboss.tsedmik.multi_exec_maven_plugin;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class RunMojoTest extends AbstractMojoTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testMojoGoal() throws Exception {
		File testPom = new File(getBasedir(), "src/test/resources/test-pom/pom.xml");
		RunMojo mojo = (RunMojo) lookupMojo("run", testPom);
		assertNotNull(mojo);
		try {
			mojo.execute();
		} catch (Exception e) {
			fail("Something is wrong with test build!");
		}
		File archivedFile = new File (getBasedir(), "target/test-archive/RunMojo.java");
		assertNotNull(archivedFile);
		assertTrue(archivedFile.exists());
	}
}
