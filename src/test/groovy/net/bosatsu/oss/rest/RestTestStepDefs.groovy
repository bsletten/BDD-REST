package net.bosatsu.oss.rest

import cucumber.annotation.en.Then
import groovyx.net.http.RESTClient
import groovyx.net.http.ParserRegistry

this.metaClass.mixin(cucumber.runtime.groovy.Hooks)
this.metaClass.mixin(cucumber.runtime.groovy.EN)

class CustomWorld {
    def client
    def conf
    def url
    def badUser
    def badPass
    def goodUser
    def goodPass

    CustomWorld() {
        def configFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties")
        def p = new Properties()
        p.load(configFile)
        conf = new ConfigSlurper().parse(p)
        def host = conf.rest.bdd.host
        def port = conf.rest.bdd.port
        url = "http://${host}"

        if(port != null) {
            url += ":${port}"
        }

        badUser = conf.rest.bdd.credentials.bad.username
        badPass = conf.rest.bdd.credentials.bad.password
        goodUser = conf.rest.bdd.credentials.good.username
        goodPass = conf.rest.bdd.credentials.good.password
    }

    def initializeClient() {
        client = new RESTClient(url)
    }

    def addBadCredentials() {
        client.auth.basic badUser, badPass
    }

    def addGoodCredentials() {
        client.auth.basic goodUser, goodPass
    }

    def issueRequest(String method, String resource, int responseCode) {
        issueRequest(method, null, resource, responseCode)
    }

    def issueRequest(String method, def body, String resource, int responseCode) {
        def success = false

        def m = method.toLowerCase()
        def res = resource.toLowerCase()
        def slash = false

        if(res.endsWith("_slash")) {
            slash = true
            res = res.substring(0, res.length() - 6)
        }

        def path = conf.rest.bdd.resource."$res".path
        def mimetype = conf.rest.bdd.resource."$res".mimetype
        def handler = conf.rest.bdd.resource."$res".handler
        def validator = conf.rest.bdd.resource."$res".validator

        if(slash && !path.endsWith("/")) {
            path += "/"
        }

        if(handler != null) {
            assert mimetype != null
            client.parser."$mimetype" = { resp ->
                Reader r = new InputStreamReader(resp.entity.content, ParserRegistry.getCharset(resp))
                def ctor = Class.forName(handler)
                   .getConstructor([Reader.class] as Class[])
                return ctor.newInstance(r)
            }
        }

        try {
            def reqMap = ['path' : path]

            if(body != null) {
                reqMap['body'] = body
                //TODO: Why do custom MIME types throw NPEs in HTTPBuilder?
                reqMap['requestContentType'] = 'application/xml'
            }

            // Invoke the requested method (GET, POST, PUT, DELETE, HEAD, OPTIONS)
            // against the specified resource.
            def resp = client."$m"(reqMap)

            assert resp.status == responseCode

            if(m.equals("get") && mimetype != null) {
                assert resp.contentType == mimetype
            }

            if(resp.status == 201) {
                assert resp.headers['Location'] != null
            }

            // Validators can be specified to analyze the structure
            // of what is returned beyond a specific MIME type.
            if(validator.size() > 0) {
              def v = Class.forName("$validator").newInstance()
              assert v.validate(resp.data)
            }

            success = true
        } catch(ex) {
            if(responseCode < 400) {
                // This was not an exception we were
                // expecting. Complain.
                ex.printStackTrace()
            } else {
                // We were expecting a failure, so let's
                // make sure it was the right kind.
                assert ex.response.status == responseCode
                success = true
            }
        }

        success
    }
}

World {
    new CustomWorld()
}

When(~"I want to interact with a resource") { ->
    initializeClient()
}

Then(~"I can (\\w+) a (\\w+) resource") { String method, String resource ->
    assert issueRequest(method, resource, 200)
}

Then(~"I can create a new (\\w+) resource") { String resource ->
    def res = resource.toLowerCase()
    def factory = conf.rest.bdd.resource."$res".factory
    assert factory != null
    factory = Class.forName(factory).newInstance()

    def body = factory.createResource()
    assert issueRequest("POST", body, resource, 201)
}

Then(~"I am I denied a (\\w+) resource") { String resource ->
    assert issueRequest(method, resource, 401)
}

Then(~"I am told I cannot (\\w+) a (\\w+) resource") { String method, String resource ->
    assert issueRequest(method, resource, 405)
}

And(~"I use the wrong username and password") { ->
    addBadCredentials()
}

And(~"I use the right username and password") { ->
    addGoodCredentials()
}
