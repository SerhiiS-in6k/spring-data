package com.nixmash.springdata.jpa.service;

import com.nixmash.springdata.jpa.config.ApplicationConfig;
import com.nixmash.springdata.jpa.enums.DataConfigProfile;
import com.nixmash.springdata.jpa.model.addons.Flashcard;
import com.nixmash.springdata.jpa.model.addons.FlashcardCategory;
import com.nixmash.springdata.jpa.repository.addons.FlashcardCategoryRepository;
import com.nixmash.springdata.jpa.repository.addons.FlashcardRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfig.class)
@Transactional
@ActiveProfiles(DataConfigProfile.H2)
public class AddonServiceTests {

    @Autowired
    AddonService addonService;

    @Autowired
    FlashcardCategoryRepository flashcardCategoryRepository;

    @Autowired
    FlashcardRepository flashcardRepository;


    @Before
    public void setup() {
    }

    @Test
    public void flashcardCategoriesRetrieveFlashcardChildren() {
        List<FlashcardCategory> flashcardCategories = addonService.getFlashcardCategories();
        for (FlashcardCategory flashcardCategory : flashcardCategories) {
            if (flashcardCategory.getCategoryId() < 3)
                assertThat(addonService.getFlashcardsByCategoryId(flashcardCategory.getCategoryId()).size(), greaterThan(0));
        }
    }

    @Test
    public void canAddFlashcardCategoryAndFlashcards() {
        FlashcardCategory flashcardCategory = new FlashcardCategory("Category Three");
        FlashcardCategory savedCategory = addonService.addFlashCardCategory(flashcardCategory);

        Flashcard flashcard = new Flashcard(savedCategory.getCategoryId(), "image_for_saved.jpg","This is a new flashcard");
        addonService.addFlashcard(flashcard);

        FlashcardCategory found = addonService.getFlashcardCategoryById(savedCategory.getCategoryId());
        assertNotNull(found);
        assertEquals(addonService.getFlashcardsByCategoryId(found.getCategoryId()).get(0).getImage(), "image_for_saved.jpg");
    }

}
