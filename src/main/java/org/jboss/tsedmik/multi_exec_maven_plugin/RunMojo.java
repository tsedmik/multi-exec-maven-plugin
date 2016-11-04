/*
 *  multi-exec-maven-plugin - Maven plugin which is capable to execute
 *  a list of commands
 *  Copyright (C) 2016 Tomas Sedmik
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
 * Executes given list of commands sequentially. After each command archive artifacts. 
 * 
 * @author tsedmik
 */
@Mojo(name = "run")
public class RunMojo extends AbstractMojo {

	/**
	 * Path from where the commands will be executed
	 */
	@Parameter
	private File path;

	/**
	 * List of commands to execute
	 */
	@Parameter
	private List<String> commands;

	/**
	 * Location from where files are archived
	 */
	@Parameter
	private File archiveFrom;

	/**
	 * Location where archived files will be stored
	 */
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
