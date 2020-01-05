



# Spring boot

##  一、Spring Boot入门

### 1、主程序类，主入口类

@SpringBootApplication：标注在某个类上，就是SpringBoot的主程序类，SpringBoot就会运行该类的main方法来启动SpringBoot应用

```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {@Filter(
    type = FilterType.CUSTOM,
    classes = {TypeExcludeFilter.class}
), @Filter(
    type = FilterType.CUSTOM,
    classes = {AutoConfigurationExcludeFilter.class}
)}
)
public @interface SpringBootApplication {
```

**@SpringBootConfiguration**：Spring Boot的配置类

​				标注在某个类上，表示这个一个Spring Boot的配置类

​				**@Configuration**：配置类（配置文件），配置类也是容器中的一个组件@Component

**@EnableAutoConfiguration**：开启自动配置功能

​				以前我们配置的东西，Spring Boot帮我们自动配置，**@EnableAutoConfiguration**告诉Spring Boot开				启自动配置更能，这样自动配置的功能才能生效

```java
@AutoConfigurationPackage
@Import({EnableAutoConfigurationImportSelector.class})
public @interface EnableAutoConfiguration {
```

**@AutoConfigurationPackage**：自动配置包

```java
@Import({Registrar.class})
public @interface AutoConfigurationPackage {}
```

​				Spring的底层注解**@Import**，给容器导入一个组件，导入的组件

```java
public abstract class AutoConfigurationPackages {
    public static void register(BeanDefinitionRegistry registry, String... 
           GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
           beanDefinition.setBeanClass(AutoConfigurationPackages.BasePackages.class);
           beanDefinition.getConstructorArgumentValues().addIndexedArgumentValue(0, packageNames);
            beanDefinition.setRole(2);
            registry.registerBeanDefinition(BEAN, beanDefinition);
        }

    }
```

​				AutoConfigurationPackages.java的register()将主配置类（@SpringBootApplication标注的类）的所在的包及下面所有子包里面的所有组件注册到Spring容器中。

​				@Import({EnableAutoConfigurationImportSelector.class})给容器中导入组件

​						@Import 注解，用来导入配置类，这些配置方式又分为三种类型：

​							1. 直接导入配置类：@Import({xxxConfiguration.class})

​							2.依据条件选择配置类：@Import({xxxSelector.class})

​							3.动态注册 Bean：@Import({xxxRegistrar.class})

​				EnableAutoConfigurationImportSelector.class：将所有导入的组件以全类名的方式返回；这些组件会被添加到容器中。给容器导入90多个自动配置类（xxxxAutoConfiguration），给容器中导入这个场景需要的所有组件，并配置好这些组件。有了这些自动配置类，就省去了手动自动配置的工作。

