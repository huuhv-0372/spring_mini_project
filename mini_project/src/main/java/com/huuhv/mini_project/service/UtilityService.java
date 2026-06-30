package com.huuhv.mini_project.service;

import org.springframework.stereotype.Service;

@Service // Tells Spring to register this class as a Bean in the IoC Container
public class UtilityService {
    // Generates an employee code automatically (e.g., input 1 -> returns EM-00001)
    public String generateEmployeeCode(Long id) {
        if (id == null) {
            return null;
        }

        // Format the number as 5 digits, padding with leading zeros if necessary
        return String.format("EM-%05d", id);
    }
}
