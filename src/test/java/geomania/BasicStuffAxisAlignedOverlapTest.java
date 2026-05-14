package geomania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BasicStuffAxisAlignedOverlapTest {

    @Test
    void identicalRectsOverlap() {
        assertTrue(BasicStuff.axisAlignedRectsOverlap(0, 0, 10, 10, 0, 0, 10, 10));
    }

    @Test
    void separatedHorizontallyNoOverlap() {
        assertFalse(BasicStuff.axisAlignedRectsOverlap(0, 0, 10, 10, 10, 0, 10, 10));
    }

    @Test
    void separatedVerticallyNoOverlap() {
        assertFalse(BasicStuff.axisAlignedRectsOverlap(0, 0, 10, 10, 0, 10, 10, 10));
    }

    @Test
    void partialOverlap() {
        assertTrue(BasicStuff.axisAlignedRectsOverlap(0, 0, 10, 10, 5, 5, 10, 10));
    }

    @Test
    void oneContainedInsideOther() {
        assertTrue(BasicStuff.axisAlignedRectsOverlap(0, 0, 100, 100, 40, 40, 10, 10));
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0, 10, 10, 9, 5, 2, 2, true",
            "0, 0, 10, 10, 10, 0, 5, 5, false",
            "0, 0, 10, 10, 0, 10, 5, 5, false",
    })
    void parameterizedOverlap(
            float ax, float ay, float aw, float ah,
            float bx, float by, float bw, float bh,
            boolean expected) {
        if (expected) {
            assertTrue(BasicStuff.axisAlignedRectsOverlap(ax, ay, aw, ah, bx, by, bw, bh));
        } else {
            assertFalse(BasicStuff.axisAlignedRectsOverlap(ax, ay, aw, ah, bx, by, bw, bh));
        }
    }
}
