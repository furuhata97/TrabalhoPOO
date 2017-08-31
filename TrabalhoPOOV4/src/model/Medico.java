/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author gusta
 */
public class Medico extends Pessoa{
    
    //Método que adiciona e remove dados adicionais. Este método também é utilizado para a remoção, 
    //pois quando um dado adicional é removido, ele é setado para null, o que conta como uma inclusão de dado null
    public void adicionaDadosPaciente(Paciente paciente) {
        adiciona(paciente);
    }
    
    //Método que adiciona e remove prontuário. Este método também é utilizado para a remoção, 
    //pois quando um prontuario é removido, ele é setado para null, o que conta como uma inclusão de dado null
    public void adicionaProntuarioPaciente(Paciente paciente) {
        adiciona(paciente);
    }
}
