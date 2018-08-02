# SSM整合流程

## 一. maven引入依赖

> Spring、SpringMVC、mybatis、数据库连接池、驱动包、其他

```
<build>
  <resources>
    <resource>
      <directory>src/main/java</directory>
      <includes>
        <include>**/*.xml</include>
      </includes>
    </resource>
  </resources>
</build>

<dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
      <scope>test</scope>
    </dependency>


    <!--引入项目依赖的jar包-->
    <!--SpringMVC、Spring-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>5.0.7.RELEASE</version>
    </dependency>

    <!--Spring JDBC-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>5.0.7.RELEASE</version>
    </dependency>

    <!--Spring面向切面编程-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aspects</artifactId>
      <version>5.0.7.RELEASE</version>
    </dependency>

    <!--mybatis-->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.4.6</version>
    </dependency>

    <!--mybatis和spring 整合-->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>1.3.2</version>
    </dependency>


    <!--数据库连接池、驱动-->
    <dependency>
      <groupId>com.mchange</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.5.2</version>
    </dependency>

    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.11</version>
    </dependency>

    <!--jstl servlet-api junit-->
      <dependency>
          <groupId>javax.servlet</groupId>
          <artifactId>jstl</artifactId>
          <version>1.1.2</version>
      </dependency>

      <dependency>
          <groupId>taglibs</groupId>
          <artifactId>standard</artifactId>
          <version>1.1.2</version>
      </dependency>

<dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.0.1</version>
      <scope>provided</scope>
    </dependency>
    
    <!--MBG-->
    <dependency>
      <groupId>org.mybatis.generator</groupId>
      <artifactId>mybatis-generator-maven-plugin</artifactId>
      <version>1.3.7</version>
    </dependency>

    <!--SpringTest-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
      <version>4.3.18.RELEASE</version>
      <scope>test</scope>
    </dependency>
    
    <dependency>
      <groupId>org.testng</groupId>
      <artifactId>testng</artifactId>
      <version>RELEASE</version>
    </dependency>

    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>RELEASE</version>
    </dependency>

    <!--引入pagehelper分页插件-->
    <dependency>
      <groupId>com.github.pagehelper</groupId>
      <artifactId>pagehelper</artifactId>
      <version>5.1.4</version>
    </dependency>
    <dependency>
      <groupId>org.apache.commons</groupId>
      <artifactId>commons-lang3</artifactId>
      <version>3.7</version>
    </dependency>
    
    <dependency>
      <groupId>org.apache.logging.log4j</groupId>
      <artifactId>log4j-core</artifactId>
      <version>2.11.0</version>
    </dependency>

      <!--jackson-->
      <dependency>
          <groupId>com.fasterxml.jackson.core</groupId>
          <artifactId>jackson-databind</artifactId>
          <version>2.9.6</version>
      </dependency>

      <!--数据校验-->
      <dependency>
          <groupId>org.hibernate</groupId>
          <artifactId>hibernate-validator</artifactId>
          <version>6.0.11.Final</version>
      </dependency>
  </dependencies>
```



## 二. 编写ssm整合的关键配置文件

### 1. 编写SSM整合的关键配置文件

> web.xml、spring的配置文件、springMVC的配置文件、mybatis的配置文件

