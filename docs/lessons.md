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