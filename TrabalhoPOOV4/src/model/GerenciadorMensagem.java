/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Calendar;
import java.util.List;
import javax.persistence.Query;
import static model.Pessoa.entityManager;

/**
 *
 * @author gusta
 */
public class GerenciadorMensagem {
    
    //Adiciona na lista de consultas, as consultas dos pacientes que tem consulta na data solicitada e que possuem telefone cadastrado no sistema, retornando essa lista de consultas.
    public List<Consulta> getConsultasDatacomTelefone(Calendar dataDesejada) {
        Query q = entityManager.createQuery(
                new StringBuilder("SELECT c FROM Consulta c WHERE"
                        + "  coalesce(c.paciente.telefone, '') <> '' AND dataConsulta = :dataDesejada")
                .toString(),
                Consulta.class);
        q.setParameter("dataDesejada", dataDesejada.getTime());
        return q.getResultList();
    }
    
    //Adiciona na lista de consultas, as consultas dos pacientes que tem consulta na data solicitada e que possuem email cadastrado no sistema, retornando essa lista de consultas.
    public List<Consulta> getConsultasDatacomEmail(Calendar dataDesejada) {
        Query q = entityManager.createQuery(
                new StringBuilder("SELECT c FROM Consulta c WHERE"
                        + " coalesce(c.paciente.email, '') <> '' AND c.dataConsulta = :dataDesejada")
                .toString(),
                Consulta.class);
        q.setParameter("dataDesejada", dataDesejada.getTime());
        return q.getResultList();
    }
}
