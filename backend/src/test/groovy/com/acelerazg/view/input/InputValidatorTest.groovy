package com.acelerazg.view.input

import com.acelerazg.model.Candidate
import spock.lang.Specification

import java.time.LocalDate

class InputValidatorTest extends Specification {
    InputValidator inputValidator

    def setup() {
        inputValidator = new InputValidator()
    }

    def "HasValidLength with valid input"() {
        given:
        String input = "this is a valid input"

        when:
        ValidationResult<String> result = inputValidator.hasValidLength(input, 255, 0)

        then:
        assert (result.isValid)
        assert (result.value == input)
    }

    def "HasValidLength with over max length"() {
        given:
        String input = "this is an invalid input"

        when:
        ValidationResult<String> result = inputValidator.hasValidLength(input, 5, 0)

        then:
        assert (!result.isValid)
        assert (result.message != null)
    }

    def "HasValidLength with empty input"() {
        given:
        String input = ""

        when:
        ValidationResult<String> result = inputValidator.hasValidLength(input, 5, 0)

        then:
        assert (!result.isValid)
        assert (result.message != null)
    }

    def "HasValidLength with valid input but with spaces"() {
        given:
        String input = "this is a valid input"
        String spaces = "    "
        String inputWithSpaces = spaces + input + spaces

        when:
        ValidationResult<String> result = inputValidator.hasValidLength(inputWithSpaces, 255, 0)

        then:
        assert (result.isValid)
        assert (result.value == input)
    }

    def "IsValidDate with valid input"() {
        given:
        String input = "2020-10-10"
        LocalDate date = LocalDate.parse(input, inputValidator.DATE_FORMAT)

        when:
        ValidationResult<LocalDate> result = inputValidator.isValidDateInPast(input)

        then:
        assert (result.isValid)
        assert (result.value == date)
    }

    def "IsValidDate with bad input"() {
        given:
        String input = "qweqwe"

        when:
        ValidationResult<LocalDate> result = inputValidator.isValidDateInPast(input)

        then:
        assert (!result.isValid)
        assert (result.message != null)
    }

    def "IsValidDate with future date"() {
        given:
        String input = "5000-10-10"

        when:
        ValidationResult<LocalDate> result = inputValidator.isValidDateInPast(input)

        then:
        assert (!result.isValid)
        assert (result.message != null)
    }

    def "IsValidId with valid input"() {
        given:
        String input = "2"
        Candidate fake1 = new Candidate(1, "mail", "desc", 1, 1, "First", "Last", "123", LocalDate.now(), "CS")
        Candidate fake2 = new Candidate(2, "mail", "desc", 1, 2, "First", "Last", "123", LocalDate.now(), "CS")
        List<Candidate> list = [fake1, fake2]

        when:
        ValidationResult<Integer> result = inputValidator.isValidId(input, list, "idCandidate")

        then:
        assert (result.isValid)
        assert (result.value == Integer.parseInt(input))
    }

    def "IsValidId with out of bounds input"() {
        given:
        String input = "10"
        Candidate fake1 = new Candidate(1, "mail", "desc", 1, 1, "First", "Last", "123", LocalDate.now(), "CS")
        Candidate fake2 = new Candidate(2, "mail", "desc", 1, 2, "First", "Last", "123", LocalDate.now(), "CS")
        List<Candidate> list = [fake1, fake2]

        when:
        ValidationResult<Integer> result = inputValidator.isValidId(input, list, "idCandidate")

        then:
        assert (!result.isValid)
        assert (result.message != null)
    }

    def "IsValidId with out of bad input"() {
        given:
        String input = "qweqweqweqew"
        Candidate fake1 = new Candidate(1, "mail", "desc", 1, 1, "First", "Last", "123", LocalDate.now(), "CS")
        Candidate fake2 = new Candidate(2, "mail", "desc", 1, 2, "First", "Last", "123", LocalDate.now(), "CS")
        List<Candidate> list = [fake1, fake2]

        when:
        ValidationResult<Integer> result = inputValidator.isValidId(input, list, "idCandidate")

        then:
        assert (!result.isValid)
        assert (result.message != null)
    }
}
