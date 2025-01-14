# Null Analysis

- [Enabling Null Analysis in Your Java Project: Why It Matters and How to Do It](https://nikhilsomansahu.medium.com/enabling-null-analysis-in-your-java-project-why-it-matters-and-how-to-do-it-50ef9ede9348)

In VS Code this takes the form of a popup dialog on the bottom right on start up once Java has been initialized. You get the opportunity to enable or disable it based on the following message:

"Null annotation types have been detected in the project. Do you wish to enable null analysis for this project?"

In C# and .NET we use the TAP (async await) in order to execute asynchronous code. This is considered best practice approach when building .NET Core Web API projects. Take a look at the following code:

```csharp
public async Task<int> MethodAsync(int arg0, int arg1)
{
    int result = await HelperMethods.MethodTask(arg0, arg1);
    return result;
}
```

How does one achieve the same end using Java Spring Boot?