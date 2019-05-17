package gr.rongasa.eshop.configuration;

import gr.rongasa.eshop.domain.Inventory;
import gr.rongasa.eshop.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class DatabaseDataInitialization implements CommandLineRunner {
    private final InventoryRepository inventoryRepository;

    @Override
    public void run(String... args)  {
        if (inventoryRepository.countByType("book")==0){
            inventoryRepository.saveAll(Stream.of("Spring Boot", "Spring Cloud").map(book->Inventory.builder().name(book).cost(BigDecimal.valueOf(150)).amount(100L).build())
                    .collect(Collectors.toList()));
        }
    }
}
