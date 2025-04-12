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
