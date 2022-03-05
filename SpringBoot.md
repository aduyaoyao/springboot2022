# 1.SpringBoot

## 1.SB介绍

```
概念：SpringBoot是整合Spring技术栈的一站式框架，它能简化Spring技术栈的快速开发的脚手架。
微服务概念：微服务是一种架构风格，一个大型复杂软件应用由一个或多个微服务组成。
分布式概念：一个业务分拆多个子业务，部署在不同的服务器上。
```

## 2.快速入门

### 2.1导入父工程和web依赖

```xml
<parent>
 	<groupId>org.springframework.boot</groupId>
 	<artifactId>spring-boot-starter-parent</artifactId>
	<version>2.6.2</version>
</parent>
<dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 2.2创建启动类

```java
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
```

### 2.3创建controller进行测试

```java
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String Handle(){
        String str = "hello";
        return str;
    }
}
```

### 2.4浏览器中输入127.0.0.1:/hello

![image-20211223214234298](E:\study\springBoot\img\hello.png)

### 2.5引入插件，简化部署

```xml
 <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

```
总结：	
    1.创建maven项目，pom中导入父工程和web-start依赖
    2.创建启动类MainApplication,添加SpringBootApplication注解标识该项目为SB,然后增加psvm。
    3.创建controller层进行测试。
    4.在application.yml中写配置内容
    5.可以简化部署方式。
        1.引入插件
        2.打成jar包，clean-package
        3.使用java -jar jar包
        jar包的BOOT-INF/classes是存放业务逻辑代码
        jar包的BOOT-INF/lib是存放逻辑所用jar包
```

## 3.SB基础

### 3.1SB特点

#### 3.1.1SB依赖管理

```xml
每一个SB项目所需要的父工程
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
	<version>2.6.2</version>
</parent>
该父工程的父工程。在这个工程里面，定义了所有开发中常用的依赖的版本号，即自动版本仲裁机制。
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-dependencies</artifactId>
    <version>2.6.2</version>
</parent>
web开发场景的启动器，自动引入web开发所需的相关依赖。
spring-boot-starter-xxx:官方的启动器
xxx-spring-boot-starter:第三方的启动器
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
</dependency>
官方启动器的最底层的依赖是Spring-boot-starter。
 <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter</artifactId>
      <version>2.6.2</version>
      <scope>compile</scope>
</dependency>

版本引入：
	1.引入SB中存在的依赖，可以不写版本。若版本不一致，只写版本号即可。
	2.否则，正常dependcy即可。
<properties>
    <mysql.version>5.1.43</mysql.version>
</properties>
```

#### 3.1.2SB自动配置

```
1.SB(web)默认为我们导入tomcat包
2.SB(web)默认为我们引入SpringMVC全套组件。
	例如：字符编码、请求转换（post->put）
3.SB默认扫描的包路径是启动类所在的包
4.每个配置都会绑定java类，并且这个类会在容器中创建对象。
5.SB所有的自动配置类都在spring-boot-autoconfigure-2.6.2.jar中
```

### 3.2.SB容器功能

#### 3.2.1添加组件

```
1.@Configuration
	作用：
		1.标识该类为一个SB的配置类。
		2.该配置类本身也是组件。
		3.存在一个属性proxyBeanMethods：代理bean的方法。
			true:Full模式，保证每个@Bean方法被调用都是单实例的，常用于组件依赖。
			false:Lite模式，@Bean方法被调用，全部都是新创建的。
		4.@Configuration中存在@Bean用于向容器中添加组件。
2.@Bean方法名作为组件id,返回类型为组件类型，返回的值就是组件在容器中的实例化对象。
@Bean
public User user1(){
	return new User();
}
3.@Component,@Controller,@Service,@Repository都可以向容器中注册组件
4.@ComponentScan用于组件扫描
5.@Import({User.class})给容器中自动创建出这两个类型的组件，默认id是全类名。
6.@Conditional:条件装配，满足Conditional中指定的条件，则进行组件注入。
```

#### 3.2.2引入原始配置文件

```
1.@ImportResource("classpath:beans.xml"):引入类路径下的beans.xml文件。将该注解放到配置类上。
```

#### 3.2.3配置绑定

```
1.@ConfigurationProperties(perfix="person")：将配置文件中前置为person的内容与该组件内容相绑定
2.实现配置绑定的方式：
	@ConfigurationProperties+@Component:适用于SB组件自定义想要读取配置文件中的内容
	@ConfigurationProperties+@EnableConfigurationProperties：适用于SB组件是第三方定义的
		该EnableConfigurationProperties(Car.class)作用：放到配置类上
			1.开启Car.class配置绑定
			2.将该Car.class注册到容器中
```

