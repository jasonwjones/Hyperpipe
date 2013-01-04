# Hyperpipe

Hyperpipe is a simple command-line Java program that can be used to fetch data from a SQL database using given credentials, driver, and query and then pipe the data directly into a Hyperion Essbase cube without using a load rule.

This program was born based on a comment from [Cameron Lackpour](http://camerons-blog-for-essbase-hackers.blogspot.com/) regarding a separate one-off project of mine called [Column Transformer](http://www.jasonwjones.com/projects/column-transformer/). In a nutshell, Column Transformer was me playing around with an object-oriented approach to implementing a set of transformations to a columnar row-based data source. In short, it's an attempt to be able to process a data file in the same way that an Essbase load rule does. Cameron's comment was basically "Hey, why not load data directly to Essbase?"

So after a couple days of hacking, Hyperpipe was born. This was all good timing for another reason as I had long had on my list of possible side projects to do a program based around possibly piping data directly into Essbase from the command-line (sans MaxL -- think more along the lines of a Unix command).

Hyperpipe is licensed under the Apache software license — a very business friendly open source license.


## Disclaimer

Hyperpipe comes with absolutely no warranty and I will not be held responsible for damaging your data, server, software, or anything related to these processes. Please use it at your own risk and always test in a development environment. Please let me know of any issues you run into so they can be addressed as time permits.


## Usage

Hyperpipe is a self-contained runnable jar file. At present it is linked against an Essbase Java library from version 9.3.1 of Essbase. It has been linked against an Essbase Java library from 11.1.1.3 and worked too but it has not been extensively tested against all versions and permutations. If it doesn't work for you as-is you may have to link against a different library. 

The following shows a command-line example invocation of Hyperpipe. For the sake of readability, the command has been broken up with one argument per line. The backslash character is used at the end of a line to escape the newline, meaning that while this visually appears as several lines, it is actually one line that you would type in the command shell.

    java -classpath "h2-1.3.153.jar:hyperpipe-1.0.0.jar" \
    com.jasonwjones.hyperpipe.Hyperpipe \
    -sqlDriver org.h2.Driver \
    -sqlUrl jdbc:h2:tcp://localhost/~/testess \
    -sqlUser sa \
    -sqlPass pass \
    -sqlQuery "SELECT * FROM TESTESS" \
    -apsServer http://prdashhy03:13080/aps/JAPI \
    -olapServer prdashhy03 \
    -application Sample \
    -database Basic \
    -essUser hypuser \
    -essPass hyppass

**NOTE:** The above example references a driver that was used for testing but is not provided in this distribution! Replace **h2-1.3.153.jar** above with the name of your own JDBC driver file that you have put in the same folder! For example, your SQL Server file might be called sqljdbc.jar.

All of the parameters listed below are required. In a nutshell, they are as follows:

* **sqlDriver**: This is the Java class name of a JDBC driver class provided by you. If you are connected to SQL Server, for example, then you would put the sqljdbc.jar file in the same directory as Hyperpipe and specify the class name of the driver (you can use Google to figure out what this should be)

* **sqlUrl**: This is the JDBC database URL to your SQL database. In the example this is just a local H2 database but it could be anything that Java supports (which is pretty much anything)

* **sqlUser**: The user name to use to connect to the JDBC data source

* **sqlPass**: The password for the given user

* **sqlQuery**: The SQL query to issue to the given database. Note that you should enclose this in quotes. This query is given directly to the database to execute. At present this query must be very precisely formed. It should represent every single dimension in the target cube.

* **apsServer**: the Analytic Provider Services server URL. As in the above example, this is typically, but not always, your Essbase server on the port 13080 (default) with the suffix /aps/JAPI. You should be able to type this URL into a web browser and get a web page to come up.

* **olapServer**: this is just the server part of the server hosting the Essbase cube you are targeting. It's frequently (but not always) the same server from your APS URL (if your cubes and APS are on the same machine).

* **application**: the Essbase application

* **database**: the Essbase database

* **essUser**: the username to use to connect to the cube

* **essPass**: the password to use to connect to the cube


## How it works

Hyperpipe will use the given SQL/JDBC connection details/credentials to connect to that data source and issue the query you provide. It will then process the results row-by-row and then sends this data to the Essbase cube with the given connection information and credentials. The format of the query should be to specify a member from each dimension and then the fact (numeric value) for that row.

For example, let's say you had a cube with three dimensions, Time, Year, and Scenario. You'd want a query something like `SELECT Period, Year, 'Actual', Sales FROM TEST`. In this case the database table contains values like `Jan`, `Feb`, `Mar` in the Period column and perhaps values like `FY2010` in the Year column. In this case our table doesn't have a scenario for us so we are supplying it manually. Lastly we select Sales which for this example is the fact column, in other words, it has a numeric value.


## Building

Assuming you have all of the dependencies setup correctly on your system (Essbase jar files installed as artifacts) you can run a `mvn clean compile assembly:single` to create the runnable jar.


## Roadmap/Contributing

There is no concrete roadmap for this utility. It was made primarily as a proof of concept and provided to the community in case they find a use for it. If you end up using this, it would be appreciated if you let me know if/how this is useful to you. If you end up enhancing it, please contact me so your changes/additions can be incorporated into the main codebase to help others!


## Other

Please feel free to download Hyperpipe and play with it. The source code is setup as a Maven project. It relies on Essbase Java files that have been imported into a local Maven repository. For simple instructions on how to do this, please email me. It's not hard.

If you would like to hack on the source code for Hyperpipe, please let me know and I will embellish the documentation a bit to make it easier. If you are having some issues or would like to report a bug, please also let me know and I will see what I can do.

