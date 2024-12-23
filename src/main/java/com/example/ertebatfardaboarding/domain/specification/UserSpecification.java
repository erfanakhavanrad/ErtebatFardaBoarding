package com.example.ertebatfardaboarding.domain.specification;

import com.example.ertebatfardaboarding.domain.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    private SearchCriteria searchCriteria;

    public static Specification<User> hasName(String name) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("name"), name));
    }

    public static Specification<User> hasEmail(String email) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email));
    }

}
