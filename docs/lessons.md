## Auto Reload (Compile)

- [Use `springboot-dev-tools`](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33523000#notes) to activate quick reloads of your code when changing. This may only work in IntelliJ Ultimate.

## Dependency Injection

[Use spring profiles to further increase dependency injection flexibility](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33434486). Use the [default profile when there are no active profiles](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33434490).

## Spring Bean Life Cycle
See the [Spring Bean Lifecycle](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33434500#notes) and [the Demo](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33434506#notes). Generally you don't need to use the hooks into the lifecycle. There are a number of annotations and Bean Post Processors. Spring also has around 14 'Aware' interfaces.

## Project Lombock

[Set up Project Lombock when creating a new project](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/36089976#notes). In `IntelliJ` you need to add Lombock as a plugin. There are some settings that need to be changed for example: 'Enable annotation processing'. See:
- `@Getter`
- `@Setter`
- `@ToString`
- `@EqualsAndHashCode`
- `@Data` - Combines all of the above annotations to generate getters, setters, string outputs and equality.
- `@NoArgsConstructor`
- `@RequiredArgsConstructor`
- `@Builder` - Generate a builder (see builder pattern) for your entities.
- `@Slf4j`

[Lombock works by hooking into the Annotation Processor API](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33483598#notes). IntelliJ and Visual Studio work well with Project Lombock.
- [Creating a POJO will generate the code in a "target" directory](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33483600#notes)
- [Generate builders for your entities using Lombock](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33483602#notes)
- [Use the `@AllArgsConstructor` to generate a constructor with parameters and initializations for injected services](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33483602#notes)
- [Use `@Slf4j`](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33483610#notes) to add logging to your controllers
- [Delombok](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33483614#notes) using `IntelliJ` to end auto generation and move forward manually. Would not be a common use case. There if you should need it. 


## Spring MVC Controllers (REST APIs)

- `@RestController` - will [apply REST to your API](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33522686#notes) and return JSON (rather than HTML). Also see `@RequestMapping` annotation for your controller methods.
- [HTTP Client](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33522752#notes) - in IntelliJ is a very similar implementation to the VS Code plugin you use to send HTTP requests and responses using an `.http` file.

### Use the following CRUD approaches:
- [Get a beer by its ID](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33522998#notes). Take a deeper look at `@RequestMapping` (at the class and method level).
- [Create a new beer (POST)](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33523004#notes). Covers `@PostMapping` on the controller and the creating of the service and repostory code. When creating a resource its considered best practice to return a [`Location` header in order to point the client to the newly created resource](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33523010#notes).
- [Modify (PUT) a beer](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33575750#notes).

- [Delete a beer](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33579134#notes)
- [PATCH](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33579360#notes) requires some specialized logic so worth taking a look at how properties (fields) are modified if they exist in the payload. See `@PatchMapping` and `@REquestBody`.

## Testing with MockMVC, Mockito and JUnit

Spring MVC Controllers are probably the most tricky to test properly. Controllers have a high degree of integration with the Spring MVC Framework. JUnit tests are not sufficient to test this framework interaction. [Spring Mock MVC](https://docs.spring.io/spring-framework/reference/testing/mockmvc.html) is a testing environment specifically designed for testing Spring MVC Controllers. These are technically integration tests since the tests can be run with or without the Spring Context. Test Splices create a MockMVC environment. See `@WebMvcTest`.

[Mockito](https://site.mockito.org/) is by far the most popular mocking framework for testing Java. [The overview video for this section can be accessed here](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33611234#notes).

### Test GET controller and expect a response of...
[Watch here](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33611848#notes) and note that `@WebMvcTest` is used instead of `@SpringBootTest`. Pass the controller under test to the annotation. Looks like this:

```java
@WebMvcTest(BeerController.class)
class BeerControllerTest {
    ...
}
```
There is some discussion of how to use `@MockBean` but this has been deprecated since Spring Boot 3.4.0+. It's now `@MockitoBean`. [Note there are some static imports that do not always auto generated](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33611848#notes). This video explains how to peform a test against a GET and test an expectation.

- [To test for response content, content type](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33612554#notes) use `.andExpect(..)`. Also use `given(...).willReturn(...)`.
- Use [Jayway JsonPath for reading JSON documents](https://github.com/json-path/JsonPath). Use this to peform assertions against JSON objects coming back from MockMVC. [Explained in this video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33612558#notes). So for instance one might want to know that a field in the JSON response object exists and has a specific value.

### Other MVC Tests

- List Beers - [The test implementation](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33612774#notes) checks for response status, content type, and the length of items expected.
- Create new beer - Use [Jackson's](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/jackson.html) `ObjectMapper` to [serialize a Beer object to a JSON string](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33618822#notes). Be sure to use Spring Boot's Jackson implementation.
- [Update beer](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33635080#notes) uses `verify` and checks that a status of `No Content` is returned.
- [Delete Beer](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33635734#notes) uses `verify` and tests that a `No Content` is returned. In addition an "Argument Captor" (`@Captor`) is used in conjunction with `assertThat` and ensures the correct ID is being parsed out of the URL and sent to the beer service.
- [Patch Beer](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33649136#notes) uses `@Captor` and uses Jackson with a `HashMap` to build up a simple JSON object which sends only the properties we want to test are being patched.

### DRY approach for URL mappings

[See lecture](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33650534#notes) and [next lecture](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33650878#notes) - Two solutions are considered:
- Externalize to property and set at runtime (usually not necessary)
- Define as a constant and reuse - Simple and efficient.

[This lecture looks at using URI Builder to build up a path](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33650962#notes)

## Exception Handling

REST API should return HTTP status codes rather than exception stack traces. [This video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33663806#notes) runs through the status codes and an overview. See the `DefaultHandlerExceptionResolver` and a number of Spring standard exceptions. See also: @ExceptionHandler, @ResponseStatus, @ControllerAdvice annotations. Implement the `AbstractHandlerException` resolver for full control over response and use the `ResponseStatusException.class` to throw a custom HTTP status and message. Video discusses responding with a Whitelabel error page for HTML requests and how you can turn certain exception behaviors in the `application.properties` file.

[This video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33674284#notes) discusses how to throw a custom exception and then set up a test that will simulates throwing an exception using Mockito. [See `@ExceptionHandler` annotation](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33674322#notes). Use `@ControllerAdvice` on an `ExceptionController` to [handle exceptions globally](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33674506#notes). The example uses this handler to return a "Not Found" response.

Frequently you may want [simplify things](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33674546#notes) by using `@ResponseStatus` annotation on a custom exception rather than using the annotated `ExceptionController`.

One may not want the "Not Found" logic to live in the service (or Controller layer). [Fix this using](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33674560#notes) the `Optional<T>` and `Optional.of()` and `.orElseThrow(NotFoundException::new)`.

## DTOs (Why?)

The needs of the consumers are [often different](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33663812#notes) from the needs of persistence. Don't leak data to the client tier. DTOs can be optimized for JSON serialization and deserialization. Type converters should be used through Converters. Prefer [MapStruct](https://mapstruct.org/documentation/spring-extensions/reference/html/) as the code generator. It works like Lombock via annotation processed during code compile. It has good Spring integration.

Use **MapStruct** to convert between DTOs and Entities.

## Spring Data JPA (Java Persistence API)

Spring Data JPA is an abstraction built upon Hibernate. Hibernate is an implementation of JPA. JPA is a standard. Hibernate is an implemenation. The goal is to make database persistence easier.

See `pom.xml` for `spring-boot-starter-data.jpa` and `h2` for dependencies for data persistence during development.

### Entities

[Entities are annotated](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33674952#notes) with the `@Entity` stereotype and note the `@Id` and `@Version` field annotation. It is best not to use `@Data` annotation with JPA entities... rather use `@Getter` and `@Setter`.

When using a `UUID` as an ID is [best to use](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33721942#notes) the `@GeneratedValue(generator = "UUID")` and `@UuidGeneator()` annotations.

> `@GenericGenerator(name = "UUID", strategy = "...")` is deprecated in Spring Boot 3.40+ in favor of `@UuidGeneator()`.

Use the `@Column` for more control of how the SQL is created to generate the entity table.

### Repositories and Services

Repositories always extend `JpaRepository<T>. This allows for paging and sorting too. The [video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33721944?start=15#overview) explains the methods available via these generic repositories.

Use another Spring Boot test splice to do JPA testing. The video shows the use of the `@DataJpaTest` annotation. This loads a minimal database Context to the H2 database and the test verifies that repository saves correctly to the database. The test does not load the Spring context so attempting to access any other components will result in errors.

#### MapStruct
[This video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33721954#overview) explains how to configure [MapStruct](https://mapstruct.org/) using `annotationProcessorPaths` however `annotationProcessorPaths` does not seem to be required as you have it working without them. [This video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33721956#overview) shows basic usage.

> Note: the config as provided on course does not work, [this is how you got it to work](https://www.youtube.com/watch?app=desktop&v=7UC3ZjQnric). Look in `target/generated-sources`. Somewhere down the chain will be the `mappers` folder with the generated mappers.

#### JPA Service

[This video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33729204#notes) explains how to create a service using `@Service`, `@Primary`, `@RequiredArgsConstructor`. T[he next one](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33729206#overview) uses the Stream API to list items and uses `Optional.ofNullable()` and `orElse()`.

#### Integration Tests

[This video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33729232#notes) tests the interaction between the service and the controller. Since this is an integration test, use `@SpringBootTest` which loads the entire Spring context. `@Transactional` and `@Rollback` must be used to ensure that the database transaction is rolled back after each test completes. [This video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33729234#notes) tests the controller for expected Exceptions.

The next set of videos create integration tests for all the CRUD operations to check for response status, headers, etc.

- [Save (new)](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33729236#notes)
- [Update (by ID)](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33729240#notes)
- [Update (NOT FOUND)](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33729242#notes). Asserts that NOT FOUND is correctly thrown using `Optional<T>`.
- [Delete](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33729248#notes)
- [Delete (NOT FOUND)](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33729252#notes) - Asserts that NOT FOUND is correctly thrown.


## Data Validation

#### Controller and Entity Validation

Validation is done using the [Java Bean Validation API](https://docs.spring.io/spring-framework/reference/core/validation/beanvalidation.html). Here's a [reference](https://docs.oracle.com/javaee/6/tutorial/doc/gircz.html) and this one looks at [advanced bean validation concepts](https://www.ibm.com/docs/en/was-nd/8.5.5?topic=validation-bean#cdat_beanval__title__5).

[This video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33850108#notes) explains how the dependencies are added to your Spring project.

- [Bind validation to the Entity](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33850124#notes) and use `@Validated` for the incoming `@RequestBody` DTO.
- [Implement a custom validation handler using](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33850168#notes) `@ControllerAdvise` and `@ExceptionHandler` as a catch all for all uncaught exceptions and then [customize the data returned in the Bad Request response](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33850232#notes).
- [This video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33851452#notes) and [this video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33851458#notes) explains how to handle a `TransactionSystemException` using the `CustomErrorController`. See `MockMvcBuilders.webAppContextSetup(wac).build()` to get a reference to the full web context in your tests. Finally, the error [message is customized](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33851464#notes) to provide the reason in a more readable manner.

> Constraints should exist and be consistent on both the DTO and the Entity.

#### JPA Validation

[This video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33851440#notes) explains the process. An important consideration is the use of `repository.flush()` when writing the tests in order to ensure that the test doesn't end before the operation has completed.

#### Database Constraint Validation

[This video](https://www.udemy.com/course/spring-framework-6-beginner-to-guru/learn/lecture/33851448#notes) demonstrates the `@Column` and `@Size` annotations on an entity to ensure that a failed validation occurs before Hibernate attempts to persist the data. A test ensures that the correct validation has been thrown.