### 3.3自动配置原理分析

```
@SpringBootApplication包含3个注解
	@SpringBootConfiguration：SB的配置类
	@EnableAutoConfiguration：
		@AutoConfigurationPackage：
			@Import(AutoConfigurationPackages.Registrar.class)：给容器中导入一个组件，默认扫描主程序所在的包
		@Import(AutoConfigurationImportSelector.class)：
			1.调用AutoConfigurationImportSelector类中的selectImports()方法。
				1.getAutoConfigurationEntry(annotationMetadata)方法来批量导入一些组件。
					1.getCandidateConfigurations(annotationMetadata, attributes)获取到所有需要导入到容器中的配置类
						1.loadFactoryNames返回的是loadSpringFactories中从META-INF/spring.factories中加载文件
          最主要的是这个包spring-boot-autoconfigure-2.3.4.RELEASE.jar包里面也有META-INF/spring.factories	
	@ComponentScan：指定扫描哪些类
```

### 3.4按需自动配置

```
通过@Conditional注解，来条件加载这些自动配置。
```

### 3.5总结

```
1.SB通过@AutoConfigurationPackage加载主程序所在包的组件，通过@Import(AutoConfigurationImportSelector.class)自动配置。
2.每个自动配置类按照条件生效，从默认的配置文件类中取值，配置文件类和Properties进行了绑定。
3.容器中存在了自动配置类的组件，则配置的功能就生效了。
4.如果需要修改默认的配置文件的组件：
	1。直接新建一个@Bean，去替换默认的组件
	2.修改这个组件用到的配置文件的值
```

### 3.6开发技巧

```
1.lombok插件
	@Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
2.dev-tools:热启动，ctrl+F9刷新
3.Spring Initailizr
```

## 4.SB配置文件

```
1.yaml和properties文件都是SB的配置文件
2.基础语法：
    ● key: value；kv之间有空格
    ● 大小写敏感
    ● 使用缩进表示层级关系
    ● 缩进不允许使用tab，只允许空格
    ● 缩进的空格数不重要，只要相同层级的元素左对齐即可
    ● '#'表示注释
3.yaml自动提示插件，用于自己写的类和配置文件映射。spring-boot-configuration-processor
```

## 5.web开发（源码）

### 5.1静态资源访问

```
1.静态资源的目录（4个）：类路径下包的位置
	/static
	/public
	/resources
	/META-INF/resources
2.如何访问：当前项目根路径/+资源名
3.原理：
	请求进来，先去找Controller看能不能处理。不能处理的所有请求又都交给静态资源处理器。静态资源也找不到则响应404页面
```

### 5.2欢迎页

```
欢迎页的设计有2种：
	1.静态资源下的index.html
	2.controller可以处理/index
```

### 5.3自定义网页图标（Favicon）

```
favicon.ico直接放在静态资源目录下即可，浏览器会进行自动读取。
```

### 5.4原理

```
1.SB启动默认加载xxxAutoConfiguration类
2.在WebMvcAutoConfiguration中，将WebMvcProperties==spring.mvc、ResourceProperties==spring.resources关联起来。
3.欢迎页在它的welcomePageHandlerMapping中。
4.资源默认处理在addResourceHandlers中。
```

### 5.5请求参数处理

#### 5.5.1请求映射

```
1.通过下图所示的流程可知，所有请求都是最终会来到DispatcherServlet的doDispatch方法。
2.mappedHandler = getHandler(processedRequest);找到当前请求使用哪个Handler（Controller的方法）处理
	总共5种HandlerMapping，保存着请求和对应处理的方法之间的映射关系。
	通过比较HandlerMapping中存在着的路径映射关系，从而找到当前请求的控制器处理方法。
```

![image.png](E:\study\springBoot\img\doDispatch.png)

![image-20211229165934017](E:\study\springBoot\img\HandlerMapping.png)

#### 5.5.2普通参数与注解

```
1.常用注解：@PathVariable、@RequestHeader、@RequestParam、@MatrixVariable、@CookieValue、@RequestBody
2.矩阵变量
	1.请求路径：/cars/sell;low=34;brand=byd,audi,yd
   	2.SpringBoot默认是禁用了矩阵变量的功能
    //      手动开启：原理。对于路径的处理。UrlPathHelper进行解析。
    //              removeSemicolonContent（移除分号内容）支持矩阵变量的
    //3、矩阵变量必须有url路径变量才能被解析
```

