package test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import br.com.cdweb.persistence.entities.Configuracoes;

public class JpaTest {

	private EntityManager manager;

	public JpaTest(EntityManager manager) {
		this.manager = manager;
	}

	public static void main(String[] args) {
		setIP();
	}

	/**
	 * @param args
	 */
	public static void main2(String[] args) {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("cdwebPersistenceUnit");
		EntityManager manager = factory.createEntityManager();
		JpaTest test = new JpaTest(manager);

		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		try {
			test.createEmployees();
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();

		test.listEmployees();

		System.out.println(".. done");
	}

	private void createEmployees() {
		int numOfEmployees = manager
				.createQuery("Select a From Employee a", Employee.class)
				.getResultList().size();
		if (numOfEmployees == 0) {
			Department department = new Department("java");
			manager.persist(department);
			//
			// manager.persist(new Employee("Jakab Gipsz",department));
			// manager.persist(new Employee("Captain Nemo",department));

		}
	}

	private void listEmployees() {
		List<Employee> resultList = manager.createQuery(
				"Select a From Employee a", Employee.class).getResultList();
		System.out.println("num of employess:" + resultList.size());
		for (Employee next : resultList) {
			System.out.println("next employee: " + next);
		}
	}

	public static void setIP() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("cdwebPersistenceUnit");
		EntityManager manager = factory.createEntityManager();
		// EntityTransaction tx = manager.getTransaction();
		// tx.begin();
		Configuracoes configuration = new Configuracoes();
		configuration.setChave("IP_RASP");
		configuration.setValor("192.168.0.1");
		try {
			manager.getReference(Configuracoes.class, "IP_RASP");
			manager.persist(configuration);

		} catch (javax.persistence.EntityNotFoundException e) {
			manager.persist(configuration);
		}
	}

}