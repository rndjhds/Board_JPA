package com.jpa.board.service;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@DisplayName("비즈니스 로직 - 페이지네이션")
@SpringBootTest
class PaginationTest {

    @Autowired
    private PaginationService paginationService;

    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 만들어준다.")
    @MethodSource // 메서드로 테스트 파라미터를 넘겨줘서 @ParameterizedTest로 테스트를 할 수 있게 해준다.
    @ParameterizedTest(name = "[{index}] 현재 페이지: {0}, 총 페이지: {1} => {2}") // 파라미터를 줘서 테스트를 파라미터 값을 변형시켜서 자동으로 여러번 테스트를 할 수 있다.
    public void givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumbers(int currentPageNumber, int totalPages, List<Integer> expected) {
        // given

        // when
        List<Integer> actual = paginationService.getPaginationBarNumbers(currentPageNumber, totalPages);
        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    // @MethodSource를 사용하기 위해서 테스트 하는 메서드와 같은 이름으 static 메서드를 만든다.
    static Stream<Arguments> givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumbers() {
        return Stream.of(
                Arguments.arguments(0, 10, Arrays.nonNullElementsIn(new Integer[]{0, 1, 2, 3, 4})),
                Arguments.arguments(1, 10, Arrays.nonNullElementsIn(new Integer[]{0, 1, 2, 3, 4})),
                Arguments.arguments(2, 10, Arrays.nonNullElementsIn(new Integer[]{0, 1, 2, 3, 4})),
                Arguments.arguments(3, 10, Arrays.nonNullElementsIn(new Integer[]{1, 2, 3, 4, 5})),
                Arguments.arguments(4, 10, Arrays.nonNullElementsIn(new Integer[]{2, 3, 4, 5, 6})),
                Arguments.arguments(5, 10, Arrays.nonNullElementsIn(new Integer[]{3, 4, 5, 6, 7})),
                Arguments.arguments(6, 10, Arrays.nonNullElementsIn(new Integer[]{4, 5, 6, 7, 8})),
                Arguments.arguments(7, 10, Arrays.nonNullElementsIn(new Integer[]{5, 6, 7, 8, 9})),
                Arguments.arguments(8, 10, Arrays.nonNullElementsIn(new Integer[]{6, 7, 8, 9})),
                Arguments.arguments(9, 10, Arrays.nonNullElementsIn(new Integer[]{7, 8, 9}))
        );
    }

}