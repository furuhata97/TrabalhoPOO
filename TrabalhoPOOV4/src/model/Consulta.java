package model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import util.GeraDatas;
import java.io.Serializable;
import java.sql.Date;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

/**
 *
 * @author gusta
 */
@Entity
public class Consulta implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //O código da consulta é gerado automaticamente
    private Long codigo;
    
    private Date dataConsulta;
    private String hora;
    private String medico;
    
    @ManyToOne(cascade = CascadeType.REFRESH, fetch=FetchType.EAGER)
    private Paciente paciente;
    
    private String tipoConsulta;
    
    //Construtor padrão
    public Consulta(){
        
    }
    
    
    //Construtor parâmetrizado
    public Consulta(Long codConsulta, Date dataConsulta, String hora, String medico, Paciente paciente, String tipoConsulta) {
        this.codigo = codConsulta;
        this.dataConsulta = dataConsulta;
        this.hora = hora;
        this.medico = medico;
        this.paciente = paciente;
        this.tipoConsulta = tipoConsulta;
    }

    
    //Todos os métodos getter e setter de consulta são apresentados abaixo
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public Date getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(Date dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMedico() {
        return medico;
    }

    public void setMedico(String medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }
    
    public String getDataConsultaString() {
        return GeraDatas.geraDataDate(dataConsulta);
    }
    
}