```
原理（继续）：
	3.HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());获取处理该handler的handlerApapter。总共有4种。见下图。
	4.mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
		1.mav = invokeHandlerMethod(request, response, handlerMethod);
			this.argumentResolvers（27）
			this.returnValueHandlers（15）
			invocableMethod.invokeAndHandle(webRequest, mavContainer);
				1.Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
					//获取请求参数
					1.Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
						//遍历所有参数：遍历参数解析器看是否支持解析这种参数。
						resolvers.supportsParameter(parameter)
						//遍历所有参数：解析参数
						args[i] = this.resolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
							1.return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
								//获取参数名和值
								1.NamedValueInfo namedValueInfo = getNamedValueInfo(parameter);
								Object arg = resolveName(resolvedName.toString(), nestedParameter, webRequest);
					//动态代理增强功能
					2.doInvoke(args)
	5.最后：将所有的数据存放到ModelAndViewContainer中，其中包含模型数据和需要跳转的页面view
	6.最后：处理派发结果，将数据存储到request域对象中。
```

![image-20211229222900622](E:\study\springBoot\img\handlerApapter.png)

#### 5.5.3Servlet API

```
1.参数为这些：WebRequest、ServletRequest、MultipartRequest、 HttpSession、javax.servlet.http.PushBuilder、Principal、InputStream、Reader、HttpMethod、Locale、TimeZone、ZoneId
2.仅仅是解析的时候使用的解析器换了而已。(ServletRequestMethodArgumentResolver)
```

#### 5.5.4复杂参数解析

```
1.参数为以下这些
Map、Model（map、model里面的数据会被放在request的请求域  request.setAttribute）
Errors/BindingResult
RedirectAttributes（重定向携带数据）
ServletResponse（response）
SessionStatus
UriComponentsBuilder
ServletUriComponentsBuilder
2.map类型的参数使用（MapMethodProcessor）来解析参数。最后会返回BindingAwareModelMap()。它是Map也是Model.
	args[i] = this.resolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
		return resolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
			return mavContainer.getModel();
				private final ModelMap defaultModel = new BindingAwareModelMap();
3.Model使用(ModelMethodProcessor)来解析参数。解析过程和Map一样。都会返回BindingAwareModelMap()，底层一样。
4.自定义类型参数使用（ServletModelAttributeMethodProcessor）来解析参数。
	解析参数重要过程：
		//1.创建空的person对象
		attribute = createAttribute(name, parameter, binderFactory, webRequest);
		//2.将请求参数中的数据绑定到javaBean中。
		WebDataBinder binder = binderFactory.createBinder(webRequest, attribute, name);
		//3.WebDataBinder利用自己的Converters（124）将请求参数转换为指定的数据类型，从而封装到javaBean对象中。
		bindRequestParameters(binder, webRequest);
```

#### 5.6json数据响应

```
1.引入依赖spring-boot-starter-web引入了json依赖
2.原理分析：（参数解析完成之后）
	1.this.returnValueHandlers.handleReturnValue(
					returnValue, getReturnValueType(returnValue), mavContainer, webRequest);
		//1.找到RequestResponseBodyMethodProcessor能处理，共有15个解析器		
		HandlerMethodReturnValueHandler handler = selectHandler(returnValue, returnType);
		//2.进行处理
		handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
			//1.使用消息处理器进行写
			writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
				//1.内容协商，浏览器告诉服务器能接收什么类型的参数。
				acceptableTypes = getAcceptableMediaTypes(request);
					//1.底层调用request.getheaders
					String[] headerValueArray = request.getHeaderValues(HttpHeaders.ACCEPT);
				//2.服务器自身能返回的数据类型.能处理person的，返回值是啥种。
				List<MediaType> producibleTypes = getProducibleMediaTypes(request, valueType, targetType);
					//1.遍历当前所有的MessageConverter，看谁支持操作（person）对象，统计处理结果
				//3.通过比较两个，可以得到最终的写回的数据类型。
				mediaTypesToUse.add(getMostSpecificMediaType(requestedType, producibleType));
				//4.查询哪个消息转换器能完成将对象转换为json数据格式的任务。最终找到MappingJackson2HttpMessageConverter这个转换器。
				((GenericHttpMessageConverter) converter).canWrite(targetType, valueType, selectedMediaType) :
						converter.canWrite(valueType, selectedMediaType))
				//5.使用这个转换器进行写出。
				genericConverter.write(body, targetType, selectedMediaType, outputMessage);
					//1.写到outputMessage中
					writeInternal(t, type, outputMessage);
						objectWriter.writeValue(generator, value);
					//2.这其中存在着json数据
					outputMessage.getBody().flush();
```

