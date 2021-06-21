package io.github.xiaoyureed.mockitomybatisplusdemo;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;


/**
 * @author xiaoyu
 * date: 2020/1/22
 */
@Slf4j
public class MockitoTest {
    @Test
    public void testMockito() {
        List<String> mockList = mock(List.class);
        mockList.add("hello"); // add() is not been called actually; add 方法实际未被调用
        log.info(">>> mockList: {}", mockList.get(0));// null
        mockList.clear();

        verify(mockList).add("hello"); // 监控 add("hello"), 方法被调用 (参数值必须完全一样)
        verify(mockList).clear();
        verify(mockList, never()).add("xxx");// 监控没有被调用

        mockList.add("hello");
        verify(mockList, times(2)).add("hello");
        mockList.add("hella"); // 参数值不同, 实际还是被认为仅仅执行了2次
        verify(mockList, times(2)).add("hello");
    }
}
