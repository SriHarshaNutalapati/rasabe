package com.enpm.rasa.services;

import com.enpm.rasa.model.ProductTag;
import com.enpm.rasa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

public class DummyData {

    @Autowired
    private static TagRepository tagRepository;

    @Autowired
    private static ProductRepository productRepository;

    @Autowired
    private static UserRepository userRepository;

    @Autowired
    private static CartRepository cartRepository;

    @Autowired
    private static OrderRepository orderRepository;

    @Autowired
    private static OrderItemRepository orderItemRepository;

    public static String getRandomFirstName(){

         String[] FIRST_NAMES = {
                "John", "Jane", "Michael", "Emily", "David",
                "Sarah", "Matthew", "Jessica", "Daniel", "Jennifer",
                "Christopher", "Amanda", "James", "Ashley", "Robert",
                "Elizabeth", "William", "Megan", "Joseph", "Nicole"
        };

        Random rand = new Random();

        return FIRST_NAMES[rand.nextInt(FIRST_NAMES.length)];
    }

    public static String getRandomLastName(){

        String[] LAST_NAMES = {
                "Smith", "Johnson", "Williams", "Jones", "Brown",
                "Davis", "Miller", "Wilson", "Moore", "Taylor",
                "Anderson", "Thomas", "Jackson", "White", "Harris",
                "Martin", "Thompson", "Garcia", "Martinez", "Robinson"
        };

        Random rand = new Random();

        return LAST_NAMES[rand.nextInt(LAST_NAMES.length)];
    }

    public static ProductTag getRandomTag(){
        List<ProductTag> tags = tagRepository.findAll();
        Random rand = new Random();

        return tags.get(rand.nextInt(tags.size()));
    }
}
