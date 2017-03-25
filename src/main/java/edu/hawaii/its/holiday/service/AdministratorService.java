package edu.hawaii.its.holiday.service;

import java.util.List;

public interface AdministratorService {
    public boolean exists(String uhuuid);

    public void load(List<String> admins);
}
