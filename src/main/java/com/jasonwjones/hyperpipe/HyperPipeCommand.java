package com.jasonwjones.hyperpipe;

import com.beust.jcommander.Parameter;

/**
 * Annotated POJO for JCommander to parse command-line arguments for us.
 * 
 * @author Jason W. Jones
 * 
 */
public class HyperPipeCommand {

	@Parameter(names = "-sqlDriver", description = "Java class name of JDBC driver", required = true)
	public String driverClass;

	@Parameter(names = "-sqlUrl", description = "JDBC URL/DSN for data source", required = true)
	public String sqlUrl;

	@Parameter(names = "-sqlUser", description = "User name for SQL source connection", required = false)
	public String sqlUser;

	@Parameter(names = "-sqlPass", description = "Password for SQL source user", required = false)
	public String sqlPassword;

	@Parameter(names = "-sqlInitQuery", description = "Execute the given command prior to issuing the main query.", required = false)
	public String sqlInitQuery;

	@Parameter(names = "-sqlQuery", description = "SQL query to issue for rows to process", required = true)
	public String sqlQuery;

	@Parameter(names = "-simulateEssbase", description = "Don't load data to Essbase, just show it")
	public boolean simulateEssbaseLoad = false;

	@Parameter(names = "-printRows", description = "Print each row that is processed")
	public boolean printProcessedRows = false;

	@Parameter(names = "-apsServer", description = "Essbase APS server URL (http://server:13080/aps/JAPI)")
	public String apsServer;

	@Parameter(names = "-olapServer", description = "Essbase OLAP server (frequently, but not always the same as just the server from APS)")
	public String olapServer;

	@Parameter(names = "-application", description = "Essbase application name (e.g., Sample)")
	public String application;

	@Parameter(names = "-database", description = "Essbase database name (e.g., Basic)")
	public String database;

	@Parameter(names = "-essUser", description = "Essbase user")
	public String essUser;

	@Parameter(names = "-essPass", description = "Essbase password")
	public String essPass;

}