##### 开启浏览器的内容协商功能。

```xml
    contentnegotiation:
      favor-parameter: true
```

浏览器访问的时候带一个format=json的内容，会将这个解析为浏览器支持的值。增加了一个策略（基于请求参数内容的策略）。

## 6.web开发（基本使用）   

### 6.1视图解析与模板引擎 

```
1.thymeleaf的基础：	
	1、所有thymeleaf的配置值都在 ThymeleafProperties
	2、配置好了 SpringTemplateEngine 
	3、配好了 ThymeleafViewResolver 
	4、我们只需要直接开发页面
```

### 6.2视图解析原理（以/login重定向到/main.html为例子）

```
1.在所有目标方法都执行完毕后，都会返回ModelAndView。
//处理派发结果
2.processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
	//1.进行页面渲染
	render(mv, request, response);
		//1.解析视图名字，得到View对象。
		view = resolveViewName(viewName, mv.getModelInternal(), locale, request);
			//1.遍历5个视图解析器，看哪个能解析。第一个就可以。ContentNegotiatingViewResolver。
			List<View> candidateViews = getCandidateViews(viewName, locale, requestedMediaTypes);
				//1.循环遍历剩余的4个解析器，看哪个能解析。
				View view = viewResolver.resolveViewName(viewName, locale);
			    //因为是一个redircet:/main.html,所以thymeleafViewResolver产生new RedirectView();
		//2.真正渲染页面。
		view.render(mv.getModelInternal(), request, response);
        	renderMergedOutputModel(mergedModel, getRequestToExpose(request), response);
        		//1.获取目标url地址
				String targetUrl = createTargetUrl(model, request);
				//2.进行转发
				sendRedirect(request, response, targetUrl, this.http10Compatible);
```

```
视图解析：
	返回值以forward:开始：new InternalResourceView()
	返回值以redirect:开始：new RedirectView()
	返回值以普通字符串：new ThymeleafView()
```

## 7.web开发（常用功能）

### 7.1拦截器

```
实现思路：
	1.编写一个拦截器实现HandlerInterceptor接口
	2.拦截器进行注册到容器中（实现WebMvcConfigurer的addInterceptors）
	3.指定拦截器的拦截规则。（若是进行拦截所有的话，则需要放行静态资源）
原理：在DispatcherServlet中。
	//1.得到哪个方法能处理请求的时候，也会得到所有的拦截器。
	mappedHandler = getHandler(processedRequest);
	//2.顺序执行PreHandle方法。
	mappedHandler.applyPreHandle(processedRequest, response)
		   //1.若有异常，直接触发AfterCompletion方法，从当前异常之前的那个拦截器的AfterCompletion开始。倒序执行。
            boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
            for (int i = 0; i < this.interceptorList.size(); i++) {
                HandlerInterceptor interceptor = this.interceptorList.get(i);
                if (!interceptor.preHandle(request, response, this.handler)) {
                    triggerAfterCompletion(request, response, null);
                    return false;
                }
                this.interceptorIndex = i;
            }
            return true;
        }
    //3.执行方法
    mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
    //4.倒序执行PostHandle
    mappedHandler.applyPostHandle(processedRequest, response, mv);
    //5.页面渲染结束执行AfterCompletion();
    mappedHandler.triggerAfterCompletion(request, response, null);
```

```
总结：
	preHandler顺序+postHandler倒序+afterCompletion到序。
	preHandler一旦异常，从异常之前不异常的那个开始执行afterCompletion，直接返回。
```

### ![](E:\study\springBoot\img\interceptor)

### 7.2文件上传

```
1.文件上传相关的自动配置类MultipartAutoConfiguration有创建文件上传参数解析器StandardServletMultipartResolver
//1.请求进来使用文件上传解析器判断并重新封装请求request
processedRequest = checkMultipart(request);
//2.参数解析的时候，封装文件内容为MultipartFile
//3.将request中的文件进行封装成为一个Map.
//4.使用FileCopyUtils实现文件流的拷贝
```

### 7.3异常处理

