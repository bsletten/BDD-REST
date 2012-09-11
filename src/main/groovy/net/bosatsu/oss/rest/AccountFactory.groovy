package net.bosatsu.oss.rest

import groovy.xml.MarkupBuilder

class AccountFactory {
    def createResource() {
        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.account() {
            password('test')

            contact() {
                email('user@example.com')
            }

            address() {
                line('1234 Main St.')
                line('Los Angeles, CA 90233')
            }
        }

        writer.toString()
    }
}
