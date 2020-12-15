package de.bildwerk.qr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.bildwerk.qr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class UserQrCodeExposedTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserQrCodeExposed.class);
        UserQrCodeExposed userQrCodeExposed1 = new UserQrCodeExposed();
        userQrCodeExposed1.setId(1L);
        UserQrCodeExposed userQrCodeExposed2 = new UserQrCodeExposed();
        userQrCodeExposed2.setId(userQrCodeExposed1.getId());
        assertThat(userQrCodeExposed1).isEqualTo(userQrCodeExposed2);
        userQrCodeExposed2.setId(2L);
        assertThat(userQrCodeExposed1).isNotEqualTo(userQrCodeExposed2);
        userQrCodeExposed1.setId(null);
        assertThat(userQrCodeExposed1).isNotEqualTo(userQrCodeExposed2);
    }
}