```
1.默认的异常处理规则：
	Spring Boot提供/error处理所有错误的映射
	机器客户端，它将生成JSON响应，其中包含错误，HTTP状态和异常消息的详细信息。
	对于浏览器客户端，响应一个“ whitelabel”错误视图，以HTML格式呈现相同的数据
2.ErrorMvcAutoConfiguration（异常处理自动配置）中的组件
	1.DefaultErrorAttributes id=errorAttributes：定义错误页面中可以包含哪些数据.见getErrorAttributes方法。
	2.BasicErrorController id=basicErrorController：处理默认的/error的请求。
		页面响应：errorHtml中的new ModelAndView("error", model);
		json响应： ResponseEntity<Map<String, Object>>的new ResponseEntity<>(body, status);
	3.id=error的defaultErrorView：默认的白页。
	4.BeanNameViewResolver：按照返回的View视图名作为id去容器中寻找View对象。
	5.DefaultErrorViewResolver id=conventionErrorViewResolver：如果发生错误会以HTTP错误响应码作为试图页的地址，找到真正的页面。
3.异常处理步骤：（除0异常）
	1.执行目标方法，目标方法在运行期间有任何异常都会被catch，而且标志当前请求结束。dispatchException来处理异常。
	2.进行视图解析流程。
	processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
		//1.异常处理过程。处理结束返回ModelAndView
		mv = processHandlerException(request, response, handler, exception);
			//1.遍历handlerExceptionResolvers，看谁能处理。1+3组合
                if (this.handlerExceptionResolvers != null) {
                for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
                    exMv = resolver.resolveException(request, response, handler, ex);
                    if (exMv != null) {
                        break;
                    }
                }
            }
            //2.DefaultErrorAttributes先来处理异常。把异常信息保存到rrequest域，并且返回null。
            //3.默认没有任何人能处理异常，所以异常会被抛出
   3.如果没有人能处理异常，底层发/error请求，被BasicErrorController处理。
   4.解析错误视图，遍历所有的ErrorViewResolver，最后找到DefaultErrorViewResolver。
   5.DefaultErrorViewResolver的作用是把响应状态码作为错误页的地址，error/500.html 
   6.模板引擎最终响应这个页面 error/500.html 
```

```java
@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		Map<String, Object> errorAttributes = getErrorAttributes(webRequest, options.isIncluded(Include.STACK_TRACE));
		if (!options.isIncluded(Include.EXCEPTION)) {
			errorAttributes.remove("exception");
		}
		if (!options.isIncluded(Include.STACK_TRACE)) {
			errorAttributes.remove("trace");
		}
		if (!options.isIncluded(Include.MESSAGE) && errorAttributes.get("message") != null) {
			errorAttributes.remove("message");
		}
		if (!options.isIncluded(Include.BINDING_ERRORS)) {
			errorAttributes.remove("errors");
		}
		return errorAttributes;
	}
	private Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
		Map<String, Object> errorAttributes = new LinkedHashMap<>();
		errorAttributes.put("timestamp", new Date());
		addStatus(errorAttributes, webRequest);
		addErrorDetails(errorAttributes, webRequest, includeStackTrace);
		addPath(errorAttributes, webRequest);
		return errorAttributes;
	}
```

```java
	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
	public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		HttpStatus status = getStatus(request);
		Map<String, Object> model = Collections
				.unmodifiableMap(getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.TEXT_HTML)));
		response.setStatus(status.value());
		ModelAndView modelAndView = resolveErrorView(request, response, status, model);
		return (modelAndView != null) ? modelAndView : new ModelAndView("error", model);
	}

	@RequestMapping
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		HttpStatus status = getStatus(request);
		if (status == HttpStatus.NO_CONTENT) {
			return new ResponseEntity<>(status);
		}
		Map<String, Object> body = getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
		return new ResponseEntity<>(body, status);
	}
```

### 7.4自定义异常处理

```
1.自定义错误页。
	error/404.html error/5xx;有精确的错误状态码页面就精确匹配，没有就4xx.html，如果都没有就触发白页。
2.@ControllerAdvice+@ExceptionHandler处理全局异常。底层是ExceptionHandlerExceptionResolver 
3.@ResponseStatus+自定义异常：底层是ResponseStatusExceptionResolver，
	将responsestatus注解的信息底层调用response.sendError(statusCode,resolverdReason);tomcat发送的/error请求。
4.Spring底层的异常处理：底层是DefaultHandlerExceptionResolver处理的。
  底层是：response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());tomcat发送的/error请求。
5.自定义实现HandlerExceptionResolver。
6.ErrorViewResolver一般不重写。他能处理的情况。
	1.response.sendError()
	2.没人能处理异常请求。tomcat底层会发response.sendError。
	3.最后都是到了BasicErrorController这边处理。
```

### 7.5原生组件注入

```
1. 	@ServletComponentScan("com.example.demo4")：指定原生Servlet组件都放在哪里
Servlet组件：
    + @WebServlet("/my")：
Filter组件：
    + @WebServlet("/css/*")：
Listen组件：
	+ @WebListener
```