[参考博客](https://blog.csdn.net/zchdjb/article/details/90795633)

![](E:\study\spring boot\md pic\2019-12-08 155612.png)

AutoConfigurationImportSelector.java的Line.64

```java
List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);
```

SpringFactoriesLoader.java的loadFactoryNames()

```java
public static List<String> loadFactoryNames(Class<?> factoryClass, ClassLoader classLoader) {    
	String factoryClassName = factoryClass.getName();    
	try {        
		Enumeration<URL> urls = classLoader != null ? classLoader.getResources("META-INF/spring.factories") : ClassLoader.getSystemResources("META-INF/spring.factories"); 
```

Spring Boot启动的时候从类路径下META-INF/spring.factories中获取EnableAutoConfiguration指定的值，将这些值作为自动配置类导入到Spring容器中，自动配置类生效。程序员以前需要手动配置的东西，自动配置类帮我们实现了。spring-boot将所有功能场景都抽取出来，做成一个个starter(场景启动器)，只需要在项目里导入这些starter，需要什么功能就导入哪个场景启动器。

![2019-12-08 1556122](E:\study\spring boot\md pic\2019-12-08 1556122.png)

J2EE的整体解决方案和自动配置都在spring-boot-autoconfigure:x.x.x.jar包中。

### 2、使用Spring Initializer快速创建Spring Boot项目

使用IDEA的Spring Initializer快速创建的项目

+ resources文件夹的目录结构
  + static：保存所有的静态资源；js,css,images
  + templates：保存所有的模板页面（Spring Boot默认jar包使用嵌入式的Tomcat，默认不支持jsp页面），可以使用模板引擎（freemarker,thymeleaf）
  + application.properties：Spring Boot应用的配置文件，可以修改一些默认设置

## 二、配置文件

###  1、配置文件

Spring Boot使用一个全局配置文件，配置文件名是固定的。

*  application.properties

* application.yml

配置文件的作用：修改Spring Boot自动配置的默认值

YAML：标记语言，以数据为中心，比json、xml等更适合做配置文件。

### 2、YAML语法
#### 1、基本语法

以**空格**的缩进来控制层级关系；只要是左对齐的一列数据，都是同一个层级的

```yaml
server:
    port: 8081
    path: /hello
```

属性和值是大小写敏感的。

#### 2、值的写法

 ##### 字面量：普通的值（数字，字符串，布尔）

​		key: value : 字面直接写

​					字符串默认不用加上单引号或者双引号；

​					“”：双引号；不会转义字符串里面的特殊字符；

​						例：name: "Hello \n World!" => Hello 换行 World!

​					''：单引号；会转义特殊字符

​						例：name: 'Hello \n World!' => Hello \nWorld!

##### 对象、Map（属性和值）（键值对）

​		key:value ：在下一行写对象的属性和值的关系，注意缩进

```yaml
film:
  name: Frozen
  length: 115
```

行内写法

```yaml
film: {name: Frozen,length: 115}
```

##### 数组（List，Set）

用- 值表示数组中的一个元素

```yaml
pets:
 - cat
 - dog
 - pig
```

行内写法

```yaml
pets: [cat,dog,pig]
```

### 3、配置文件注入

配置文件

```yaml
film:
  title: ZOOLANDER FICTION
  year: 2006
  length: 50
  actors:
    - {firstName: NICK,lastName: WAHLBERG}
    - {firstName: JOHNNY,lastName: LOLLOBRIGIDA}
```

javaBean

```java
/**
 * 将配置文件中配置的每个属性的值，映射到这个组件中
 * @ConfigurationProperties：将类与配置文件中的配置进行绑定
 * @Component：只有这个组件是容器中的组件，才能提供@ConfigurationProperties功能
 */
@ConfigurationProperties(prefix="film")
@Component
public class Film {

    private String title;
    private String year;
    private Integer length;
    private List<Actor> actors;

```

导入配置文件处理器，配置文件进行绑定就会有提示

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
```

application.properties实现跟yaml同样的功能

```properties
film.title=ZOOLANDER FICTION
film.year=2006
film.length=50
film.actors[0].first-name=NICK
film.actors[0].last-name=WAHLBERG
film.actors[1].first-name=JOHNNY
film.actors[1].last-name=LOLLOBRIGIDA
```

###### ##### @Value获取值和@ConfigurationProperties的区别

|                      | @ConfigurationProperties | @Value     |
| -------------------- | ------------------------ | ---------- |
| 功能                 | 批量注入配置文件中的属性 | 一个个指定 |
| 松散绑定（松散语法） | 支持                     | 不支持     |
| SpEL                 | 不支持                   | 支持       |
| JSR303数据校验       | 支持                     | 不支持     |
| 复杂类型封装         | 支持                     | 不支持     |

获取配置文件中的一项的值，用@Value

获取配置文件对应的一个JavaBean的属性值，用@ConfigurationProperties

### 4、@PropertySource & @ImportResource

  @PropertySource：加载指定的配置文件

```java
@PropertySource(value ={"classpath:film.yml"})
```

@ImportResource：导入Spring的配置文件，让配置文件里面的内容生效。

```java
@ImportResource(locations = {"classpath:bean.xml"})
```

Spring Boot推荐给容器中添加组件的方式，

### 5、配置文件占位符

#### 1、随机数

```properties
${radom.value}、${radom.int}、${radom.uuid}
```

#### 2、获取之前配置的值，如果没有可以使用: 默认值

```properties
film.title=${film.name:title1}
```

### 6、Profile

#### 1、多profile文件

在编写主配置文件的时候，文件名可以是application-{profile}.properties/yml

#### 2、yml支持多文档块方式

```yml
server:
  port: 8081
spring:
  profiles:
    active: prod
---
server:
  port: 8082
spring:
  profiles: dev
---
server:
  port: 8083
spring:
  profiles: prod
```



#### 3、激活指定profile

##### 1、在主application.properties配置文件中指定，激活哪个profile的配置文件

```properties
spring.profiles.active=dev
```

##### 2、命令行

java -jar xxxx.jar --spring.profiles.active=dev

可以在测试的时候，配置传入命令行参数（Program arguments）

##### 3、JVM参数

VM options:-Dspring.profiles.active=dev

#### 4、配置文件加载位置

SpringBoot启动会扫描以下位置的application.properties或者application.yml，作为SpringBoot的默认配置文件，优先级由高到低，高优先级的配置会覆盖低优先级的配置。

* file:./config/ （工程目录）
* file:./
* classpath:/config/ （src目录）
* classpath:/

可以通过spring.config.location改变默认配置文件的位置

项目打包以后，可以使用命令行参数的形式，启动项目的时候，来指定配置文件的新位置，指定的配置文件和默认加载的配置文件，会共同起作用，进行互补配置。

#### 5、外部配置加载顺序

springboot可以以下位置加载配置文件；优先级由高到低；高优先级的配置文件覆盖低优先级的配置，所有的配置会形成互补配置

##### 1.命令行参数

过个配置用空格分开

java -jar config-0.0.1-SNAPSHOT.jar --server.port=8083 --server.context-path=/boot

##### 2.来自java:comp/env的JNDI属性

##### 3.java系统属性(System.getProperties())

##### 4.操作系统环境变量

##### 5.RandomValuePropertySource配置的random.*属性

优先级：有jar包外向jar包内进行寻找

优先加载带profile的配置

##### 6.jar包外部的application-{profile}.properties或application.yml（带spring.profile）配置文件

##### 7.jar包内部的application-{profile}.properties或application.yml（带spring.profile）配置文件

再来加载不带profile的配置

##### 8.jar包外部的application.properties或application.yml（不带spring.profile）配置文件

##### 9.jar包内部的application.properties或application.yml（不带spring.profile）配置文件

##### 10.@Configuration注解类上的@PropertyValue

##### 11.通过SpringApplication.setDefaultProperties指定的默认属性

#### 6、自动配置原理

[配置文件可以配置的属性参照](https://docs.spring.io/spring-boot/docs/2.1.11.RELEASE/reference/html/common-application-properties.html)

##### 1）、SpringBoot启动的时候加载主配置类，开启了自动配置功能@EnableAutoConfiguration

##### 2）、@EnableAutoConfiguration的作用

* 利用AutoConfigurationImportSelector给容器中导入一些组件

* 可以查看AutoConfigurationImportSelector的selectImports()方法的内容

* getAutoConfigurationEntry()获取候选配置

  ```java
  List<String> configurations = this.getCandidateConfigurations(annotationMetadata, attributes);
  ```

  + 扫描所有jar包类路径下META-INF/spring.factories资源，把扫描到的资源封装成Properties对象，从properties中获取到EnableAutoConfiguration.class类（类名）对应的值，添加到容器中

    ```java
    SpringFactoriesLoader.loadFactoryNames
    ```

  ​        将类路径下的META-INF/spring.factories里面配置的所有的EnableAutoConfiguration的值加入到容器中。每一个这样的xxxAutoConfiguration类都是容器中的组件，加入到容器中，来进行自动配置。

  ```properties
  # Auto Configure
  org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration,\
  org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
  org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration,\
  ```

  ##### 3）、每个自动配置类完成自动配置功能

  ##### 4）、以HttpEncodingAutoConfiguration（Http编码自动配置）为例解释自动配置原理

  ```java
  @Configuration(  //这是一个配置类
      proxyBeanMethods = false
  )
  @EnableConfigurationProperties({HttpProperties.class})//启用指定类的ConfigurationProperties功能，将配置文件中的值，与HttpProperties类进行绑定，并加入到Spring的IOC容器中
  @ConditionalOnWebApplication( //Spring底层的Conditional注解，根据不同的条件，如果满足指定的条件，整个配置类里的配置才会生效，判断当前应用是否是Web应用，如果是的话，配置生效
      type = Type.SERVLET
  )
  @ConditionalOnClass({CharacterEncodingFilter.class})//判断当前项目是否有CharacterEncodingFilter类，SpringMVC中进行乱码解决的过滤器
  @ConditionalOnProperty(//判断配置文件中是否存在某个配置spring.http.encoding.enabled
      如果不存在，也认为是成立的matchIfMissing = true
      prefix = "spring.http.encoding",
      value = {"enabled"},
      matchIfMissing = true
  )
  public class HttpEncodingAutoConfiguration {
      
      //在项目启动的自动加载配置时，已经跟SpringBoot的配置文件完成映射
      private final Encoding properties;
  	
      //只有一个有参构造器的情况下，参数的值就会从容器中取得
      public HttpEncodingAutoConfiguration(HttpProperties properties) {
          this.properties = properties.getEncoding();
      }
  
          @Bean //给容器中添加一个组件，这个组件的某些值，需要从properties中获取
      @ConditionalOnMissingBean
      public CharacterEncodingFilter characterEncodingFilter() {
          CharacterEncodingFilter filter = new OrderedCharacterEncodingFilter();
          filter.setEncoding(this.properties.getCharset().name());
          filter.setForceRequestEncoding(this.properties.shouldForce(org.springframework.boot.autoconfigure.http.HttpProperties.Encoding.Type.REQUEST));
          filter.setForceResponseEncoding(this.properties.shouldForce(org.springframework.boot.autoconfigure.http.HttpProperties.Encoding.Type.RESPONSE));
          return filter;
      }
  ```

  根据当前不同的条件判断，决定这个配置类是否生效？

  一旦这个配置类生效，这个配置类就会给容器中添加各种组件，这些组件的属性是从对应的properties类中获取的，这些类里面的每一个属性是和配置文件绑定的。

  ##### 5）、所有的能在配置文件中能配置的属性都在xxxProperties类被封装，配置文件能够配置的属性可以参照相应的Properties类的属性。

  ```java
  @ConfigurationProperties(
      prefix = "spring.http" //从配置文件中获取指定的值和bean的属性进行绑定
  )
  public class HttpProperties {
  ```

  ###### SpringBoot的精华

  * SpringBoot启动会加载大量的自动配置类

  * 程序员需要的功能有没有SpringBoot默认写好的自动配置类

  * 这个自动配置类到底配置了哪些组件，只要程序员需要的组件已经写好，就不需要编写了。

  * 给容器中自动配置类添加组件的时候，会从Properties类中获取某些属性，程序员可以在配置文件中指定这些属性的值

    xxxxAutoConfiguration：自动配置类

    xxxxProperties：封装配置文件中相关的属性

  #### 7、细节

  ##### 1、@Conditional派生注解（Spring注解版原生的@Conditional作用）
  
  作用：必须是@Conditional指定的条件成立，才给容器中添加组件，配置里面的所有内容才生效
  
  ![批注 2019-12-11 203956](E:\study\spring boot\md pic\批注 2019-12-11 203956.png)

自动配置类必须在一定的条件下才生效

通过在application.properties里添加debug=true，SpringBoot以debug模式运行，启动时log会生成配置类可用的报告。

```java
============================
CONDITIONS EVALUATION REPORT
============================


Positive matches://生效的配置类
-----------------

   AopAutoConfiguration matched:
```

```java
Negative matches: //没生效的配置类
-----------------

   ActiveMQAutoConfiguration:
      Did not match:
```



## 三、日志文件

### 1、日志框架

|                   日志门面（日志的抽象层）                   |                           日志实现                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| JCL（Jakarta Commons Logging）    SLF4J(Simple Logging Facade for java) | Log4j JUL(java.util.logging)                 Log4j2   Logback |

左边选一个门面，右边选一个实现；

日志门面选择：SLF4J

日志实现选择：Logback

SpringBoot：底层是Spring框架，Spring框架默认是用JCL；SpringBoot选用SLF4J和Logback

### 2、SLF4J使用

#### 1、如何在系统中使用SLF4J

以后开发的时候，日志记录方法的调用，不应该直接调用日志的实现类，而应该调用日志抽象层的方法。

[SLF4J官方文档](http://www.slf4j.org/manual.html)

 给项目里导入slf4j的jar和logback的实现jar

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorld {
  public static void main(String[] args) {
    Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    logger.info("Hello World");
  }
}
```

每一个日志的实现框架都有自己的配置文件，使用slf4j以后，配置文件还是用日志实现类本身的。

#### 2、统一日志框架

统一项目中各个框架的使用的日志框架，统一使用slf4j进行输出

###### 1、将项目中其他日志框架先排除，

###### 2、用中间包替换原有的日志框架

3、导入slf4j的实现

#### 3、SpringBoot日志关系

SpringBoot使用该组件完成日志功能。

```xml
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-logging</artifactId>
    </dependency>
```

###### 1）、SpringBoot底层也是使用slf4j+logback的方式进行日志记录

###### 2）、SpringBoot利用中间转换包，把其他的日志都替换成为slf4j

###### 3）、中间转换包重写了元日志框架的类和方法，使用slf4j进行记录

###### 4）、将框架默认的日志依赖移除`<exclusion>`

所以SpringBoot能够自动适配所有的日志，引入其他框架的时候，需要将这个框架依赖的日志框架移除

#### 4、默认配置文件

在org.springframework.boot.jar的logging文件夹下

| `logging.file.name` | `logging.file.path` | 例         | 描述                                     |
| :------------------ | :------------------ | :--------- | :--------------------------------------- |
| *(none)*            | *(none)*            |            | 只输出到控制台                           |
| 指定文件            | *(none)*            | `my.log`   | 输出到指定的文件，该文件在项目的相对目录 |
| *(none)*            | 指定目录            | `/var/log` | 输出到指定目录下 `spring.log` 文件       |

#### 5、指定配置

给类路径下放置每个日志框架各自的配置文件即可，SpringBoot将不使用自己的默认配置了。

| 日志框架                | Customization                                                |
| :---------------------- | :----------------------------------------------------------- |
| Logback                 | `logback-spring.xml`, `logback-spring.groovy`, `logback.xml`, or `logback.groovy` |
| Log4j2                  | `log4j2-spring.xml` or `log4j2.xml`                          |
| JDK (Java Util Logging) | `logging.properties`                                         |

logback.xml：被日志框架自动识别

logback-spring.xml：日志框架不加载日志文件的配置项，但可以由SpringBoot加载

```xml
<springProfile name="staging">
    <!-- 可以指定某个配置只在某个环境下生效-->
</springProfile>
<springProfile name="dev | staging">
    <!-- dev | staging 环境生效-->
</springProfile>

```

## 四、Web开发

### 1、使用SpringBoot

**1）、创建SpringBoot应用，选择使用的模块**

**2）、SpringBoot默认将模块创建完成，只需要在配置文件中指定少量配置就可以运行**

**3）、编写业务代码**

自动配置原理？

这个模块SpringBoot帮我们配置了什么？嫩不能修改？能修改哪些配置？能不能扩展？

```end
xxxxAutoConfiguration：完成在容器中自动配置组件

xxxxProperties：封装配置文件的内容
```

### 2、SpringBoot对静态资源的映射规则

```java
@ConfigurationProperties(
    prefix = "spring.resources",
    ignoreUnknownFields = false
)
public class ResourceProperties {
    //可以设置和静态资源有关的参数，缓存时间等
```

WebMvcAutoConfiguration.java

```java
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            if (!this.resourceProperties.isAddMappings()) {
                logger.debug("Default resource handling disabled");
            } else {
                Duration cachePeriod =                     this.resourceProperties.getCache().getPeriod();
                CacheControl cacheControl =                 this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
                if (!registry.hasMappingForPattern("/webjars/**")) {
                    this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"}).setCachePeriod(this.getSeconds(cachePeriod)).setCacheControl(cacheControl));
                }

                String staticPathPattern = this.mvcProperties.getStaticPathPattern();
                if (!registry.hasMappingForPattern(staticPathPattern)) {
                                    this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{staticPathPattern}).addResourceLocations(WebMvcAutoConfiguration.getResourceLocations(this.resourceProperties.getStaticLocations())).setCachePeriod(this.getSeconds(cachePeriod)).setCacheControl(cacheControl));
                }

            }
        }
		
		//欢迎页的映射
        @Bean
        public WelcomePageHandlerMapping welcomePageHandlerMapping(ApplicationContext applicationContext, FormattingConversionService mvcConversionService, ResourceUrlProvider mvcResourceUrlProvider) {
            WelcomePageHandlerMapping welcomePageHandlerMapping = new WelcomePageHandlerMapping(new TemplateAvailabilityProviders(applicationContext), applicationContext, this.getWelcomePage(), this.mvcProperties.getStaticPathPattern());
            welcomePageHandlerMapping.setInterceptors(this.getInterceptors(mvcConversionService, mvcResourceUrlProvider));
            return welcomePageHandlerMapping;
        }
```



1）、在`classpath:/META-INF/resources/webjars/`下找资源文件

webjars：以jar包的方式引入静态资源

[webjars官网](www.webjars.org)   导入org.webjars.jquery后的目录结构

![批注 2019-12-13 204940jq](E:\study\spring boot\md pic\批注 2019-12-13 204940jq.png)

http://localhost:8080/webjars/jquery/3.3.1/jquery.js

2）、"/**"访问当前项目的任何资源，下面是静态资源的文件夹

ResourceProperties.java

```java
private static final String[] CLASSPATH_RESOURCE_LOCATIONS = new String[]{"classpath:/META-INF/resources/", 
                                                                          "classpath:/resources/", 
                                                                          "classpath:/static/", 
                                                                          "classpath:/public/"};
```

3）、欢迎页，静态资源文件夹下的所有index.html页面，被/**映射

​	localhost:8080/index.html

4）、所有的页面的图标**/favicon.ico，都是在静态资源文件夹下

### 3、模板引擎

[Thymeleaf官网](https://www.thymeleaf.org/)

![批注 2019-12-14 053525html](E:\study\spring boot\md pic\批注 2019-12-14 053525html.png)

### 4、引入thymeleaf

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
    <!-- 更改SpringBoot的thymeleaf默认版本 -->
    <properties>
        <thymeleaf.versin>3.0.9.RELEASE</thymeleaf.versin>
        <!--布局功能的支持程序 thymeleaf是3的话，layout要求2以上版本-->
        <thymeleaf-layout-dialect.version>2.2.2</thymeleaf-layout-dialect.version>
    </properties>
```

### 5、Thymeleaf使用&语法

ThymeleafProperties.java

```java
    public static final String DEFAULT_PREFIX = "classpath:/templates/";
    public static final String DEFAULT_SUFFIX = ".html";
    private String prefix = "classpath:/templates/";
    private String suffix = ".html";
    private String mode = "HTML";
```

#### 1、导入Thymeleaf的名称空间

```html
<html lang="en" xmlns:th="http://www.thymeleaf.org">
```

#### 2、使用Thymeleaf语法

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    <div th:text="${hello}">SpringBoot</div>
</body>
</html>
```

#### 3、语法规则

##### 1、th:text

改变当前元素里面的文本内容

th：任意html属性，替换原生属性的值

| Order | Feature                                               | Attributes     |
| :---- | ----------------------------------------------------- | -------------- |
| 1     | Fragment inclusion（片段包含，jsp:include）           | th:insert      |
|       |                                                       | th:replace     |
| 2     | Fragment iteration（遍历）                            | th:each        |
| 3     | Conditional evaluation（条件判断）                    | th:if          |
|       |                                                       | th:unless      |
|       |                                                       | th:switch      |
|       |                                                       | th:case        |
| 4     | Local variable definition（声明变量）                 | th:object      |
|       |                                                       | th:with        |
| 5     | General attribute modification（任意属性修改）        | th:attr        |
|       |                                                       | th:attrprepend |
|       |                                                       | th:attrappend  |
| 6     | Specific attribute modification（修改制定属性默认值） | th:value       |
|       |                                                       | th:href        |
|       |                                                       | th:src         |
| 7     | Text (tag body modification)（修改标签体内容）        | th:text        |
|       | （text：转义特殊字符；utext：不转义特殊字符）         | th:utext       |
| 8     | Fragment specification（声明片段）                    | th:fragment    |
| 9     | Fragment removal                                      | th:remove      |

##### 2、表达式

```properties
Simple expressions:(表达式语法)
    Variable Expressions: ${...} #获取变量值
            #1.获取对象的属性，调用方法
            #2.使用内置的基本对象
                #ctx : the context object.
                #request : (only in Web Contexts) the HttpServletRequest object.
                #response : (only in Web Contexts) the HttpServletResponse object.
                #session : (only in Web Contexts) the HttpSession object.
                #servletContext : (only in Web Contexts) the ServletContext object.
                ：
            #3.内置工具对象
                #numbers : methods for formatting numeric objects
                #arrays : methods for array
				#lists : methods for lists
				：
    Selection Variable Expressions: *{...} #变量选择表达式，同${}功能相同，
    										#配合th:object一起使用
    Message Expressions: #{...}#获取国际化内容
    Link URL Expressions: @{...}#定义URL
    Fragment Expressions: ~{...}#片段引用表达式

Literals（字面量）
    Text literals: 'one text' , 'Another one!' ,…
    Number literals: 0 , 34 , 3.0 , 12.3 ,…
    Boolean literals: true , false
    Null literal: null
    Literal tokens: one , sometext , main ,…

Text operations:（文本操作）
    String concatenation: +
    Literal substitutions: |The name is ${name}|

Arithmetic operations:（数学运算）
    Binary operators: + , - , * , / , %
    Minus sign (unary operator): -

Boolean operations:（布尔运算）
    Binary operators: and , or
    Boolean negation (unary operator): ! , not

Comparisons and equality:（比较运算）
    Comparators: > , < , >= , <= ( gt , lt , ge , le )
    Equality operators: == , != ( eq , ne )

Conditional operators:（条件运算）
    If-then: (if) ? (then)
    If-then-else: (if) ? (then) : (else)
    Default: (value) ?: (defaultvalue)
Special tokens:（特殊操作）
No-Operation: _
```



# 到31讲

## 五、SpringBoot与Docker

### 1、简介

### 2、核心概念

Docker主机（Host）：安装了Docker的机器（Docker直接安装在操作系统上）

Docker客户端（Client）： 连接Docker主机进行操作

Docker仓库（Registry）：保存打包好的软件镜像

Docker镜像（Images）：软件打包好的镜像，放在Docker仓库中

Docker容器（Container）：镜像启动后的实例，称为容器；容器是独立运行的一个或一组应用

**使用Docker步骤：**

1、安装Docker

2、在Docker仓库找到这个软件的镜像

3、使用Docker运行这个镜像，这个镜像会生成一个Docker容器

4、对容器的启动停止，就是对软件的启动停止

### 3、安装Docker

#### 1、安装Linux虚拟机

1）、VMWare、VirtualBox

2）、导入虚拟机文件centos7.ova

3）、双击启动Linux虚拟机，使用root/dvwa登录

4）、使用客户端连接Linux虚拟机

5）、设置虚拟机网络

​			桥接网络->选好网卡(跟主机windows10相同的无线网卡)->接入网线

6）、设置好网络以后使用命令重启虚拟机的网络，或者重启虚拟机

```shell
+
```

如果失败，参考[博客](https://blog.csdn.net/gunxueqiucjw/article/details/27231687)，[修改IP](https://blog.csdn.net/qq_41875147/article/details/81144327)，[启动异常](https://blog.csdn.net/yelllowcong/article/details/80389481)

7）、查看Linux的IP地址 ip addr

192.168.1.103

8）、使用客户端连接

#### 2、在Linux虚拟机上安装Docker

步骤

```shell
1、检查内核版本必须是3.10以上
uname -r
2、安装docker
yum install docker
如果镜像出错，无法安装，[参考](https://blog.csdn.net/qq_30938705/article/details/87281698)
3、输入y确认安装
4、启动docker
systemctl start docker
docker -v
5、开机启动docker
systemctl enable docker
6、停止docker
systemctl stop docker
7、删除docker
yum -y  remove  docker  docker-common  docker-selinux  docker-engine
8、设置docker国内镜像
touch /etc/docker/daemon.json
{
"registry-mirrors": ["http://hub-mirror.c.163.com"]
}
重启docker
systemctl restart docker
```

### 4、Docker常用命令和操作

#### 1、镜像操作

取得镜像，默认取得最新版，也可以指定版本 docker pull mysql

查看左右本地镜像 docker images

删除指定的本地镜像 docker rmi image-id

https://hub.docker.com/

#### 2、容器操作

软件镜像 -> 运行镜像 -> 产生一个容器（正在运行的软件）

步骤：

```shell
1、搜索镜像
docker search tomcat
2、拉取镜像
docker pull tomcat
3、根据镜像，启动容器
docker run -d tomcat ##[-d]后台运行；启动不了的话，可能centos太旧，yum update更新 
4、查看运行中的容器
docker ps
5、停止运行中的容器
docker stop 容器ID
6、查看所有的容器
docker ps -a
7、启动容器
docker start 容器ID
8、删除容器
docker rm 容器ID
9、启动一个做了端口映射的tomcat
docker run -d -p 8888:8080 tomcat
-d : 后台运行
-p : 将主机的端口，映射到容器的端口  主机端口：容器端口
# http://192.168.1.103:8888/ 可以看到tomcat管理画面，如果看到可以进行一下操作
#查看防火墙状态
#service firewalld status
#临时关闭防火墙
#systemctl stop firewalld
#禁止开机启动
#systemctl disable firewalld
#如果启动容器时出现错误，重启docker
# systemctl stop docker
# systemctl start docker
10、查看容器日志
docker logs 容器ID

可以用一个镜像生成多个容器

#更多命令参照
https://docs.docker.com/engine/reference/commandline/docker/
可以参考每个镜像的文档

mysql 启动
docker run -p 3307:3306 -e MYSQL_ROOT_PASSWORD=123456 -d mysql 
```

## 六、SpringBoot与数据访问

## 七、启动配置原理

重要的事件回调机制

配置在META-INF/spring.factories

ApplicaitonContextInitializer

SpringApplicationRunListener

加载在IOC容器中 @Componet

ApplicationRunner

CommandLineRunner

#### 1、启动流程

##### 1、创建SpringApplication对象

```java
public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
    //判断当前是否是Web应用
    this.webApplicationType = WebApplicationType.deduceFromClasspath();
    //从META-INF/spring.factories下获取ApplicationContextInitializer，保存
    this.setInitializers(this.getSpringFactoriesInstances(
        ApplicationContextInitializer.class));
    //从META-INF/spring.factories下获取ApplicationListener，保存
    this.setListeners(
        this.getSpringFactoriesInstances(ApplicationListener.class));
    //从多个配置类中，找到有main方法的主配置类
    this.mainApplicationClass = this.deduceMainApplicationClass();
}
```

##### 2、运行run方法

```java
public ConfigurableApplicationContext run(String... args) {
    //从META-INF/spring.factories下获取SpringApplicationRunListeners（默认配置）
    SpringApplicationRunListeners listeners = this.getRunListeners(args);
    //回调启动
    listeners.starting();
    try {
		//封装命令行参数
        ApplicationArguments applicationArguments =
            new DefaultApplicationArguments(args);
        //准备环境
        //常见环境完成后，回调SpringApplicationRunListeners.environmentPrepared()，
        //表示环境准备完成
        ConfigurableEnvironment environment = 
              this.prepareEnvironment(listeners, applicationArguments);
        this.configureIgnoreBeanInfo(environment);
        //打印Spring 图标
        Banner printedBanner = this.printBanner(environment);
        //创建ApplicationContext，创建web的IOC，还是普通的IOC
        context = this.createApplicationContext();
        //准备上下文环境，将environment保存到IOC中，
        //回调之前保存的ApplicationContextInitializer.initialize()
        //回调SpringApplicationRunListener.contextPrepared()
        this.prepareContext(context, environment, 
                            listeners, applicationArguments, printedBanner);
        //prepareContext运行完成后，回调所有的SpringApplicationRunListener的ContextLoader方法
        //刷新容器，Spring IOC容器初始化，如果是Web容器，还会创建嵌入式的Tomcat
        this.refreshContext(context);
        //从IOC容器中获取所有的ApplicationRunner和CommandLineRunner进行回调
        //先回调ApplicationRunner，再回调CommandLineRunner
        this.afterRefresh(context, applicationArguments);
        stopWatch.stop();
        if (this.logStartupInfo) {
            (new StartupInfoLogger(this.mainApplicationClass)).
                logStarted(this.getApplicationLog(), stopWatch);
        }

        listeners.started(context);
        this.callRunners(context, applicationArguments);
        } catch (Throwable var10) {
}
```

# 到70讲

## 第三章 SpringBoot与检索

### 一、了解Elasticsearch

docker run -e ES_JAVA_OPS="-Xms256m -Xmx256m" -d -p 9200:9200 -p 9300:9300 --name ES01 2bd69c322e98 镜像ID

修改配置文件

find . -name elasticsearch.yml

```shell
[root@localhost /]# find . -name elasticsearch.yml
./var/lib/docker/overlay2/c867403c261490aac50bd50b86dd2e27c4973ccdc6a18c2be3a74cc18523052d/diff/usr/share/elasticsearch/config/elasticsearch.yml
./var/lib/docker/overlay2/a42c81ae0efb9bde7f7b966df2db9a5bb617611df055173d4a6419e514a434e6/diff/usr/share/elasticsearch/config/elasticsearch.yml
[root@localhost /]# cat ./var/lib/docker/overlay2/a42c81ae0efb9bde7f7b966df2db9a5bb617611df055173d4a6419e514a434e6/diff/usr/share/elasticsearch/config/elasticsearch.yml
cluster.name: "docker-cluster"
network.host: 0.0.0.0
cluster.initial_master_nodes: ["node-1"]
bootstrap.memory_lock: false
bootstrap.system_call_filter: false
```

[docker安装位置](https://blog.csdn.net/runner668/article/details/80713713)

[docker容器和镜像区别](https://www.cnblogs.com/bethal/p/5942369.html)

![image-20191220063415723](C:\Users\HP\AppData\Roaming\Typora\typora-user-images\image-20191220063415723.png)

索引 -> 数据库/schema

类型->表

文档->记录

属性->字段

### 二、跟SpringBoot整合

#### 1、使用Jedis

#### 2、使用SpringData JPA

| Spring Data Elasticsearch | Elasticsearch |
| :-----------------------: | :-----------: |
|           3.2.x           |     6.8.1     |
|           3.0.x           |     5.5.0     |

使用Docker下载Elasticsearch 6.8.1版本

### 六、SpringBoot与分布式

#### 1、zookeeper启动

$ docker run --name zk01 -p 2181:2181 --restart always -d 镜像ID

#### 2、创建Consumer和Provider工程

##### 1）Provider工程

###### 1.中引入dubbo和zookeeper依赖

dubbo与SpringBoot整合的GitHub

https://github.com/apache/dubbo-spring-boot-project/blob/master/README_CN.md

| Dubbo Spring Boot                                            | Dubbo  | Spring Boot |
| ------------------------------------------------------------ | ------ | ----------- |
| [0.2.1.RELEASE](https://github.com/apache/dubbo-spring-boot-project/tree/0.2.x) | 2.6.5+ | 2.x         |
| [0.1.2.RELEASE](https://github.com/apache/dubbo-spring-boot-project/tree/0.1.x) | 2.6.5+ | 1.x         |

```xml
       <!-- 引入Dubbo依赖-->
        <dependency>
            <groupId>com.alibaba.boot</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
            <version>0.2.1.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dubbo</artifactId>
            <version>2.6.5</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
            <version>4.0.0</version>
        </dependency>
        <!-- 引入zookeeper客户端-->
        <dependency>
            <groupId>com.github.sgroschupf</groupId>
            <artifactId>zkclient</artifactId>
            <version>0.1</version>
            <exclusions>
                <exclusion>
                    <groupId>org.apache.zookeeper</groupId>
                    <artifactId>zookeeper</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
```

###### 2.配置dobbo注册中心地址 application.properties

```properties
dubbo.application.name=provicer-ticket
dubbo.registry.address=zookeeper://192.168.1.200:2181
dubbo.scan.base-packages=com.wh.ticket.service
```

###### 3.ServiceImpl类中，加入下列注解，@Service发布服务

```java
@Component
@Service//com.alibaba.dubbo.config.annotation.Service
public class TicketServiceImpl implements  TicketService{
```

##### 1）Consumer工程

###### 1.中引入dubbo和zookeeper依赖

跟Provider工程同样

###### 2.配置dobbo注册中心地址 application.properties

```properties
dubbo.application.name=comsumer-user
dubbo.registry.address=zookeeper://192.168.1.200:2181
```

###### 3.将Provider工程中的接口Service，拷贝到Consumer工程，package相同

```java
@Component
@Service//com.alibaba.dubbo.config.annotation.Service
public class TicketServiceImpl implements  TicketService{
```

###### 4.调用远程Service接口

```java
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;
import com.wh.ticket.service.TicketService;

@Service
public class UserService {
    @Reference
    TicketService ticketService;

    public void hello(){
        System.out.println(ticketService.getTicket());
    }
}
```

