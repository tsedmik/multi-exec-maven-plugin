package org.jboss.tsedmik.multi_exec_maven_plugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;

/**
 * TODO Add JavaDoc
 * 
 * @author tsedmik
 */
@Mojo(name = "run")
public class RunMojo extends AbstractMojo {

	@Parameter
	private File path;

	@Parameter
	private List<String> commands;

	@Parameter
	private File archiveFrom;

	@Parameter
	private File archiveTo;

	public void execute() throws MojoExecutionException, MojoFailureException {

		generateBeforeInfo();
		for (int i = 0; i < commands.size(); i++) {
			generateBeforeCommandInfo(i, commands.get(i));

			// command execution
			Runtime rt = Runtime.getRuntime();
			final Process pr;
			try {
				pr = rt.exec(commands.get(i), null, path);
			} catch (IOException e) {
				throw new MojoExecutionException("IOException", e.getCause());
			}

			printCommandLog(pr);

			try {
				generateAfterCommandInfo(pr.waitFor(), commands.get(i));
			} catch (InterruptedException e) {
				throw new MojoExecutionException("InterruptedException", e.getCause());
			}

			try {
				archiveArtifacts(i);
			} catch (IOException e) {
				throw new MojoExecutionException("IOException", e.getCause());
			}
		}

	}

	private void generateBeforeInfo() {
		getLog().info("Path: " + path.getAbsolutePath());
		getLog().info("Archive From: " + archiveFrom.getAbsolutePath());
		getLog().info("Archive To: " + archiveTo.getAbsolutePath());
		getLog().info("----------");
		getLog().info("");
	}

	private void generateBeforeCommandInfo(int i, String command) {
		getLog().info("");
		getLog().info("----------");
		getLog().info("Started with executing: " + command + " (" + (i + 1) + "/" + commands.size() + ")");
		getLog().info("----------");
		getLog().info("");
	}

	private void printCommandLog(final Process pr) {
		new Thread(new Runnable() {
			public void run() {
				BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
				String line = null;

				try {
					while ((line = input.readLine()) != null)
						System.out.println(line);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	private void generateAfterCommandInfo(int waitFor, String command) {
		getLog().info("");
		getLog().info("----------");
		getLog().info("Execution of '" + command + "' was completed!");
		getLog().info("----------");
		getLog().info("");
	}

	private void archiveArtifacts(int command) throws IOException {
		getLog().info("");
		getLog().info("----------");
		getLog().info("Started archivation");
		getLog().info("----------");
		getLog().info("From: " + archiveFrom.getAbsolutePath());
		getLog().info("To: " + archiveTo.getAbsolutePath());
		FileUtils.copyDirectory(archiveFrom, archiveTo);
		getLog().info("----------");
		getLog().info("Archivation completed!");
		getLog().info("----------");
		getLog().info("");
	}
}
