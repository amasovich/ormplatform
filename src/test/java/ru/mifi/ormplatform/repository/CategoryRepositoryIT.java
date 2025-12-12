package ru.mifi.ormplatform.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.ormplatform.domain.entity.Category;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CategoryRepositoryIT {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save_and_find_category() {
        // given
        Category category = new Category();
        category.setName("Test Category");

        // when
        Category saved = categoryRepository.save(category);

        // then
        assertThat(saved.getId()).isNotNull();

        Category found = categoryRepository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test Category");
    }

    @Test
    void find_by_name() {
        // given
        Category category = new Category();
        category.setName("Java");
        categoryRepository.save(category);

        // when
        Category found = categoryRepository.findByName("Java").orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Java");
    }
}
