package zero.eight.donut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zero.eight.donut.domain.Product_Price;

public interface Product_PriceRepository extends JpaRepository<Product_Price, Long> {
}
