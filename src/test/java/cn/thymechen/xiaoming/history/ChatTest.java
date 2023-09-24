package cn.thymechen.xiaoming.history;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class ChatTest {
    @Test
    public void messageChain() {
        List<Object> list = new ArrayList<>();
        list.add(123);
        list.add(2.5);
        list.add("aaa");
        list.add(true);

        List<Object> list1 = list.stream()
                .filter(o -> o instanceof String)
                .map(o -> 1)
                .collect(Collectors.toList());

        System.out.println(list1);
    }

    @Test
    public void base64() throws IOException {
        String base64 = Base64.getEncoder().encodeToString(new FileInputStream("C:\\Users\\Thyme-Chen\\Desktop\\图片\\16695686138509c0e87423ff403c62bf2aa5633e11df12c085da897d940eea610db25df34a433.0.png").readAllBytes());
        System.out.println();
        System.out.println("[data:image/*;base64," + base64 + "]");
        System.out.println();
    }

    @SneakyThrows
    @Test
    public void string() {
        System.out.println(String.format("%d aa", 13251));
    }
}
