/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacontrolepedagio.vendas;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author heitor.filho
 */
public class VendasTest {
    
    public VendasTest() {
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

    @Test
    public void testaCalculoTroco(){
        Vendas venda = new Vendas();
        
        assertEquals(venda, venda); venda.calcularTroco(18, 18);
    }
}