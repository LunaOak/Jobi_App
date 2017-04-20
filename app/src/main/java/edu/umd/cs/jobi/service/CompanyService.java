package edu.umd.cs.jobi.service;

import java.util.List;

import edu.umd.cs.jobi.model.Company;

public interface CompanyService {
    public void addPositionToDb(Company company);
    public Company getSPositionByName(String name);
    public List<Company> getAllCompanies();
    public List<Company> getCurrentCompanies();
}
