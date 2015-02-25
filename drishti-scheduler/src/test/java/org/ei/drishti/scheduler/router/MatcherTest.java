package org.ei.drishti.scheduler.router;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatcherTest {
    @Test
    public void shouldMatchUsingStringEquality() {
        Matcher matcher = Matcher.eq("abc");

        assertTrue(matcher.matches("abc"));
        assertFalse(matcher.matches("abcd"));
        assertFalse(matcher.matches("ab"));
        assertFalse(matcher.matches("dabcd"));
        assertFalse(matcher.matches("dabc"));
        assertFalse(matcher.matches("dddd"));
        assertFalse(matcher.matches(null));
    }

    @Test
    public void shouldMatchAnyValues() {
        Matcher matcher = Matcher.any();

        assertTrue(matcher.matches("abcd"));
        assertTrue(matcher.matches("defg"));
        assertTrue(matcher.matches("xyza"));
        assertTrue(matcher.matches("zzzz"));
    }

    @Test
    public void shouldMatchAGivenSetOfValues() {
        Matcher matcher = Matcher.anyOf("abc", "def");

        assertTrue(matcher.matches("abc"));
        assertTrue(matcher.matches("def"));
        assertFalse(matcher.matches("abcdef"));
        assertFalse(matcher.matches("xyz"));
    }
}
