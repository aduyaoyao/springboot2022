package com.example.demo4.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DisplayName("测试Junit5")
public class Junit5Test {

    @Test
    @DisplayName("测试displayName")
    void testDisplayName(){
        System.out.println("1");
    }

    @Disabled
    @Test
    void test2(){
        System.out.println("2");
    }

    @Timeout(value = 500,unit = TimeUnit.MILLISECONDS)
    @Test
    void testTimeOut() throws InterruptedException {
        Thread.sleep(200);
    }

    @BeforeEach
    void testBeforeEach(){
        System.out.println("BeforeEach");
    }
    @AfterEach
    void testAfterEach(){
        System.out.println("AfterEach");
    }

    @BeforeAll
    static void testBeforeAll(){
        System.out.println("BeforeAll");
    }

    @AfterAll
    static void testAfterAll(){
        System.out.println("AfterAll");
    }

    @Test
    void testSimpleAssertions(){
        int cal = cal(2, 3);
        assertEquals(5, cal,"you are the best student");
    }
    int cal(int i,int j){
        return i+j;
    }
    @Test
    @DisplayName("array assertion")
    public void array() {
        assertArrayEquals(new int[]{1, 2}, new int[] {1, 2});
    }

    @Test
    @DisplayName("assert all")
    public void all() {
        /**
         * 所有断言全部需要成功
         */
        assertAll("Math",
                () -> assertEquals(2, 1 + 1),
                () -> assertTrue(1 > 0)
        );
    }
    @Test
    @DisplayName("异常测试")
    public void exceptionTest() {
        ArithmeticException exception = Assertions.assertThrows(
                //扔出断言异常
                ArithmeticException.class, () -> System.out.println(1 % 0));
    }
    @Test
    @Disabled
    @DisplayName("fail")
    public void shouldFail() {
        fail("This should fail");
    }

    @Test
    @DisplayName("测试前置条件")
    void testAssumption(){
        Assumptions.assumeTrue(true);
        System.out.println("1");
    }
    @ParameterizedTest
    @DisplayName("参数化测试")
    @ValueSource(ints = {1,2,3,4,5})
    void testParameterized(int i){
        System.out.println(i);
    }
    @ParameterizedTest
    @DisplayName("参数化测试2")
    @MethodSource("stringProvider")
    void testParameterized2(String i){
        System.out.println(i);
    }
    static Stream<String> stringProvider(){
        return Stream.of("a","b");
    }
}
