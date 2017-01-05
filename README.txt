================================================ Purpose                ============================

Show the simplest possible "Hello, World" J2EE web app, and a more realistic webapp that lets a user
edit a single street address.

================================================ Build                  ============================

There is no build, the project is intended to only run locally.

================================================ Run                    ============================

Simply execute the me.bantling.j2ee.basics.run.J2EEBasicsFrameworksJetty class as a plain old Java
application, and it will start up Jetty and wait for you to terminate the process.

The console output will indicate the URLs you use in your browser to access the web apps.

================================================ Layout                 ============================

+===============================================+==================================================+
| Location                                      | Purpose                                          |
+===============================================+==================================================+
| src/main/java                                 | Main code                                        |
|  me.bantling.j2ee.basics.controller.filter    |  Automatic transaction management for servlets   |
|  me.bantling.j2ee.basics.controller.listener  |  Log web apps, Log requests, add servlet filter  |
|  me.bantling.j2ee.basics.controller.servlet   |  Servlets                                        |
|  me.bantling.j2ee.basics.model.entity         |  Entities to persist to database                 |
|  me.bantling.j2ee.basics.model.exception      |  ModelException                                  |
|  me.bantling.j2ee.basics.model.mao.api        |  API for manipulating the model                  |
|  me.bantling.j2ee.basics.model.mao.jdbc       |  Original JDBC code for persistence (not used)   |
|  me.bantling.j2ee.basics.model.mao.jpa        |  JPA code for persistence                        |
|  me.bantling.j2ee.basics.model.transaction    |  Transaction API                                 |
|  me.bantling.j2ee.basics.model.validation     |  Validation API                                  |
|  me.bantling.j2ee.basics.run                  |  Start Jetty and execute webapps                 |
|  me.bantling.j2ee.basics.view.bean            |  Immutable View JavaBeans and Enums              |
|  me.bantling.j2ee.basics.view.form            |  Create JavaBeans from request paramaters        |
| src/main/resources                            | Main resources                                   |
|  META-INF/persistence.xml                     |  JPA initialization file                         |
|  logback.xml                                  |  Logback configuration file                      |
| src/main/webapp                               | Webapp dir                                       |
|  WEB-INF/jsp                                  |  jsp files                                       |
|  WEB-INF/jetty-env.xml                        |  Jetty JNDI configuration                        |
| src/test/java                                 | Testing code                                     |
| src/test/resources                            | Testing resources                                |
+=====================================+============================================================+

================================================ Tests                  ============================

There are no tests
