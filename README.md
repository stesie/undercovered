# undercovered

This is a "just to learn stuff" project. Feel free to learn with me, but please don't use this
code in production.

## What is this?

This is a simple project to learn how to write a Code Coverage Collector for Java.

I've also published a series of articles on the topic here:
https://stefansiegl.de/2025/02/lets-create-a-coverage-analyzer-part-1/

It uses the Java Agent API to register bytecode instrumentation to collect code coverage data.

Currently it only collects line coverage data. It does not bother to collect branch coverage data.

It's also not meant to run particularly fast. It's meant to be simple and easy to understand.
It's not meant to be a production-ready code coverage tool.

Also it clobbers the classpath. It assumes that it can statically call its `Tracker` class from
the instrumented classes and also depends on Jackson to serialize the coverage data.

## How to use it?

The project is a multi-module Maven project. The `coverista` module handles the instrumentation
itself. The `Coverista` class may also be run as an application, to instrument a single class file.
The `agent` module is a simple Java Agent that uses the `coverista` module to instrument classes.
It builds an assembly JAR file that can be used as a Java Agent.

The `demo` module is a simple demo application that can be used to test the agent.

To use the agent, you need to add the assembly JAR to the classpath and specify the agent class
as a Java Agent. For example:

```shell
java -Djava.util.logging.config.file=./coverista/src/main/resources/logging.properties -javaagent:agent/target/agent-1.0-SNAPSHOT.jar=destfile=stuff.json -cp  demo/target/demo-1.0-SNAPSHOT.jar de.brokenpipe.dojo.undercovered.demo.Demo
```

## License

This program is free software. It comes without any warranty, to the extent permitted by applicable
law. You can redistribute it and/or modify it under the terms of the Do What The Fuck You Want To
Public License, Version 2, as published by Sam Hocevar. See http://www.wtfpl.net/ for more details.
