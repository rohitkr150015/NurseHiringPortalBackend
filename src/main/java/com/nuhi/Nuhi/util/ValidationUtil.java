package com.nuhi.Nuhi.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Pattern LICENSE_PATTERN = Pattern.compile("^[A-Z0-9-]{10,20}$");

    public static boolean isValidLicenseNumber(String license) {
        // Corrected: Proper matcher usage
        return license != null && LICENSE_PATTERN.matcher(license).matches();
    }
}
