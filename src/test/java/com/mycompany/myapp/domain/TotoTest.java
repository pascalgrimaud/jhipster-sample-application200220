package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class TotoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Toto.class);
        Toto toto1 = new Toto();
        toto1.setId("id1");
        Toto toto2 = new Toto();
        toto2.setId(toto1.getId());
        assertThat(toto1).isEqualTo(toto2);
        toto2.setId("id2");
        assertThat(toto1).isNotEqualTo(toto2);
        toto1.setId(null);
        assertThat(toto1).isNotEqualTo(toto2);
    }
}
