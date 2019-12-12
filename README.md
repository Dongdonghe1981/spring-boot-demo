



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

#### 1、日志框架

|                   日志门面（日志的抽象层）                   |                           日志实现                           |
| :----------------------------------------------------------: | :----------------------------------------------------------: |
| JCL（Jakarta Commons Logging）    SLF4J(Simple Logging Facade for java) | Log4j JUL(java.util.logging)                 Log4j2   Logback |

左边选一个门面，右边选一个实现；

日志门面选择：SLF4J

日志实现选择：Logback

SpringBoot：底层是Spring框架，Spring框架默认是用JCL；SpringBoot选用SLF4J和Logback

#### 2、SLF4J使用

##### 1、如何在系统中使用SLF4J

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

##### 2、统一日志框架

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

给类路径下放置每个日志框架各自的配置文件即可，SpringBoot将不使用自己的默认配置了。