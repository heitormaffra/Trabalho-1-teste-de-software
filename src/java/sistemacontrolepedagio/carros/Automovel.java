/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacontrolepedagio.carros;

import java.io.Serializable;
import javax.persistence.Id;


/**
 *
 * @author heitormaffra
 */
public class Automovel implements Serializable{
    
    
    
    private String placa;
    private String motorista;
    @Id
    private Long Id;

    public Automovel() {
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMotorista() {
        return motorista;
    }

    public void setMotorista(String motorista) {
        this.motorista = motorista;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }
    

    
}
