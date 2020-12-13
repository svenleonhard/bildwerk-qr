package de.bildwerk.qr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import de.bildwerk.qr.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class QrRouteTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QrRoute.class);
        QrRoute qrRoute1 = new QrRoute();
        qrRoute1.setId(1L);
        QrRoute qrRoute2 = new QrRoute();
        qrRoute2.setId(qrRoute1.getId());
        assertThat(qrRoute1).isEqualTo(qrRoute2);
        qrRoute2.setId(2L);
        assertThat(qrRoute1).isNotEqualTo(qrRoute2);
        qrRoute1.setId(null);
        assertThat(qrRoute1).isNotEqualTo(qrRoute2);
    }
}
