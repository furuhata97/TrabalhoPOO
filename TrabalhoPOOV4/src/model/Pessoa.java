    package model;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;

/**
 *
 * @author gusta
 */
public abstract class Pessoa {
    private static final long serialVersionUID = 1L;

    protected static EntityManager entityManager = Persistence.createEntityManagerFactory("PU").createEntityManager();
    
    
    //Método que retorna uma lista com todos os pacientes cadastrados
    public List<Paciente> listaPacientes() {
        return entityManager.createQuery(
                new StringBuilder("SELECT ob FROM Paciente  ob")
                .toString(),
                Paciente.class).getResultList();
    }
    
    //Método que retorna uma lista com todas as consultas cadastradas
    public List<Consulta> listaConsultas() {
        return entityManager.createQuery(
                new StringBuilder("SELECT c FROM Consulta  c")
                .toString(),
                Consulta.class).getResultList();
    }
    
    
    //Método utilizado para adicionar e atualizar informações no banco de dados. Primeiro é feito um merge com o objeto passado como parâmetro e depois é feito o commit desse objeto
    protected void adiciona(Object entidade) {
        entityManager.getTransaction().begin();
        entityManager.merge(entidade);
        entityManager.setFlushMode(FlushModeType.COMMIT);
        entityManager.flush();
        entityManager.getTransaction().commit();
    }
    
    //Realiza a remoção de um objeto passado com parâmetro do banco de dados
    protected void remove(Object entidade) {
        entidade = entityManager.merge(entidade);
        entityManager.getTransaction().begin();
        entityManager.remove(entidade);
        entityManager.getTransaction().commit();
    }
    
    //Busca um paciente no banco de dados de acordo com o CPF passado por parâmetro, e retorna esse paciente; caso este não seja encontrado, retorna null
    public Paciente getPacienteCPF(Long cpf) {
        return entityManager.find(Paciente.class, cpf);
    }
    
    //Busca uma consulta no banco de dados de acordo com o código passado por parâmetro, e retorna essa consulta; caso esta não seja encontrada, retorna null
    public Consulta getConsultaCodigo(Long codigo) {
        return entityManager.find(Consulta.class, codigo);
    }
    
}
