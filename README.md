# OpenAPI Generator for the generic-cpp-client library

## What's OpenAPI
The goal of OpenAPI is to define a standard, language-agnostic interface to REST APIs which allows both humans and computers to discover and understand the capabilities of the service without access to source code, documentation, or through network traffic inspection.
When properly described with OpenAPI, a consumer can understand and interact with the remote service with a minimal amount of implementation logic.
Similar to what interfaces have done for lower-level programming, OpenAPI removes the guesswork in calling the service.

Check out [OpenAPI-Spec](https://github.com/OAI/OpenAPI-Specification) for additional information about the OpenAPI project, including additional libraries with support for other languages and more.

## Build
```
git clone https://github.com/ipariparipa/generic-cpp-client.git
cd generic-cpp-client
mvn package
```

## Example
```
java -cp ~/.local/bin/openapi-generator-cli-7.25.0.jar:"generic-cpp-client-openapi-generator-1.0.0.jar" org.openapitools.codegen.OpenAPIGenerator \
generate -g generic-cpp-client -i ../openapi/ollama.yaml -o ./ollama-client \
--additional-properties=cppNamespace=oa,defaultApiName=OllamaClient,modelPackage=ollama_model,apiPackage=ollama_api,forceLongInteger=true
``
