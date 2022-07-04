# Structurizr AutoConfigure with Spring Boot

[![License](https://img.shields.io/badge/License-Apache_2.0-green.svg)](https://opensource.org/licenses/Apache-2.0)
[![Maven Central](https://img.shields.io/maven-central/v/io.cloudflight.structurizr/structurizr-autoconfigure.svg?label=Maven%20Central)](https://search.maven.org/artifact/io.cloudflight.structurizr/structurizr-autoconfigure)

The purpose of this small utility on top of [the Structurizr Client for Java](https://github.com/structurizr/java) and its
[Export Library](https://github.com/structurizr/export) is to provide a bit more structure especially for bigger workspaces.

If you follow the instructions of the default library, you easily end up with classes with 100s of lines of code without any
structure. We use Spring Boot here in order to

* cluster your C4 components to chunks of reasonable size
* separate model and view-generation from each other and ensure that views are only created after the model is fully initialized
* provide helper-code which allows to export your workspace either to Structurizr Web or to PlantUML files using [C4-PlantUML](https://github.com/plantuml-stdlib/C4-PlantUML).

## The Structurizr Workspace

This will give you a step-by-step tutorial how to embed this library:

### Embed the library

First thing you need to do is to
add [this library](https://search.maven.org/artifact/io.cloudflight.structurizr/structurizr-autoconfigure) to your
project. With Gradle this is as easy as adding that to your `build.gradle`:

````groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.cloudflight.structurizr:structurizr-autoconfigure:1.0.0")
}
````

### Configure the application:

In your folder `src/main/resources` create a file called `application.yml`.

```yaml
structurizr:
  workspace:
    name: My project name
    description: My project description
```

### Create the main class

We recommend to use Kotlin to create those architecture models.

Last step is to create your main class, it's as easy as that, simply start the Spring Boot application.

```kotlin
package io.cloudflight.architecture

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Architecture

fun main(args: Array<String>) {
    SpringApplication.run(Architecture::class.java)
}
```

### Fill your model

Then you can create Spring Components to populate your model. The Structurizr classes `Workspace` and `Model` are
available in the `ApplicationContext`,
thus can be injected. Inside your `src`-folder, create a file called `Personas.kt`:

```kotlin
@Component
class Personas(model: Model) {
    val customer = model.addPerson("Customer", "Wants to buy articles");
    val admin = model.addPerson("Admin", "Maintains the system");
}
```

You might then inject those components into other components:

```kotlin
@Component
class WebShop(model: Model, personas: Personas) {

    val webShop = model.addSoftwareSystem("WebShop", "") {
        usedBy(personas.admin, "maintains the articles")
        usedBy(personas.customer, "buys articles", Technology.Browser)
    }
}
```

You might noticed that we connected personas already with our webshop software system. Populate your model as described in the
[official library](https://github.com/structurizr/java).

### Create Views

Implement the interface `ViewProvider` to create views as soon as your model has been created.

```kotlin
@Component
class WebShop(model: Model, personas: Personas) {

    val webShop = model.addSoftwareSystem("WebShop", "") {
        usedBy(personas.admin, "maintains the articles")
        usedBy(personas.customer, "buys articles", Technology.Browser)
    }

    override fun createViews(viewSet: ViewSet) {
        viewSet.createContainerView(webShop, "webshop", "WebSatalog Containers").addAllContainersAndInfluencers()
    }
}
```

## Configure the Export

Now that we have the model and the view, we should export and/or render it. There are two built-in options here, but you
are free to define your own `io.cloudflight.architecture.structurizr.WorkspaceExportService`. 

1. Export to Structurizr
2. Export as PlantUML files


### Export to Structurizr

If you want to send your workspace to a Structurizr Web Server, then configure your app in the `application.yaml`:

````yaml
structurizr:
   workspace:
      name: My project name
      description: My project description
   export:
      structurizr:
         enabled: true
         id: xxx
         key: "any"
         secret: "any"
````

This will configure a `StructurizrClient` as described [in the docs](https://github.com/structurizr/java/blob/master/docs/api-client.md). 
Take the `id`, `key` and `secret` from Structurizr. 

Then run your application, your workspace will be synced to Structurizr.
No need to create and configure a `StructurizrClient` on your own.


### Export as PlantUML files

If you don't want to use the Structurizr Web Server, you can also export your workspace as PlantML files using 
[Structurizr Export for C4PlantUML](https://github.com/cloudflightio/structurizr-export-c4plantuml). All you need to do is to
enable the export in your `application.yaml`:

````yaml
structurizr:
   workspace:
      name: My project name
      description: My project description
   export:
     c4-plant-uml:
       enabled: true
````

This will create one `.puml`-file per view into your folder `build/c4PlantUml` with help of the [C4-PlantUML](https://github.com/plantuml-stdlib/C4-PlantUML). 
Use PlantUML to transform those files into images.