package com.citizenv.app;

import static org.junit.jupiter.api.Assertions.*;
import com.citizenv.app.component.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;


class BackendApplicationTests {

    @BeforeEach
    void setUp() {
    }

    @Test
    public void standardizeNameTest() {
        assertEquals("Đinh Tùng Duy", Utils.standardizeName("  =!#%Đ@#inh    tÙNG  duy   "));
    }

    @Test
    public void validateNationId() {
        assertTrue(Utils.validateNationalId("024063128572", "Nam", "24", LocalDate.parse("1963-08-24")));
    }

    @Test
    public void validateNameTest() {
        assertTrue(Utils.validateName("Bà Rịa - Vũng Tàu"));
    }
}
