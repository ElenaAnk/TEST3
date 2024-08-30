import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.example.BookingService;
import org.example.CantBookException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

public class BookingServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceTest.class);

    private BookingService bookingService;

    @BeforeEach
    public void setUp() throws CantBookException {

        bookingService = mock(BookingService.class);
        doCallRealMethod().when(bookingService).book(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));

    }

    @Test
    public void reservationSuccessTest() throws CantBookException {
        logger.debug("Тестирование успешного бронирования");
        String userId = "userId";
        LocalDateTime from = LocalDateTime.now().plusDays(1);
        LocalDateTime to = from.plusHours(2);

        when(bookingService.checkTimeInBD(from, to)).thenReturn(true);
        when(bookingService.createBook(userId, from, to)).thenReturn(true);

        boolean result = bookingService.book(userId, from, to);
        logger.debug("Бронирование успешно: {}", result);
        assertTrue(result);
        logger.debug("Тест завершен");
    }

    @Test
    public void reservationFailedTest() {
        logger.debug("Тестирование неуспешного бронирования");
        String userId = "userId";
        LocalDateTime from = LocalDateTime.now().plusDays(1);
        LocalDateTime to = from.plusHours(2);

        when(bookingService.checkTimeInBD(from, to)).thenReturn(false);

        assertThrows(CantBookException.class, () -> {
            bookingService.book(userId, from, to);
        });
        logger.debug("Бронирование не удалось");
        logger.debug("Тест завершен");
    }
}