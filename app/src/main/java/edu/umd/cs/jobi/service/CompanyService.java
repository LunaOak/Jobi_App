package edu.umd.cs.jobi.service;

import java.util.List;

import edu.umd.cs.jobi.model.Company;

public interface CompanyService {
    public void addCompanyToDb(Company company);
    public Company getCompanyByName(String name);
    public List<Company> getAllCompanies();
    public List<Company> getCurrentCompanies();
}
