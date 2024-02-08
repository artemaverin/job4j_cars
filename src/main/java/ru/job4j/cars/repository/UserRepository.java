package ru.job4j.cars.repository;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "UPDATE User SET password = :fPassword WHERE id = :fId")
                    .setParameter("fPassword", "new password")
                    .setParameter("fId", user.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                            "DELETE User WHERE id = :fId", User.class)
                    .setParameter("fId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        var session = sf.openSession();
        List<User> usersList = new ArrayList<>();
        try {
            session.beginTransaction();
            usersList = session.createQuery("from User", User.class).getResultList();
            session.getTransaction().commit();
        }catch (Exception e){
            session.getTransaction().rollback();
        }
        return usersList;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int userId) {
        var session = sf.openSession();
        Optional<User> userOptional = Optional.empty();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "from User as i where i.id = :fId", User.class);
            query.setParameter("fId", userId);
            userOptional = Optional.of(query.getSingleResult());
            session.getTransaction().commit();
        }catch (Exception e) {
            session.getTransaction().rollback();
        }

        return userOptional;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        var session = sf.openSession();
        List<User> usersList = new ArrayList<>();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(
                    "from User as i where i.login LIKE :fKey", User.class);
            query.setParameter("fKey",  "%" + key + "%");
            usersList = query.getResultList();
            session.getTransaction().commit();
        }catch (Exception e) {
            session.getTransaction().rollback();
        }
        return usersList;
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        var session = sf.openSession();
        try {
            Query<User> query = session.createQuery(
                    "from User as i where i.login = :fLogin", User.class);
            query.setParameter("fLogin", login);
            return Optional.ofNullable(query.getSingleResult());
        } catch (Exception e) {
            session.getTransaction().rollback();
        }

        return Optional.empty();
    }
}