#### 1） web.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app  xmlns="http://xmlns.jcp.org/xml/ns/javaee"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
          version="3.1">
  <!--<display-name>Archetype Created Web Application</display-name>-->

  <!--1. 启动Spring的容器，项目一启动就会加载的配置文件classpath下的applicationContext.xml-->
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:applicationContext.xml</param-value>
  </context-param>

  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>

  <!--2. springmvc的前端控制器,拦截所有请求-->
  <!--在与web.xml同级的目录下添加spring配置文件-->
  <servlet>
    <servlet-name>dispatcherServlet</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
  </servlet>

  <servlet-mapping>
    <servlet-name>dispatcherServlet</servlet-name>
    <url-pattern>/</url-pattern>
  </servlet-mapping>

  <!--过滤器有先后顺序,字符编码过滤器一定要放在所有过滤器之前-->
  <!--3. 字符编码过滤器-->
  <!--CharacterEncoding把字符编码设置成请求或响应的-->
  <filter>
    <filter-name>CharacterEncodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>utf-8</param-value>
    </init-param>
    <init-param>
      <param-name>forceRequestEncoding</param-name>
      <param-value>true</param-value>
    </init-param>
    <init-param>
      <param-name>forceResponseEncoding</param-name>
      <param-value>true</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>CharacterEncodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>

  <!--4. 使用rest风格的URI, 将页面普通的post请求转为指定的delete或者put请求-->
  <filter>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <filter-class>org.springframework.web.filter.HiddenHttpMethodFilter</filter-class>
  </filter>
  <filter-mapping>
    <filter-name>HiddenHttpMethodFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>
    <filter>
        <filter-name>HttpPutFormContentFilter</filter-name>
        <filter-class>org.springframework.web.filter.HttpPutFormContentFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>HttpPutFormContentFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
</web-app>
```



#### 2） dispatcherServlet-servlet.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!--SpringMVC的配置文件,包含网站转跳逻辑的控制,配置-->
    <context:component-scan base-package="com.qaqa" use-default-filters="false">
        <!--只扫描控制器 Controller-->
        <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>

    <!--配置试图解析器,方便页面返回-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/views"/>
        <property name="suffix" value=".jsp"/>
    </bean>

    <!--两个标准配置-->
    <!--将springmvc不能处理的请求交给tomcat-->
    <mvc:default-servlet-handler/>
    <!--能支持springmvc更高级的一些功能,JSR303校验,快捷的ajax...映射动态请求-->
    <mvc:annotation-driven/>
</beans>
```



#### 3) applicationContext.xml

> 数据源、事务控制、等业务逻辑相关

可先在resources下建立dbconfig.properties

```
jdbc.jdbcUrl=jdbc:mysql://localhost:3306/ssm_crud?serverTimezone=GMT
jdbc.driverClass=com.mysql.cj.jdbc.Driver
jdbc.user=root
jdbc.password=root
```

applicationContext.xml: 

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

    <context:component-scan base-package="com.qaqa">
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
    <!--Spring配置文件,这里主要配置和业务逻辑有关的-->

    <!--数据源,事务控制,xxx-->
    <context:property-placeholder location="classpath:dbconfig.properties"/>

    <bean id="pooledDataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="jdbcUrl" value="${jdbc.jdbcUrl}"></property>
        <property name="driverClass" value="${jdbc.driverClass}"></property>
        <property name="user" value="${jdbc.user}"></property>
        <property name="password" value="${jdbc.password}"></property>
    </bean>

    <!---配置和Mybatis的整合-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!--指定mybatis全局配置文件的位置-->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
        <property name="dataSource" ref="pooledDataSource"/>

        <!--指定mybatis的mapper文件-->
        <property name="mapperLocations" value="classpath*:mapper/*xml"/>
    </bean>

    <!--配置扫描器,将mybatis接口的实现加入到ioc容器中-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!--扫描所有dao接口的实现,加入到ioc容器中-->
        <property name="basePackage" value="com.qaqa.crud.dao"/>
    </bean>

    <!--配置一个可以批量执行的sqlSession-->
    <bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
        <constructor-arg name="sqlSessionFactory" ref="sqlSessionFactory"/>
        <constructor-arg name="executorType" value="BATCH"/>
    </bean>

    <!--事务控制的配置-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!--控制住数据源-->
        <property name="dataSource" ref="pooledDataSource"/>
    </bean>

    <!--开启基于注解的事务,使用xml配置形式的事务(必要主要的都是使用配置式)-->
    <aop:config>
        <!--切入点表达式-->
        <!--..两个点表示service下如果还有子包也可以-->
        <!--切入点表达式表示了哪些方法可能要切入事务, 切入之后该怎么办,就是用事务增强来表示,
            下面配置的表示每一个method都是事务,每一个get开始的方法都是只读的
            但是 和事务管理器是如何产生联系的 ?????
            事务增强的一个属性transanction-manager默认属性就是transactionManager,
            如果 自己起的管理器名不一样,一定要加上这个属性
            -->
        <aop:pointcut id="txPoint" expression="execution(* com.qaqa.crud.service..*())"/>
        <!--配置事务增强-->
        <aop:advisor advice-ref="txAdvice" pointcut-ref="txPoint"/>
    </aop:config>

    <!--配置事务增强,事务如何切入-->
    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <!--所有方法都是事务方法-->
            <tx:method name="*"/>
            <!--以get开始的所有方法-->
            <tx:method name="get*" read-only="true"/>
        </tx:attributes>
    </tx:advice>
    <!--Spring配置文件的核心点(数据源、与mybatis的整合,事务控制)-->
