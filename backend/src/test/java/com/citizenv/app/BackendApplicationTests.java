package com.citizenv.app;

import static org.junit.jupiter.api.Assertions.*;
import com.citizenv.app.component.Utils;
import com.citizenv.app.entity.Citizen;
import com.citizenv.app.entity.Hamlet;
import com.citizenv.app.repository.CitizenRepository;
import com.citizenv.app.repository.HamletRepository;
import com.citizenv.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private HamletRepository hamletRepository;
    @Autowired
    private UserRepository userRepository;

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

    @Test
    public void TestList() {
        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(null);
        a.add(3);
        List<Integer> b = new ArrayList<>();
        b.add(1);
        b.removeAll(a);
        System.out.println(b.size());
    }

    @Test
    public void TestQuery() {
        List<Hamlet> list = hamletRepository.findHamletFromExcel("Nam Định", "Ý Yên", "Yên Thắng", "Hậu");
        System.out.println(list.size());
    }

    @Test
    public void TestQueryCount() {
        Long a = userRepository.countSubUserAreDeclaring(1L);
        System.out.println(a);
    }

    @Test
    public void encode() {
        System.out.println(encoder.encode("abc"));
    }
}
