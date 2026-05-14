package geomania;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class KeyManagerTest {

    private KeyManager keys;

    @BeforeEach
    void setUp() {
        keys = new KeyManager();
    }

    @Test
    void initiallyNoKeysPressed() {
        assertFalse(keys.isUpPressed());
        assertFalse(keys.isDownPressed());
        assertFalse(keys.isRightPressed());
        assertFalse(keys.isLeftPressed());
    }

    @Test
    void upPressAndRelease() {
        keys.arrowUpPressed();
        assertTrue(keys.isUpPressed());
        keys.arrowUpReleased();
        assertFalse(keys.isUpPressed());
    }

    @Test
    void multipleDirectionsIndependent() {
        keys.arrowUpPressed();
        keys.arrowRightPressed();
        assertTrue(keys.isUpPressed());
        assertTrue(keys.isRightPressed());
        assertFalse(keys.isDownPressed());
        assertFalse(keys.isLeftPressed());

        keys.arrowUpReleased();
        assertFalse(keys.isUpPressed());
        assertTrue(keys.isRightPressed());
    }

    @Test
    void allDirectionsCycle() {
        keys.arrowDownPressed();
        keys.arrowLeftPressed();
        assertTrue(keys.isDownPressed());
        assertTrue(keys.isLeftPressed());

        keys.arrowDownReleased();
        keys.arrowLeftReleased();
        assertFalse(keys.isDownPressed());
        assertFalse(keys.isLeftPressed());
    }
}
