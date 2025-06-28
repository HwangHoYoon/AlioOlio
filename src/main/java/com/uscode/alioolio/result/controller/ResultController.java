package com.uscode.alioolio.result.controller;

import com.uscode.alioolio.result.res.RstSt1Res;
import com.uscode.alioolio.result.res.RstSt2Res;
import com.uscode.alioolio.result.res.RstSt3A4Res;
import com.uscode.alioolio.result.service.ResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("result")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @GetMapping(value = "/step1")
    @Operation(summary = "입력 1단계", description = "입력 1단계")
    public RstSt1Res step1() throws IOException {
        return resultService.step1();
    }

    @GetMapping(value = "/step2")
    @Operation(summary = "입력 2단계", description = "입력 2단계")
    public RstSt2Res step2(@Schema(description = "location", example = "경상북도 의성군", name = "location") @RequestParam(name = "location") String location )throws IOException {
        return resultService.step2(location);
    }

    @GetMapping(value = "/step3")
    @Operation(summary = "입력 3단계", description = "입력 3단계")
    public RstSt3A4Res step3(
            @Schema(description = "location", example = "경상북도 의성군", name = "location") @RequestParam(name = "location") String location,
            @Schema(description = "crop", example = "마늘", name = "crop") @RequestParam(name = "crop") String crop,
            @Schema(description = "money", example = "1억", name = "money") @RequestParam(name = "money") String money,
            @Schema(description = "level", example = "농업 경력이 있어요", name = "level") @RequestParam(name = "level") String level
    )throws IOException {
        return resultService.step3And4(location, crop, money, level);
    }

}
