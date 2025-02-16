package com.bahadir.pos.repository;

import com.bahadir.pos.entity.organization.Organization;
import com.bahadir.pos.entity.role.Role;
import com.bahadir.pos.entity.user.User;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email); // Yeni metot

    @Query("SELECT u FROM User u " +
            "LEFT JOIN FETCH u.role r " +
            "LEFT JOIN FETCH r.permissions " +
            "LEFT JOIN FETCH u.organization o " +
            "LEFT JOIN FETCH o.company " +
            "WHERE u.email = :email")
    Optional<User> findUserByEmailWithDetails(@Param("email") String email);

//    public Optional<User> findUserByEmailWithDetails(String email) { // Daha açıklayıcı bir isim
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<User> cq = cb.createQuery(User.class);
//        Root<User> root = cq.from(User.class);
//
//        // Join fetch'ler (Gereksiz fetch'lerden kaçının)
//        Join<User, Role> roleJoin = root.join("role", JoinType.LEFT);
//        roleJoin.fetch("permissions", JoinType.LEFT);
//        root.fetch("organization", JoinType.LEFT);
//        Join<User, Organization> organizationJoin = root.join("organization", JoinType.LEFT);
//        organizationJoin.fetch("company", JoinType.LEFT);
//
//        cq.where(cb.equal(root.get("email"), email));
//
//        TypedQuery<User> query = entityManager.createQuery(cq);
//        try {
//            return Optional.ofNullable(query.getSingleResult());
//        } catch (NoResultException e) {
//            return Optional.empty();
//        }
//    }
}

