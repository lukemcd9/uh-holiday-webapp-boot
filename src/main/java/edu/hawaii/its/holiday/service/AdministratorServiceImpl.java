package edu.hawaii.its.holiday.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("administratorService")
public class AdministratorServiceImpl implements AdministratorService {

    @Value("#{'${admin.list}'.split(',')}")
    private List<String> admins;

    @Override
    public void load(List<String> admins) {
        if (admins == null) {
            admins = new ArrayList<>();
        }
        this.admins = Collections.unmodifiableList(admins);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean exists(String uhuuid) {
        if (admins != null && !admins.isEmpty()) {
            return admins.contains(uhuuid);
        }
        return false;
    }
}