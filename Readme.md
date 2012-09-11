# Introduction

This repository represents the beginning of a reusable framework for
testing REST APIs w/ a BDD tool such as Cucumber. At this stage
the code is not what I would consider production and the examples are
very basic. But I will be working to stabilize and expand the capabilities
and quality over time. Contributions will be happily accepted if they
fit into the overall structure/goals of the project.

The main examples are drawn from a talk that I give. Currently, the
server side of the infrastructure is not included, but the testing
side shows an example of how to use the infrastructure.

Over time I will make this more of a turnkey infrastructure.

# Overview
The net.bosatsu.oss.rest.RestTest class is a simple JUnit-trigger for Cucumber.

The main code is in net.bosatsu.oss.rest.RestTestStepDefs.

The sample feature file is in test/resources/net.bosatsu.oss.rest/rest.feature

config.properties has examples of how to configure the URLs and trigger
MIME type checking and validation.

The AccountFactory, CommerceParser and CommerceValidator classes are from
the example domain and would normally be housed in a separate project. They are
included as examples.