```java
package com.example.demo4.config;

import com.example.demo4.filter.MyFilter;
import com.example.demo4.listen.MyListen;
import com.example.demo4.servlet.MyServlet;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration(proxyBeanMethods = true)//保证容器中是单实例的
public class MyRegistConfig {

    @Bean
    public ServletRegistrationBean myServlet(){
        MyServlet myServlet = new MyServlet();

        return new ServletRegistrationBean(myServlet,"/my","/my02");
    }


    @Bean
    public FilterRegistrationBean myFilter(){

        MyFilter myFilter = new MyFilter();
//        return new FilterRegistrationBean(myFilter,myServlet());
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(myFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/my","/css/*"));
        return filterRegistrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean myListener(){
        MyListen myListen = new MyListen();
        return new ServletListenerRegistrationBean(myListen);
    }
}
```

```
DispatcherServlet的注册：DispatcherServletAutoConfiguration	
    1.容器中自动配置了DispatcherServlet属性绑定到WebMvcProperties;对应的配置文件配置项是spring.mvc。
    2.通过ServletRegistrationBean<DispatcherServlet> 把DispatcherServlet配置进来。
    3.默认映射的是/路径。
多个Servlet的精确匹配原则。
```

### 7.6嵌入式Servlet容器

```
1.tomcat,netty,undertow
2.原理：
	1.SpringBoot应用启动发现当前应用是web应用，导入web-starter——>导入tomcat
	2.web容器会创建一个web版的IOC容器ServletWebServerApplicationContext
	3.ServletWebServerApplicationContext启动的时候寻找 ServletWebServerFactory（Servlet 的web服务器工厂——>Servlet 的web服务器）。
	SpringBoot底层默认有很多的WebServer工厂（ServletWebServerFactoryConfiguration内创建Bean），如：
        TomcatServletWebServerFactory
        JettyServletWebServerFactory
        UndertowServletWebServerFactory	
	底层直接会有一个自动配置类ServletWebServerFactoryAutoConfiguration。
	ServletWebServerFactoryAutoConfiguration导入了ServletWebServerFactoryConfiguration（配置类）。
	ServletWebServerFactoryConfiguration根据动态判断系统中到底导入了那个Web服务器的包。（默认是web-starter导入tomcat包），
	容器中就有 TomcatServletWebServerFactory，TomcatServletWebServerFac
	tory创建出Tomcat服务器并启动；
	TomcatWebServer 的构造器拥有初始化方法initialize——this.tomcat.start();
	内嵌服务器，与以前手动把启动服务器相比，改成现在使用代码启动（tomcat核心jar包存在）。
```

### 7.7定制化

```
常用：1.web应用实现WebMvcConfigurer,即可定制化web功能。
原理：@EnableWebMvc+WebMvcConfigurer实现全面接管SpringMVC
	1.WebMvcAutoConfiguration 默认的SpringMVC自动配置类。
	2.一旦使用@EnableWebMvc。会import进来DelegatingWebMvcConfiguration.class
	3.DelegatingWebMvcConfiguration：只会保证SpringMvc最基本的使用。
	public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport
	4.WebMvcAutoConfiguration上面存在一个@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)故失效。
springMVC的大概流程：
	1.导入场景启动器
	2.导入xxxAutoConfiguration
	3.导入xxx组件
	4.绑定xxxProperties配置文件
	5.修改配置文件。
```

## 8.数据访问

### 8.1数据源（JDBC场景）

```
1.导入jdbc场景启动器，会自动添加HikariDataSource和spring-jdbc
2.分析JDBC的自动配置：
	DataSourceAutoConfiguration:数据源的自动配置
        修改数据源的自动配置：spring.datasource
        默认的jdbc使用的连接池是HikariDataSource
	DataSourceTransactionManagerAutoConfiguration:事务管理器的自动配置
	JdbcTemplateAutoConfiguration：JdbcTemplate的自动配置，可以用来对数据进行CRUD操作
		可以使用spring.jdbc来修改JdbcTemplate
	JndiDataSourceAutoConfiguration：JNDI的自动配置
	XADataSourceAutoConfiguration：分布式事务相关
3.yaml配置内容
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/db_account
    username: root
    password: 123456
    driver-class-name: com.mysql.jdbc.Driver
```

### 8.2Druid数据源的使用

```
1.自定义的实现方式（不推荐，太复杂）
2.导入starter的方式
```

1.导入pom依赖

```
  <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.17</version>
  </dependency>
```

