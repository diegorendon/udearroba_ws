package co.edu.udea.udearroba.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for EncryptionUtil methods.
 * 
 * @author Diego Rendón
 */
public class EncryptionUtilTest {
    
    public EncryptionUtilTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of decrypt method, of class EncryptionUtil.
     */
    @Test
    public void testDecrypt() {
        System.out.println("decrypt");
        String password, initialVectorString, secretKey;
        password = "Mi+NSCqmhKN6vPeL";
        secretKey = "167c82ec048434e9ef8e99e373ac0c6a2f21ad16";
        initialVectorString = "167c82ec048434e9"; 
        String expResult = "testpassword";
        String result = EncryptionUtil.decrypt(password, initialVectorString, secretKey);
        assertEquals(expResult, result);
    }
    
}
