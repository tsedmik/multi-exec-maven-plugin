# multi-exec-maven-plugin
Maven plugin which is capable to execute a list of commands

## How to use it
Set the following parameters:

*path* - Path from where the commands will be executed
*commands* - List of commands to execute
*archiveFrom* - Locations from where files are archived. If you want to specify more than one location, separate them with ',' comma 
*archiveTo* - Location where archived files will be stored

### Example:
Example configuration:

   <plugin>
     <groupId>org.jboss.tsedmik</groupId>
     <artifactId>multi-exec-maven-plugin</artifactId>
     <configuration>
       <path>.</path>
       <commands>
         <param>mvn --version</param>
         <param>ls $path{/mnt/hudson_jenkins}</param>
       </commands>
       <archiveFrom>
         <param>src/main/java</param>
         <param>src/test/java</param>
       </archiveFrom>
       <archiveTo>target/test-archive</archiveTo>
     </configuration>
   </plugin>

#####Note
Notice *$path{/mnt/hudson_jenkins}* in one command. This notation ensures that file path is correctly represented on different platforms

#####Note
This plugin was made on knowledge gained from:
[Guide to Developing Java Plugins](https://maven.apache.org/guides/plugin/guide-java-plugin-development.html)
[Testing Plugins Strategies](https://maven.apache.org/plugin-developers/plugin-testing.html)