2.yaml中配置内容

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/mp?serverTimezone=UTC&useSSL=false
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver

    druid:
      aop-patterns: com.example.demo4.*
      filters: stat,wall

      stat-view-servlet:
        enabled: true
        login-username: admin
        login-password: 123456
        reset-enable: false
      web-stat-filter:
        enabled: true
        url-pattern: /*
        exclusions: "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*"
      filter:
        stat:
          slow-sql-millis: 1000
          log-slow-sql: true
        wall:
          enabled: true
```

3.自动配置内容解析

```
1.配置Druid的前缀：spring.datasource.druid
DruidSpringAopConfiguration：监控SpringBean的
DruidStatViewServletConfiguration：监控页的配置
DruidWebStatFilterConfiguration：web监控的配置
DruidFilterConfiguration：Druid的filter配置
```

### 8.3mybatis配置

1.导入starter依赖

```
      <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.4</version>
        </dependency>
```

2.自动配置内容

```
全局配置文件
SqlSessionFactory:自动配置好
SqlSession：自动配置了SqlSessionTemplate组合了SqlSession
因为导入了AutoConfiguredMapperScannerRegistrar.class，所以我们写的方法上的Mapper注解都会被自动扫描进来。
```

3.mybatis配置文件

```
两个重要的配置文件，全局配置文件和映射的xml文件。
全局配置文件可以不写，因为mybatis中的configuration都会有配。
也可以不写映射的xml文件，因为可以使用注解完成配置。
```

4.mybatis的使用过程

```
1.导入starter
2.编写mapper接口，@mapper注解（MapperScan可以配置在springboot的启动类中，简化@Mapper注解的使用）
3.sql映射文件并绑定mapper(复杂sql)
4.sql简单直接用注解
5.yml中指定2个配置文件的位置
```

```java
@Mapper
public interface CityMapper {
    @Select("select * from city where id=#{id}")
    public City getById(Long id);
    public void insert(City city);
}
```

### 8.4mybatis-plus

```
● MybatisPlusAutoConfiguration 配置类，MybatisPlusProperties 配置项绑定。mybatis-plus：xxx 就是对mybatis-plus的定制
● SqlSessionFactory 自动配置好。底层是容器中默认的数据源
● mapperLocations 自动配置好的。有默认值。classpath*:/mapper/**/*.xml；
	任意包的类路径下的所有mapper文件夹下任意路径下的所有xml都是sql映射文件。  建议以后sql映射文件，放在 mapper下
● 容器中也自动配置好了SqlSessionTemplate
● @Mapper标注的接口也会被自动扫描；建议直接 @MapperScan("com.atguigu.admin.mapper") 批量扫描就行
```

### 8.5redis

```
● RedisAutoConfiguration 自动配置类。RedisProperties 属性类 --> spring.redis.xxx是对redis的配置
● 连接工厂是准备好的。客户端有：LettuceConnectionConfiguration、JedisConnectionConfiguration
● 自动注入了RedisTemplate<Object, Object> ： xxxTemplate；
● 自动注入了StringRedisTemplate；k：v都是String
● key：value
● 底层只要我们使用 StringRedisTemplate、RedisTemplate就可以操作redis
```

## 9.单元测试

```
Junit5由3个模块组成。
JUnit5=Junit Platform +JUnit Jupiter+Junit Vintage
```

单元测试使用

```
1.测试类上加@SpringBootTest注解，然后在方法上加@Test。
2.常用注解
● @Test :表示方法是测试方法。但是与JUnit4的@Test不同，他的职责非常单一不能声明任何属性，拓展的测试将会由Jupiter提供额外测试
● @ParameterizedTest :表示方法是参数化测试，下方会有详细介绍
● @RepeatedTest :表示方法可重复执行，下方会有详细介绍
● @DisplayName :为测试类或者测试方法设置展示名称
● @BeforeEach :表示在每个单元测试之前执行
● @AfterEach :表示在每个单元测试之后执行
● @BeforeAll :表示在所有单元测试之前执行
● @AfterAll :表示在所有单元测试之后执行
● @Tag :表示单元测试类别，类似于JUnit4中的@Categories
● @Disabled :表示测试类或测试方法不执行，类似于JUnit4中的@Ignore
● @Timeout :表示测试方法运行如果超过了指定时间将会返回错误
● @ExtendWith :为测试类或测试方法提供扩展类引用
3.断言测试：前面断言失败，后面的代码都不会执行
    1.简单断言：用来对单个值进行简单的验证。
    2.数组断言：通过 assertArrayEquals 方法来判断两个对象或原始类型的数组是否相等
    3.组合断言
    4.异常断言
    5.超时断言
    6.快速失败:通过 fail 方法直接使得测试失败
