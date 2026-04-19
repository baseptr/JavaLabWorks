package controller;

import dto.CurrencyRequest;
import entity.Currency;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.CurrencyService;

import java.util.List;

@RestController
@RequestMapping("/currencies")
@AllArgsConstructor
public class CurrencyController {

    private final CurrencyService currencyService;

    @Operation(summary = "Get all currency rates")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of currencies"),
            @ApiResponse(responseCode = "500", description = "Storage is empty")
    })
    @GetMapping
    public ResponseEntity<List<Currency>> getAll() {
        return ResponseEntity.ok(currencyService.getAll());
    }

    @Operation(summary = "Get rate by currency code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Currency rate found"),
            @ApiResponse(responseCode = "500", description = "Currency not found")
    })
    @GetMapping("/{type}")
    public ResponseEntity<Double> getByType(
            @Parameter(description = "Currency code, e.g. USD") @PathVariable String type) {
        return ResponseEntity.ok(currencyService.getByType(type));
    }

    @Operation(summary = "Create new currency rate")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Currency created"),
            @ApiResponse(responseCode = "400", description = "Currency already exists")
    })
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CurrencyRequest request) {
        currencyService.create(request.type(), request.rate());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update existing currency rate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Currency updated"),
            @ApiResponse(responseCode = "500", description = "Currency not found")
    })
    @PutMapping("/{type}")
    public ResponseEntity<Void> update(
            @Parameter(description = "Currency code, e.g. USD") @PathVariable String type,
            @RequestBody CurrencyRequest request) {
        currencyService.update(type, request.rate());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete currency rate")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Currency deleted"),
            @ApiResponse(responseCode = "500", description = "Currency not found")
    })
    @DeleteMapping("/{type}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Currency code, e.g. USD") @PathVariable String type) {
        currencyService.delete(type);
        return ResponseEntity.noContent().build();
    }
}
