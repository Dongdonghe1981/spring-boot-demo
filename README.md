



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