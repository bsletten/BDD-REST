package net.bosatsu.oss.rest

/**
 * A utility class to validate the expected structure of the
 * application/vnd.bosatsu.commerce+xml representation.
 *
 * @see net.bosatsu.oss.rest.RestTestStepDefs
 */
class CommerceValidator {
    def validate(def resp) {
        boolean success = false
        def result = resp.result
        assert result.link.size() == 3
        assert result.link.find {it.@rel.text() == 'login'}
        assert result.link.find {it.@rel.text() == 'create-account'}
        assert result.link.find {it.@rel.text() == 'create-order'}
        success = true
    }
}
