# multi-exec-maven-plugin
Maven plugin which is capable to execute a list of commands

## How to use it
Set the following parameters:

*path* - Path from where the commands will be executed
*commands* - List of commands to execute
*archiveFrom* - Location from where files are archived
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
         <param>mvn --version</param>
       </commands>
       <archiveFrom>src/main/java</archiveFrom>
       <archiveTo>target/test-archive</archiveTo>
     </configuration>
   </plugin>

#####Note
This plugin was made on knowledge gained from:
[Guide to Developing Java Plugins](https://maven.apache.org/guides/plugin/guide-java-plugin-development.html)
[Testing Plugins Strategies](https://maven.apache.org/plugin-developers/plugin-testing.html)