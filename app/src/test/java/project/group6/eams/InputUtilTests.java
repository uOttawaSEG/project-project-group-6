package project.group6.eams;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import project.group6.eams.utils.InputUtils;

@RunWith(RobolectricTestRunner.class)
public class InputUtilTests {
    @Test
    public void testValidEmail() {
        assertTrue("Good Email",InputUtils.isValidEmail("bob.b.bobby@gmai.com"));
        assertTrue("Good Email",InputUtils.isValidEmail("b@gmail.c"));
        assertFalse("Missing .",InputUtils.isValidEmail("bob@gmail"));
        assertFalse("Missing @",InputUtils.isValidEmail("bobgmail.com"));
        assertFalse("Missing @ and .",InputUtils.isValidEmail("bobgmailcom"));
        assertFalse("Empty Email",InputUtils.isValidEmail(""));
        assertFalse("Null Email",InputUtils.isValidEmail(null));
        assertFalse("Space Email",InputUtils.isValidEmail("bob @gmail.com"));
        assertFalse("Space Email",InputUtils.isValidEmail("bob@ gmail.com"));
        assertFalse("Space Email",InputUtils.isValidEmail("bob@gmail. com"));
        assertFalse("Characters",InputUtils.isValidEmail("bob''&<~! $%^&*_=+}{'?-.>@gmail.com"));
    }

    @Test
    public void testValidPhoneNumber() {
        assertTrue("Dashes", InputUtils.isValidPhoneNumber("613-555-7777"));
        assertTrue("No Separator", InputUtils.isValidPhoneNumber("6135557777"));
        assertTrue("Dots", InputUtils.isValidPhoneNumber("613.555.7777"));
        assertTrue("Plus", InputUtils.isValidPhoneNumber("+16135557777"));
        assertFalse("Empty", InputUtils.isValidPhoneNumber(""));
        assertFalse("Null", InputUtils.isValidPhoneNumber(null));
        assertFalse("Too Short", InputUtils.isValidPhoneNumber("613555"));
        assertTrue("Spaces", InputUtils.isValidPhoneNumber("613 555 7777"));
        assertFalse("Characters", InputUtils.isValidPhoneNumber("613bob5557777"));
        assertTrue("Parentheses", InputUtils.isValidPhoneNumber("(613)555-7777"));
    }

    @Test
    public void testVerifyPassword() {
        assertTrue("Same", InputUtils.verifyPassword("bob", "bob"));
        assertFalse("Different", InputUtils.verifyPassword("bob", "bobby"));
        assertFalse("Empty", InputUtils.verifyPassword("", ""));
        assertFalse("Null", InputUtils.verifyPassword(null, null));
        assertFalse("Null", InputUtils.verifyPassword(null, "bob"));
        assertFalse("Null", InputUtils.verifyPassword("bob", null));
        assertFalse("Empty", InputUtils.verifyPassword("", "bob"));
        assertFalse("Empty", InputUtils.verifyPassword("bob", ""));
        assertFalse("Null and Empty", InputUtils.verifyPassword(null, ""));
        assertFalse("Empty and Null", InputUtils.verifyPassword("", null));
        assertFalse("Spaces", InputUtils.verifyPassword("bob", "bob "));
        assertFalse("Spaces", InputUtils.verifyPassword("bob ", "bob"));
    }
    @Test
    public void testIsValidName() {
        assertTrue("Valid", InputUtils.isValidName("Bob"));
        assertFalse("Empty", InputUtils.isValidName(""));
        assertFalse("Null", InputUtils.isValidName(null));
        assertFalse("Short", InputUtils.isValidName("B"));
        assertFalse("Numbers", InputUtils.isValidName("Bob1"));
        assertFalse("Special", InputUtils.isValidName("Bob@"));

    }
    @Test
    public void testIsValidStreet() {
        assertTrue("Valid", InputUtils.isValidStreet("123 Street"));
        assertFalse("Empty", InputUtils.isValidStreet(""));
        assertFalse("Null", InputUtils.isValidStreet(null));
        assertFalse("No name given", InputUtils.isValidStreet("123"));
        assertFalse("No number given", InputUtils.isValidStreet("Street"));
        assertFalse("No space", InputUtils.isValidStreet("123Street"));
        assertTrue("Suffix given", InputUtils.isValidStreet("123 StreetName Rd"));
        assertFalse("Wrong order", InputUtils.isValidStreet("StreetName 123"));
        assertFalse("Wrong order", InputUtils.isValidStreet("StreetName Rd 123"));
        assertFalse("Wrong order", InputUtils.isValidStreet("StreetName 123 Rd"));
        assertFalse("No number in split", InputUtils.isValidStreet("StreetName Rd"));
        assertFalse("Too many Arguments", InputUtils.isValidStreet("123 StreetName Rd StreetName"));

    }
    @Test
    public void testIsValidPostalCode() {
        assertTrue("Valid", InputUtils.isValidPostalCode("A1A1A1"));
        assertFalse("Empty", InputUtils.isValidPostalCode(""));
        assertFalse("Null", InputUtils.isValidPostalCode(null));
        assertFalse("Short", InputUtils.isValidPostalCode("A1A1A"));
        assertFalse("Long", InputUtils.isValidPostalCode("A1A1A1A"));
        assertFalse("Special", InputUtils.isValidPostalCode("A1A1A@"));
        assertTrue("Space", InputUtils.isValidPostalCode("A1A 1A1"));
        assertTrue("Lowercase", InputUtils.isValidPostalCode("a1a1a1"));
    }
    @Test
    public void testPasswordChecker() {
        assertEquals("Good Password","",InputUtils.passwordChecker("Bob123!@"));
        assertEquals("Good Password","",InputUtils.passwordChecker("Test#123"));
        assertEquals("Password must contain: \nAt least one upper case letter",InputUtils.passwordChecker("bob123!@"));
        assertEquals("Password must contain: \nAt least one lower case letter",InputUtils.passwordChecker("BOB123!@"));
        assertEquals("Password must contain: \nAt least one number",InputUtils.passwordChecker("Bobby!!@"));
        assertEquals("Password must contain: \nAt least one special character. Eg: @,!,?",InputUtils.passwordChecker("Bobby123"));
        assertEquals("Password must contain: \nAt least 8 characters",InputUtils.passwordChecker("Bob12@"));
    }
}
