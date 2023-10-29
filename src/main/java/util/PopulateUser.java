package util;



import config.HibernateConfig;
import jakarta.persistence.EntityManagerFactory;
import model.Role;
import model.User;

public class PopulateUser {
    public static void main(String[] args)  {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        createUserTestData(emf);
    }
    public static void createUserTestData(EntityManagerFactory emf) {

        User user = new User("user", "user123");
        User admin = new User("admin", "admin123");

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");


        user.addRole(userRole);
        admin.addRole(adminRole);

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        }
    }
}
