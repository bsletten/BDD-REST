package net.bosatsu.oss.rest

import groovy.util.slurpersupport.GPathResult

class CommerceParser {
    GPathResult result;

    CommerceParser(Reader r) {
        def slurper = new XmlSlurper()
        result = slurper.parse(r)
    }
}
