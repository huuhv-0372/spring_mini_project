package com.huuhv.mini_project.service;

import org.springframework.stereotype.Service;

@Service // Báo cho Spring biết: "Hãy đưa class này vào IoC Container làm Bean nhé!"
public class UtilityService {
    // Hàm sinh mã nhân viên tự động (VD: truyền vào số 1 -> Trả về EM-00005
    public String generateEmployeeCode(Long id) {
        if (id == null) {
            return null;
        }

        // Format số thành 5 chữ số, nếu số có ít hơn 5 chữ số thì sẽ được thêm số 0 vào trước
        return String.format("EM-%05d", id);
    }
}
