package edu.dosw.TECHCUP.core.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Base64PasswordEncoderTest {

    private Base64PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new Base64PasswordEncoder();
    }

    @Test
    void encode_returnsBase64String() {
        String encoded = encoder.encode("password123");
        assertThat(encoded).isEqualTo("cGFzc3dvcmQxMjM=");
    }

    @Test
    void matches_withCorrectPassword_returnsTrue() {
        String encoded = encoder.encode("miClave");
        assertThat(encoder.matches("miClave", encoded)).isTrue();
    }

    @Test
    void matches_withWrongPassword_returnsFalse() {
        String encoded = encoder.encode("miClave");
        assertThat(encoder.matches("otraClave", encoded)).isFalse();
    }

    @Test
    void encode_emptyString_returnsEmptyBase64() {
        String encoded = encoder.encode("");
        assertThat(encoded).isEqualTo("");
    }
}