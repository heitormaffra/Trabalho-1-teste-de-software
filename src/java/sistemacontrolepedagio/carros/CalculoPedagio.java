/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sistemacontrolepedagio.carros;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author heitormaffra
 */
@Entity
public class CalculoPedagio implements Serializable{
    
    @OneToOne
    private Caminhao caminhao;
    private CarroEmpresa carroempresa;
    private CarroPasseio carroPasseio;
    private Moto moto;
    
    private double calcularPedagio(Object tipoVeiculo){
        if(tipoVeiculo instanceof Caminhao) {
            caminhao = new Caminhao();
            
            caminhao = (Caminhao) tipoVeiculo;
            
            int num_eixos = caminhao.getEixos();
            return 20f * num_eixos; 
        } else if (tipoVeiculo instanceof CarroEmpresa) {
            return 14 * 1.3;
        } else if (tipoVeiculo instanceof CarroPasseio) {
            return 14;
        } else {
            return 7;
        }
    }
    
        private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CalculoPedagio)) {
            return false;
        }
        CalculoPedagio other = (CalculoPedagio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sistemacontrolepedagio.carros.NewEntity[ id=" + id + " ]";
    }
    
}