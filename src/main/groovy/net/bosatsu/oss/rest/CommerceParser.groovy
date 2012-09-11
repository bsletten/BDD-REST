package net.bosatsu.oss.rest

import groovy.util.slurpersupport.GPathResult

/**
 * A utility class for consuming application/vnd.bosatsu.commerce+xml
 * representations. Currently only slurps them.
 *
 * @see net.bosatsu.oss.rest.RestTestStepDefs
 *
 * TODO: Refactor this be a generic XML handler, not just for the Commerce type.
 */
class CommerceParser {
    GPathResult result;

    CommerceParser(Reader r) {
        def slurper = new XmlSlurper()
        result = slurper.parse(r)
    }
}
