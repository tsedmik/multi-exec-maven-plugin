package org.jboss.tsedmik.multi_exec_maven_plugin;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

public class RunMojoTest extends AbstractMojoTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * 1. test presence of 'run' goal
	 * 2. test execution of 'run' goal
	 * 3. test archivation (multiple location)
	 * 
	 * @throws Exception if something went wrong
	 */
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
		archivedFile = new File (getBasedir(), "target/test-archive/RunMojoTest.java");
		assertNotNull(archivedFile);
		assertTrue(archivedFile.exists());
	}

	/**
	 * 1. test presence of 'run' goal
	 * 2. test execution of 'run' goal
	 * 3. test archivation (single location)
	 * 
	 * @throws Exception if something went wrong
	 */
	public void testMojoGoal2() throws Exception {
		File testPom = new File(getBasedir(), "src/test/resources/test-pom/pom2.xml");
		RunMojo mojo = (RunMojo) lookupMojo("run", testPom);
		assertNotNull(mojo);
		try {
			mojo.execute();
		} catch (Exception e) {
			fail("Something is wrong with test build!");
		}
		File archivedFile = new File (getBasedir(), "target/test-archive2/RunMojo.java");
		assertNotNull(archivedFile);
		assertTrue(archivedFile.exists());
		archivedFile = new File (getBasedir(), "target/test-archive2/RunMojoTest.java");
		assertNotNull(archivedFile);
		assertFalse(archivedFile.exists());
	}
}
