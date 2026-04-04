import com.esdc.lab3.entity.Beverage;
import com.esdc.lab3.repository.BeverageRepository;
import com.esdc.lab3.service.BeverageRecommender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BeverageRecommenderTest {

    @Mock
    private BeverageRepository beverageRepository;

    private BeverageRecommender recommender;

    private Beverage americano;
    private Beverage cortado;
    private Beverage icedLatte;

    @BeforeEach
    public void beforeTest() {
        recommender = new BeverageRecommender(beverageRepository);
        americano = new Beverage("Americano", 1L, 22.0, "Some description");
        cortado = new Beverage("Cortado", 2L, 11.0, "Some desc");
        icedLatte = new Beverage("Iced Latte", 3L, 11.0, "Some desc");
    }

    @Test
    public void recordSelectionShouldTrackOrderCount() {
        when(beverageRepository.getBeverageById(1L)).thenReturn(americano);
        recommender.recordSelection(List.of(americano, cortado));
        recommender.recordSelection(List.of(americano));

        Beverage res = recommender.recommendBasedOnHistory();

        assertEquals(americano, res);
    }

    @Test
    public void recommendBasedOnHistoryEmptyHistoryShouldReturnRandom() {
        when(beverageRepository.getAllBeverages()).thenReturn(List.of(americano, cortado));

        Beverage res = recommender.recommendBasedOnHistory();

        assertNotNull(res);
        verify(beverageRepository, never()).getBeverageById(anyLong());
    }

    @Test
    public void recommendNewShouldReturnNeverOrdered() {
        when(beverageRepository.getAllBeverages()).thenReturn(List.of(americano, cortado, icedLatte));
        recommender.recordSelection(List.of(americano));

        Beverage res = recommender.recommendNew();

        assertNotNull(res);
        assertNotEquals(americano, res);
    }

    @Test
    public void getTopOrderedShouldReturnLimitedResults() {
        recommender.recordSelection(List.of(americano));
        recommender.recordSelection(List.of(americano, cortado));
        recommender.recordSelection(List.of(americano, cortado, icedLatte));

        List<Map.Entry<Long, Integer>> top = recommender.getTopOrdered(2);

        assertEquals(2, top.size());
        assertEquals(americano.getId(), top.get(0).getKey());
        assertEquals(cortado.getId(), top.get(1).getKey());
    }
}
