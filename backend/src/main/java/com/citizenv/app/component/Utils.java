package com.citizenv.app.component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Utils {

    public static final Long A1 = 1L;
    public static final Long A2 = 2L;
    public static final Long A3 = 3L;
    public static final Long B1 = 4L;
    public static final Long B2 = 5L;
    public static final Long EDITOR = 6L;
    public static final String NONE = "Không";
    public static final String AGE_GROUP_UNDER_LEGAL_WORKING_AGE = "Dưới độ tuổi lao động";
    public static final String AGE_GROUP_IN_LEGAL_WORKING_AGE = "Trong độ tuổi lao động";
    public static final String AGE_GROUP_OVER_LEGAL_WORKING_AGE = "Trên độ tuổi lao động";
    public static final String URBAN_AREA = "Thành thị";
    public static final String RURAL_AREA = "Nông thôn";

    interface EnumInterface<K, V> {
        V getValue();
        K findKey(V value);
    }

    public enum AdministrativeUnitsLv1 implements EnumInterface<AdministrativeUnitsLv1, Integer> {
        MUNICIPALITY(1), PROVINCE(2);
        private final Integer id;
        AdministrativeUnitsLv1(int id) {
            this.id = id;
        }

        @Override
        public Integer getValue() {
            return this.id;
        }

        @Override
        public AdministrativeUnitsLv1 findKey(Integer value) {
            return Stream.of(AdministrativeUnitsLv1.values())
                    .filter(p -> p.getValue().equals(value))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

        public static boolean containsKey(Integer value) {
            return Stream.of(AdministrativeUnitsLv1.values())
                    .anyMatch(p -> p.getValue().equals(value));
        }
    }

    public enum AdministrativeUnitsLv2 implements EnumInterface<AdministrativeUnitsLv2, Integer> {
        MUNICIPALITY_CITY(3), PROVINCE_CITY(4), URBAN_DISTRICT(5), DISTRICT_LEVEL_TOWN(6), DISTRICT(7);
        private final Integer id;
        AdministrativeUnitsLv2(int id) {
            this.id = id;
        }

        @Override
        public Integer getValue() {
            return this.id;
        }

        @Override
        public AdministrativeUnitsLv2 findKey(Integer value) {
            return Stream.of(AdministrativeUnitsLv2.values())
                    .filter(p -> p.getValue().equals(value))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

        public static boolean containsKey(Integer value) {
            return Stream.of(AdministrativeUnitsLv2.values())
                    .anyMatch(p -> p.getValue().equals(value));
        }

        public static boolean isUrbanArea(Integer i) {
            return !i.equals(DISTRICT.id);
        }
    }

    public enum AdministrativeUnitsLv3 implements EnumInterface<AdministrativeUnitsLv3, Integer> {
        WARD(8), COMMUNE_LEVEL_TOWN(9), COMMUNE(10);
        private final Integer id;
        AdministrativeUnitsLv3(int id) {
            this.id = id;
        }

        @Override
        public Integer getValue() {
            return this.id;
        }

        @Override
        public AdministrativeUnitsLv3 findKey(Integer value) {
            return Stream.of(AdministrativeUnitsLv3.values())
                    .filter(p -> p.getValue().equals(value))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

        public static boolean containsKey(Integer value) {
            return Stream.of(AdministrativeUnitsLv3.values())
                    .anyMatch(p -> p.getValue().equals(value));
        }
    }

    public enum AdministrativeUnitsLv4 implements EnumInterface<AdministrativeUnitsLv4, Integer> {
        HAMLET(11), VILLAGE(12), TRIBAL_VILLAGE(13), URBAN_NEIGHBORHOOD(14);
        private final Integer id;
        AdministrativeUnitsLv4(int id) {
            this.id = id;
        }
        @Override
        public Integer getValue() {
            return this.id;
        }

        @Override
        public AdministrativeUnitsLv4 findKey(Integer value) {
            return Stream.of(AdministrativeUnitsLv4.values())
                    .filter(p -> p.getValue().equals(value))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

        public static boolean containsKey(Integer value) {
            return Stream.of(AdministrativeUnitsLv4.values())
                    .anyMatch(p -> p.getValue().equals(value));
        }
    }

    public enum BloodType {
        A, B, O, AB
    }

    public enum Sex implements EnumInterface<Sex, String> {
        MALE("Nam"),
        FEMALE("Nữ"),
        OTHER("Khác");

        private final String value;
        Sex(String s) {
            this.value = s;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public Sex findKey(String sex) {
            return Stream.of(Sex.values())
                    .filter(p -> p.getValue().equals(sex))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }

        public static boolean containsKey(Integer value) {
            return Stream.of(AdministrativeUnitsLv1.values())
                    .anyMatch(p -> p.getValue().equals(value));
        }
    }

    public enum MaritalStatus implements EnumInterface<MaritalStatus, String> {
        SINGLE("Chưa kết hôn"),
        MARRIED("Đã kết hôn"),
        DIVORCED("Ly hôn");

        private final String value;
        MaritalStatus(String s) {
            this.value = s;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public MaritalStatus findKey(String status) {
            return Stream.of(MaritalStatus.values()).filter(targetEnum -> targetEnum.value.equals(status)).findFirst().orElseThrow(IllegalArgumentException::new);
        }

        public static boolean containsKey(Integer value) {
            return Stream.of(AdministrativeUnitsLv1.values())
                    .anyMatch(p -> p.getValue().equals(value));
        }
    }

    /**
     * Lop tien ich xu ly ky tu tieng Viet
     *
     * @author quyetdv
     *
     * @implNote I borrow from this <a href="https://quyetdo289.wordpress.com/2015/05/17/loai-bo-dau-tieng-viet-trong-java/">...</a>
     */
    public static class VNCharacterUtils {
        // Mang cac ky tu goc co dau
        private static final char[] SOURCE_CHARACTERS = { 'À', 'Á', 'Â', 'Ã', 'È', 'É',
                'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
                'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
                'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
                'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
                'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
                'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
                'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
                'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
                'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
                'ữ', 'Ự', 'ự', };

        // Mang cac ky tu thay the khong dau
        private static final char[] DESTINATION_CHARACTERS = { 'A', 'A', 'A', 'A', 'E',
                'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
                'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
                'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
                'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
                'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
                'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
                'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
                'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
                'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
                'U', 'u', 'U', 'u', };

        /**
         * Bo dau 1 ky tu
         *
         * @param ch
         * @return
         */
        public static char removeAccent(char ch) {
            int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
            if (index >= 0) {
                ch = DESTINATION_CHARACTERS[index];
            }
            return ch;
        }

        /**
         * Bo dau 1 chuoi
         *
         * @param s
         * @return
         */
        public static String removeAccent(String s) {
            StringBuilder sb = new StringBuilder(s);
            for (int i = 0; i < sb.length(); i++) {
                sb.setCharAt(i, removeAccent(sb.charAt(i)));
            }
            return sb.toString();
        }
    }

    /**
     * Hàm kiểm tra chuỗi có phải là tên tiếng Việt hợp lệ không.
     * Các ví dụ hợp lệ:
     * <ul>
     *     <li>Tên thường như Đinh Tùng Duy;</li>
     *     <li>Tên dân tộc như K'Sante, Ch'ơm, K' Dang, Tr'Hy</li>
     *     <li>Tên gộp bằng dấu gạch ngang như Bà Rịa - Vũng Tàu, Phan Rang - Tháp Chàm</li>
     * </ul>
     */
    public static boolean validateName(String name) {
        String nameRegEx = "^[A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ']{0,6}(-*)(?:[ '][A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]{1,6})*[ -]*(?:[ -']*[A-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ]['][a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđ]{1,6})*$";
        return Pattern.compile(nameRegEx).matcher(name).matches();
    }

    public static String removeNonAlphanumeric(String str)
    {
        str = str.replaceAll(
                "[^a-zàáạảãâầấậẩẫăằắặẳẵèéẹẻẽêềếệểễìíịỉĩòóọỏõôồốộổỗơờớợởỡùúụủũưừứựửữỳýỵỷỹđA-ZÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴÈÉẸẺẼÊỀẾỆỂỄÌÍỊỈĨÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠÙÚỤỦŨƯỪỨỰỬỮỲÝỴỶỸĐ0-9-']", "");
        return str;
    }

    public static String standardizeName(String name) {
        StringBuilder sb = new StringBuilder();
        for (String s : name.split(" ")) {
            s = removeNonAlphanumeric(s);
            if (s.length() > 0) {
                sb.append(Character.toUpperCase(s.charAt(0)));
                if (s.length() > 1) {
                    sb.append(s.substring(1).toLowerCase());
                }
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    public static boolean validateSex(String sex) {
        return sex.equals("Nam") ||
                sex.equals("Nữ") ||
                sex.equals("Khác");
    }

    /*  A valid national ID has 12 chars.
        3 first chars represent province code.
        The 4th follows:
        - Born 20th century -> 0 if male, 1 is female.
        - Born 21st century -> 2 if male, 3 is female, and so on.
        2 next chars represent year of birth, "YY" format.
        6 last are random, they said.
        I'm too lazy, so this only check the first condition.
     */
    public static boolean validateNationalId(String id, String sex, String provinceCode, LocalDate dateOfBirth) {
        if(id.length() != 12) return false;

        String firstThreeDigits = id.substring(1, 3); // ma tinh thanh noi dang ky khai sinh
        String nextOneDigit = id.substring(3, 4); // ma gioi tinh theo nam sinh
        String nextTwoDigits = id.substring(4, 6); // 2 so cuoi nam sinh

        boolean validateProvinceCode = false;
        boolean validateSex = false;
        boolean validateBirth = false;

        int yearOfBirth = dateOfBirth.getYear();

        if (firstThreeDigits.equals(provinceCode)) validateProvinceCode = true;
        if (nextTwoDigits.equals((String.valueOf(yearOfBirth)).substring(2))) validateBirth = true;

        switch (sex) {
            case "Nam" : if (yearOfBirth >= 1900 && yearOfBirth <= 1999) {
                                if (nextOneDigit.equals("0")) validateSex = true;
                        } else if (yearOfBirth >= 2000 && yearOfBirth <= 2099) {
                                if (nextOneDigit.equals("2")) validateSex = true;
                        }
                        break;
            case "Nữ" : if (yearOfBirth >= 1900 && yearOfBirth <= 1999) {
                            if (nextOneDigit.equals("1")) validateSex = true;
                        } else if (yearOfBirth >= 2000 && yearOfBirth <= 2099) {
                            if (nextOneDigit.equals("3")) validateSex = true;
                        }
                        break;
        }
        return validateProvinceCode && validateSex && validateBirth;
    }
    public static boolean validateFormatDivisionCode(String divisionCode) {
        String pattern = "^[0-9]+$";
        return Pattern.compile(pattern).matcher(divisionCode).matches();
    }
}
