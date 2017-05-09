package edu.umd.cs.jobi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.umd.cs.jobi.model.Company;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class CompanyModelTest {

    private Company company = null;

    @Before
    public void setUp() throws Exception {
        company = new Company("Google", true);
    }

    @After
    public void tearDown() throws Exception {
        company = null;
    }

    @Test
    public void testCompanyConstructor() {

        // Assert company values are all as set
        assertTrue(company != null);
        assertEquals(company.getName(), "Google");
        assertTrue(company.getCurrent());
    }

    @Test
    public void testSetters() {

        // Test setLocation()
        assertNull(company.getLocation());
        company.setLocation("Menlo Park");
        assertEquals(company.getLocation(), "Menlo Park");

        // Test setCurrent()
        company.setCurrent(false);
        assertFalse(company.getCurrent());

    }

}
