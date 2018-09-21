package edu.hawaii.its.holiday.util;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.junit.Test;

public class StringsTest {

    @Test
    public void fill() {
        String s = Strings.fill('$', 6);
        assertEquals("$$$$$$", s);
    }

    @Test
    public void isNotEmpty() {
        assertTrue(Strings.isNotEmpty("t"));
        assertTrue(Strings.isNotEmpty("test"));
        assertTrue(Strings.isNotEmpty(" test "));
        assertFalse(Strings.isNotEmpty(""));
        assertFalse(Strings.isNotEmpty(" "));
        assertFalse(Strings.isNotEmpty(null));
    }

    @Test
    public void isEmpty() {
        assertFalse(Strings.isEmpty("t"));
        assertFalse(Strings.isEmpty("test"));
        assertFalse(Strings.isEmpty(" test "));
        assertTrue(Strings.isEmpty(""));
        assertTrue(Strings.isEmpty(" "));
        assertTrue(Strings.isEmpty(null));
    }

    @Test
    public void trunctate() {
        String s = "abcdefghijk";
        assertEquals("abc", Strings.truncate(s, 3));
        assertEquals("ab", Strings.truncate(s, 2));
        assertEquals("a", Strings.truncate(s, 1));
        assertEquals("", Strings.truncate(s, 0));
        assertEquals(s, Strings.truncate(s, 11));
        assertEquals(s, Strings.truncate(s, 12));

        assertNull(Strings.truncate(null, 0));
        assertNull(Strings.truncate(null, 1));

        // Note this result:
        try {
            assertEquals(s, Strings.truncate(s, -1));
            fail("Should not reach here.");
        } catch (Exception ex) {
            assertThat(ex, instanceOf(IndexOutOfBoundsException.class));
        }
    }

    @Test
    public void testConstructorIsPrivate() throws Exception {
        Constructor<Strings> constructor = Strings.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
