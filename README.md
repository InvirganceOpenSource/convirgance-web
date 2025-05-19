# Convirgance (Web Services)

![Version](https://img.shields.io/badge/Version-pre&dash;release-blue) ![License](https://img.shields.io/badge/License-MIT-green) ![Repository](https://img.shields.io/badge/Platform-Java-gold) <a href="https://central.sonatype.com/artifact/com.invirgance/convirgance-web">![Repository](https://img.shields.io/badge/Repository-Maven_Central-red)</a>

Low-code / No-code solution for developing web applications, allowing capable software engineers to focus only the difficult parts of the system. Brings Convirgance technology to web services using IoC containers for rapid development and deployment.

## Installation

Create a Java EE / Jakarta EE project that supports Servlet 3.0 or higher. Add the following dependency to the project:

```xml
<dependency>
    <groupId>com.invirgance</groupId>
    <artifactId>convirgance-web</artifactId>
    <version>0.1.0</version>
</dependency>
```

Setup the Convirgance (Web Services) servlet to load configured services by adding the following XML to your ```WEB-INF/web.xml```:

```xml
<servlet>
    <servlet-name>ServicesServlet</servlet-name>
    <servlet-class>com.invirgance.convirgance.web.servlet.JakartaServicesServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>ServicesServlet</servlet-name>
    <url-pattern>/services/*</url-pattern>
</servlet-mapping>
```

This will expect your Spring XML files to be placed under ```/services/```. If you only plan to have services in this project, you can change the ```url-pattern``` to ```/``` instead.

## Application Properties

If you want to configure your web application's database with an ```application.properties``` file instead of configuring in the application server's JNDI, add the following XML to your ```WEB-INF/web.xml```:

```xml
<listener>
    <description>ServletContextListener</description>
    <listener-class>com.invirgance.convirgance.web.servlet.ApplicationInitializer</listener-class>
</listener>
```

You can then place a file called ```application.properties``` in your ```src/main/resources``` directory. Here is an example of such a file:

```properties
jdbc.database=H2
jdbc.database.username=sa
jdbc.database.password=
jdbc.database.url=jdbc:h2:mem:clinic;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false
jdbc.database.jndi=jdbc/petclinic
jdbc.init.sql.schema=/sql/init/schema.sql
jdbc.init.sql.data=/sql/init/data.sql
```

The ```schema``` and ```data``` properties are optional. If provided, these files will be pulled from the classpath (i.e. ```src/main/resources```) and executed in the database on startup. This is primarily for demo and self-contained applications.

Try to avoid picking a JNDI name that is already in use. Whatever you set here you will be able to use in your Spring XML configuration files.


## Documentation

- [JavaDocs](https://docs.invirgance.com/javadocs/convirgance-web/) (Work in Progress)
- [TLDDocs](https://docs.invirgance.com/tlddocs/convirgance-web/) (Work in Progress)


## Examples

- [Spring Pet Clinic](https://github.com/InvirganceExampleCode/ConvirgancePetClinic/)
- [Online examples](https://examples.convirgance.com)

## License

Convirgance is available under the MIT License. See [License](LICENSE.md) for more details.

