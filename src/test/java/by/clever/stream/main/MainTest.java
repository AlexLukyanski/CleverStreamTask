package by.clever.stream.main;

import by.clever.stream.main.util.Util;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.mockStatic;

class MainTest {

    private MockedStatic<Util> util = mockStatic(Util.class);

    @Test
    void should_InvokeGetHousesMethod_When_ExecutingTask13() {
        //when
        Main.task13();

        //then
        util.verify(Util::getHouses);
    }
}