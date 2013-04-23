/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacontrolepedagio.carros;

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
public class CalculoPedagioTest {
    
    public CalculoPedagioTest() {
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
     * Test of getId method, of class CalculoPedagio.
     */
    @Test
    public void testeCalculoPedagioCaminhao() {
        Caminhao automovel = new Caminhao();
        automovel.setEixos(4);
        automovel.setMotorista("Heitor");
        automovel.setPlaca("ABC 1234");
        
        CalculoPedagio calculo = new CalculoPedagio();
        assertEquals(80, calculo.calcularPedagio(automovel), 0);
    }
    
    @Test
    public void testeCalculoPedagioCarroPasseio() {
        CarroPasseio automovel = new CarroPasseio();
        automovel.setMotorista("Heitor");
        automovel.setPlaca("ABC 1234");
        
        CalculoPedagio calculo = new CalculoPedagio();
        assertEquals(14, calculo.calcularPedagio(automovel), 1);
    }
    
    @Test
    public void testeCalculoPedagioCarroEmpresa() {
        CarroEmpresa automovel = new CarroEmpresa();
        automovel.setMotorista("Heitor");
        automovel.setPlaca("ABC 1234");
        
        CalculoPedagio calculo = new CalculoPedagio();
        assertEquals(14 * 1.3, calculo.calcularPedagio(automovel), 1);
    }
    
    @Test
    public void testeCalculoPedagioMoto() {
        Moto automovel = new Moto();
        automovel.setMotorista("Heitor");
        automovel.setPlaca("ABC 1234");
        
        CalculoPedagio calculo = new CalculoPedagio();
        assertEquals(7, calculo.calcularPedagio(automovel), 1);
    }
    
}