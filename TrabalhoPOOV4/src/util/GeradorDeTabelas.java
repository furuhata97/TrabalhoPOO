/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Consulta;
import model.Paciente;
import org.apache.commons.beanutils.PropertyUtils;
import model.Pessoa;

/**
 *
 * @author gusta
 */
public class GeradorDeTabelas {

    /**
     *  Gera as tabelas de pacientes
     * @param secretaria
     * @return tabela de pacientes
     */
    public static DefaultTableModel geraTabelaPacientes(Pessoa secretaria) {
        String[] header = {"CPF", "Nome", "Data nascimento"};
        Object[][] pacientes = new Object[secretaria.listaPacientes().size()][Paciente.class.getDeclaredFields().length];
        int i = 0;
        int j = 0;
        for (Paciente p : secretaria.listaPacientes()) {
            for (Field field : p.getClass().getDeclaredFields()) {
                try {
                    pacientes[i][j] = PropertyUtils.getSimpleProperty(p, field.getName());
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    pacientes[i][j] = "";
                }
                j++;
            }
            j = 0;
            i++;
        }
        return new javax.swing.table.DefaultTableModel(
                pacientes, header);
    }
    
    public static DefaultTableModel geraTabelaPacientesMedico(Pessoa medico) {
        String[] header = {"CPF", "Nome", "Data nascimento"};
        List<Paciente> consultasHoje = PacientesConsulta(medico.listaConsultas());
        Object[][] pacientes = new Object[consultasHoje.size()][Paciente.class.getDeclaredFields().length];
        int i = 0;
        int j = 0;
        for (Paciente p : consultasHoje) {
            for (Field field : p.getClass().getDeclaredFields()) {
                try {
                    pacientes[i][j] = PropertyUtils.getSimpleProperty(p, field.getName());
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                    pacientes[i][j] = "";
                }
                j++;
            }
            j = 0;
            i++;
        }
        return new javax.swing.table.DefaultTableModel(
                pacientes, header);
    }
    
    
    public static List<Paciente> PacientesConsulta(List <Consulta> consultas){
        List<Consulta> consultasHoje = new LinkedList<Consulta>();
        Date d = Date.valueOf(LocalDate.now());
        for(Consulta c: consultas){
            if(c.getDataConsulta().compareTo(d)==0){
                consultasHoje.add(c);
            }
        }
        List<Paciente> pacientes = new LinkedList<Paciente>();
        for(Consulta c: consultasHoje){
            if(!pacientes.contains(c.getPaciente()))
                pacientes.add(c.getPaciente());
        }
        return pacientes;
    }

}