4.前置条件:而不满足的前置条件只会使得测试方法的执行终止。
5.嵌套测试:@Nested可以将相关的测试方法组织在一起。
6.参数化测试：使得用不同的参数多次运行测试成为了可能
    @ValueSource: 为参数化测试指定入参来源，支持八大基础类以及String类型,Class类型
    @NullSource: 表示为参数化测试提供一个null的入参
    @EnumSource: 表示为参数化测试提供一个枚举入参
    @CsvFileSource：表示读取指定CSV文件内容作为参数化测试入参
    @MethodSource：表示读取指定方法的返回值作为参数化测试入参(注意方法返回需要是一个流)
```

```java
@Test
@DisplayName("simple assertion")
public void simple() {
     assertEquals(3, 1 + 2, "simple math");
     assertNotEquals(3, 1 + 1);
     assertNotSame(new Object(), new Object());
     Object obj = new Object();
     assertSame(obj, obj);
     assertFalse(1 > 2);
     assertTrue(1 < 2);
     assertNull(null);
     assertNotNull(new Object());
}
@Test
@DisplayName("array assertion")
public void array() {
 assertArrayEquals(new int[]{1, 2}, new int[] {1, 2});
}
@Test
@DisplayName("assert all")
public void all() {
 assertAll("Math",
    () -> assertEquals(2, 1 + 1),
    () -> assertTrue(1 > 0)
 );
}
@Test
@DisplayName("异常测试")
public void exceptionTest() {
    ArithmeticException exception = Assertions.assertThrows(
           //扔出断言异常
            ArithmeticException.class, () -> System.out.println(1 % 0));

}
@Test
@DisplayName("超时测试")
public void timeoutTest() {
    //如果测试方法时间超过1s将会异常
    Assertions.assertTimeout(Duration.ofMillis(1000), () -> Thread.sleep(500));
}
@Test
@DisplayName("fail")
public void shouldFail() {
 fail("This should fail");
}
```

```java
@DisplayName("前置条件")
public class AssumptionsTest {
 private final String environment = "DEV";
 
 @Test
 @DisplayName("simple")
 public void simpleAssume() {
    assumeTrue(Objects.equals(this.environment, "DEV"));
    assumeFalse(() -> Objects.equals(this.environment, "PROD"));
 }
 
 @Test
 @DisplayName("assume then do")
 public void assumeThenDo() {
    assumingThat(
       Objects.equals(this.environment, "DEV"),
       () -> System.out.println("In DEV")
    );
 }
}
```

```java
@DisplayName("A stack")
class TestingAStackDemo {

    Stack<Object> stack;

    @Test
    @DisplayName("is instantiated with new Stack()")
    void isInstantiatedWithNew() {
        new Stack<>();
    }

    @Nested
    @DisplayName("when new")
    class WhenNew {

        @BeforeEach
        void createNewStack() {
            stack = new Stack<>();
        }

        @Test
        @DisplayName("is empty")
        void isEmpty() {
            assertTrue(stack.isEmpty());
        }

        @Test
        @DisplayName("throws EmptyStackException when popped")
        void throwsExceptionWhenPopped() {
            assertThrows(EmptyStackException.class, stack::pop);
        }

        @Test
        @DisplayName("throws EmptyStackException when peeked")
        void throwsExceptionWhenPeeked() {
            assertThrows(EmptyStackException.class, stack::peek);
        }

        @Nested
        @DisplayName("after pushing an element")
        class AfterPushing {

            String anElement = "an element";

            @BeforeEach
            void pushAnElement() {
                stack.push(anElement);
            }

            @Test
            @DisplayName("it is no longer empty")
            void isNotEmpty() {
                assertFalse(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when popped and is empty")
            void returnElementWhenPopped() {
                assertEquals(anElement, stack.pop());
                assertTrue(stack.isEmpty());
            }

            @Test
            @DisplayName("returns the element when peeked but remains not empty")
            void returnElementWhenPeeked() {
                assertEquals(anElement, stack.peek());
                assertFalse(stack.isEmpty());
            }
        }
    }
}
```

```java
@ParameterizedTest
@ValueSource(strings = {"one", "two", "three"})
@DisplayName("参数化测试1")
public void parameterizedTest1(String string) {
    System.out.println(string);
    Assertions.assertTrue(StringUtils.isNotBlank(string));
}


@ParameterizedTest
@MethodSource("method")    //指定方法名
@DisplayName("方法来源参数")
public void testWithExplicitLocalMethodSource(String name) {
    System.out.println(name);
    Assertions.assertNotNull(name);
}

static Stream<String> method() {
    return Stream.of("apple", "banana");
}
```

## 9指标监控



## 10原理解析