Java's approach to asynchronous programming differs from C#'s **Task-based Asynchronous Pattern (TAP)**. In Spring Boot, asynchronous programming is typically handled using Java's **CompletableFuture** or reactive programming with **Project Reactor** (`Mono` and `Flux`). Here's how you can achieve similar functionality to your C# example:

### 1. **Using `CompletableFuture`** (Standard Java Async API)
In Java, you can use `CompletableFuture` to handle asynchronous tasks. Here's how it would look:

#### Java Implementation:
```java
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    public CompletableFuture<Integer> methodAsync(int arg0, int arg1) {
        return CompletableFuture.supplyAsync(() -> helperMethod(arg0, arg1));
    }

    private int helperMethod(int arg0, int arg1) {
        // Simulate some processing
        return arg0 + arg1;
    }
}
```

In this example:
- `CompletableFuture.supplyAsync` is used to run the task asynchronously.
- The `methodAsync` function is non-blocking and returns a `CompletableFuture<Integer>`.

#### Consuming the Method:
```java
@RestController
@RequestMapping("/api")
public class AsyncController {

    private final AsyncService asyncService;

    public AsyncController(AsyncService asyncService) {
        this.asyncService = asyncService;
    }

    @GetMapping("/add")
    public CompletableFuture<Integer> add(@RequestParam int arg0, @RequestParam int arg1) {
        return asyncService.methodAsync(arg0, arg1);
    }
}
```

### 2. **Using Spring's `@Async` Annotation**
Spring Boot simplifies asynchronous programming with the `@Async` annotation.

#### Configuration:
1. Enable asynchronous processing in your Spring Boot application:
   ```java
   @Configuration
   @EnableAsync
   public class AsyncConfig {
   }
   ```

2. Update the service to use `@Async`:
   ```java
   import org.springframework.scheduling.annotation.Async;
   import org.springframework.stereotype.Service;
   import java.util.concurrent.CompletableFuture;

   @Service
   public class AsyncService {

       @Async
       public CompletableFuture<Integer> methodAsync(int arg0, int arg1) {
           int result = helperMethod(arg0, arg1);
           return CompletableFuture.completedFuture(result);
       }

       private int helperMethod(int arg0, int arg1) {
           return arg0 + arg1;
       }
   }
   ```

#### Consuming the Method:
The controller implementation remains the same as above.

### 3. **Reactive Programming with Project Reactor**
For more advanced use cases or reactive APIs, you can use `Mono` from Project Reactor, which integrates seamlessly with Spring WebFlux.

#### Implementation:
```java
import reactor.core.publisher.Mono;

@Service
public class ReactiveService {

    public Mono<Integer> methodAsync(int arg0, int arg1) {
        return Mono.fromSupplier(() -> helperMethod(arg0, arg1));
    }

    private int helperMethod(int arg0, int arg1) {
        return arg0 + arg1;
    }
}
```

#### Consuming the Method:
```java
@RestController
@RequestMapping("/api")
public class ReactiveController {

    private final ReactiveService reactiveService;

    public ReactiveController(ReactiveService reactiveService) {
        this.reactiveService = reactiveService;
    }

    @GetMapping("/add")
    public Mono<Integer> add(@RequestParam int arg0, @RequestParam int arg1) {
        return reactiveService.methodAsync(arg0, arg1);
    }
}
```

### Summary:
- **`CompletableFuture`**: Closest analog to `Task` in C#.
- **Spring `@Async`**: Simplifies asynchronous method calls and allows configuration.
- **Reactive Programming (`Mono` and `Flux`)**: Preferred for non-blocking, event-driven architectures.

For most use cases, `CompletableFuture` or `@Async` should suffice, but if you're working with a reactive system, then `Mono` and `Flux` are recommended.