package com.codebullets.sagalib.startup;

import com.codebullets.sagalib.Saga;
import com.codebullets.sagalib.TestSaga;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for SagaAnalyzer class.
 */
public class AnnotationSagaAnalyzerTest {
    private Collection<Class<? extends Saga>> sagaTypes;
    private SagaAnalyzer sut;

    @Before
    public void setUp() {
        sagaTypes = new ArrayList<>();

        TypeScanner scanner = mock(TypeScanner.class);
        when(scanner.scanForSagas()).thenReturn(sagaTypes);

        sut = new AnnotationSagaAnalyzer(scanner);
    }

    /**
     * Given => no types found by type scanner
     * When  => Scan handled message types is called.
     * Then  => Method returns empty list.
     */
    @Test
    public void scanHandledMessageTypes_noTypesFound_returnsEmptyMap() {
        // given
        sagaTypes.clear();

        // when
        Map<Class<? extends Saga>, SagaHandlersMap> scanResult = sut.scanHandledMessageTypes();

        // then
        assertThat("Expected empty map if no data present.", scanResult, not(nullValue()));
    }

    /**
     * Given => Tests saga found by injected scanner.
     * When  => scanHandledMessageTypes is called.
     * Then  => Returns the result with one entry containing expected handlers.
     */
    @Test
    public void scanHandledMessageTypes_testSagaFound_returnsEntryWithHandlers() {
        // given
        sagaTypes.add(TestSaga.class);

        // when
        Map<Class<? extends Saga>, SagaHandlersMap> scanResult = sut.scanHandledMessageTypes();

        // then
        assertThat("Expected one entry in scan result.", scanResult.size(), equalTo(1));
        assertThat("Expected entry of key TestSage.class", scanResult.containsKey(TestSaga.class), is(true));

        SagaHandlersMap handlers = scanResult.get(TestSaga.class);
        assertThat("Expected two total message handlers for saga.", handlers.messageHandlers(), hasSize(2));
    }

    /**
     * Given => Test saga found by injected scanner.
     * When  => scanHandledMessageTypes is called.
     * Then  => Returns a start handler with handling type string.
     */
    @Test
    public void scanHandledMessageTypes_testSagaFound_returnsStartHandlerOfTypeString() throws NoSuchMethodException {
        // given
        sagaTypes.add(TestSaga.class);

        // when
        Map<Class<? extends Saga>, SagaHandlersMap> scanResult = sut.scanHandledMessageTypes();

        // then
        SagaHandlersMap handlers = scanResult.get(TestSaga.class);
        assertThat(
                "Handler has entry with start saga flag set.",
                handlers.messageHandlers(),
                hasItem(samePropertyValuesAs(new MessageHandler(String.class, TestSaga.startupMethod(), true))));
    }

    /**
     * Given => Test saga found by injected scanner.
     * When  => scanHandledMessageTypes is called.
     * Then  => Returns a normal handler with handling type Integer.
     */
    @Test
    public void scanHandledMessageTypes_testSagaFound_returnsNormalHandlerOfTypeInteger() throws NoSuchMethodException {
        // given
        sagaTypes.add(TestSaga.class);

        // when
        Map<Class<? extends Saga>, SagaHandlersMap> scanResult = sut.scanHandledMessageTypes();

        // then
        SagaHandlersMap handlers = scanResult.get(TestSaga.class);
        assertThat(
                "Handler has entry with start saga flag set.",
                handlers.messageHandlers(),
                hasItem(samePropertyValuesAs(new MessageHandler(Integer.class, TestSaga.handlerMethod(), false))));
    }
}