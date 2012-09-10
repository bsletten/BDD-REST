package net.bosatsu.oss.rest;

import cucumber.annotation.en.Then;
import cucumber.annotation.en.When;

/**
 * Created with IntelliJ IDEA.
 * User: brian
 * Date: 9/9/12
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class RestTestStepDefs {
    @When("^I want to do something$")
    public void foo() {
        System.out.println("foo");
    }

    @Then("^I can do something$")
    public void boo() {
        System.out.println("boo");
    }
}
