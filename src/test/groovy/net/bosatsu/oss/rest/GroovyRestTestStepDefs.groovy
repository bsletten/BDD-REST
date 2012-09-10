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
    }

    def initializeClient() {
        client = new RESTClient(url)
    }

    def issueRequest(String method, String resource, int responseCode) {
        def success = false

        def m = method.toLowerCase()
        def res = resource.toLowerCase()

        def path = conf.rest.bdd.resource."$res".path
        def mimetype = conf.rest.bdd.resource."$res".mimetype
        def handler = conf.rest.bdd.resource."$res".handler
        def validator = conf.rest.bdd.resource."$res".validator

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
            def resp = client."$m"('path' : path)
            assert resp.status == responseCode

            if(mimetype != null) {
                assert resp.contentType == mimetype
            }

            if(validator != null) {
              def v = Class.forName("$validator").newInstance()
              assert v.validate(resp.data)
            }

            success = true
        } catch(Exception e) {
            e.printStackTrace()
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
