package de.bildwerk.qr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.bildwerk.qr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class UserQrCodeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserQrCode.class);
        UserQrCode userQrCode1 = new UserQrCode();
        userQrCode1.setId(1L);
        UserQrCode userQrCode2 = new UserQrCode();
        userQrCode2.setId(userQrCode1.getId());
        assertThat(userQrCode1).isEqualTo(userQrCode2);
        userQrCode2.setId(2L);
        assertThat(userQrCode1).isNotEqualTo(userQrCode2);
        userQrCode1.setId(null);
        assertThat(userQrCode1).isNotEqualTo(userQrCode2);
    }
}
