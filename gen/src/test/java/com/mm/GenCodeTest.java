package com.mm;

import com.mm.gen.service.GeneratorService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor
public class GenCodeTest {

    final GeneratorService generatorService;

    @Test
    public void code() {
        generatorService.generatorCode(new String[]{""}, "mainPath",
                "packageName", "moduleName", "author");
    }
}