</beans>
```

#### 4）mybatis-config.xml

```
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--驼峰命名规则-->
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>

    <!--类型别名-->
    <typeAliases>
        <package name="com.qaqa.crud.bean"/>
    </typeAliases>

    <plugins>
        <plugin interceptor="com.github.pagehelper.PageInterceptor">
            <!--分页合理化-->
            <property name="reasonable" value="true"/>
        </plugin>
    </plugins>
</configuration>
```



### 2. 数据库

#### 1) 新建表

#### 2) 逆向工程生成

​	在pom.xml同级目录下，新建mbg.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>
    <context id="DB2Tables" targetRuntime="MyBatis3">
        <commentGenerator>
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>
        <!--配置数据库连接-->
        <jdbcConnection driverClass="com.mysql.cj.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/ssm_crud?serverTimezone=GMT"
                        userId="root"
                        password="root">
            <!--设置可以获取tables remarks信息-->
            <property name="useInformationSchema" value="true"/>
            <!--设置可以获取remarks信息-->
            <property name="reamarks" value="true"/>
        </jdbcConnection>

        <!--java类型解析-->
        <javaTypeResolver >
            <property name="forceBigDecimals" value="false" />
        </javaTypeResolver>

        <!--模型生成,指定javaBean生成的位置-->
        <javaModelGenerator
                targetPackage="com.qaqa.crud.bean"
                targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
            <property name="trimStrings" value="true" />
        </javaModelGenerator>

        <!--指定sql映射文件生成的位置-->
        <sqlMapGenerator
                targetPackage="mapper"
                targetProject=".\src\main\resources">
            <property name="enableSubPackages" value="true" />
        </sqlMapGenerator>

        <!--指定dao接口生成的位置,mapper接口-->
        <javaClientGenerator type="XMLMAPPER"
                             targetPackage="com.qaqa.crud.dao"
                             targetProject=".\src\main\java">
            <property name="enableSubPackages" value="true" />
        </javaClientGenerator>

        <!--table指定每个表的生成策略-->
        <table tableName="tb1_emp" domainObjectName="Employee"></table>
        <table tableName="tb1_dept" domainObjectName="Department"></table>
    </context>
</generatorConfiguration>
```





## 引入Bootstrap



```
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:forward page="/xxx"/>
<html>
<head>
    <title>Title</title>
    <%--引入jquery>--%>
    <%--<script type="text/javascript" src="static/js/jquery-3.3.1.min.js"></script>--%>
    <script type="text/javascript" src="http://code.jquery.com/jquery-3.2.1.min.js"></script>
    <%--引入bootstrap样式--%>
    <%--<link href="static/bootstrap-3.3.7-dist/css/bootstrap.min.css"/>--%>
    <%--<script src="static/bootstrap-3.3.7-dist/js/bootstrap.min.js"/>--%>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body><button class="btn btn-success">按钮</button></body>
</html>
```