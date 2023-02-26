package com.example.kursovaya_3.Controller;

import com.example.kursovaya_3.Model.ColorSocks;
import com.example.kursovaya_3.Model.SizeSocks;
import com.example.kursovaya_3.Model.Socks;
import com.example.kursovaya_3.Service.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.BindException;
import java.util.Map;


@RestController
@RequestMapping("/api/socks")
@Tag(name = "API по работе со складом носков", description = "CRUD-операции")
public class SocksController {
    private final SocksService socksService;


    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @PostMapping()
    @Operation(
            summary = "Регистрация прихода товара на склад."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Удалось добавить приход;"),
            @ApiResponse(responseCode = "400", description = "параметры запроса отсутствуют или имеют некорректный формат;")}
    )
    public ResponseEntity<Object> add(@RequestBody Socks socks) {
        if (socks.getQuantity() < 0) {
            return ResponseEntity.badRequest().body("Нельзя добавить отрицательное кол-во носков");
        }
        return ResponseEntity.ok(socksService.addSocks(socks));
    }


    @PutMapping("/")
    @Operation(
            summary = "Обновление наличия носков на складе"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Удалось произвести отпуск носков со склада;"),
            @ApiResponse(responseCode = "400", description = "товара нет на складе в нужном количестве или параметры запроса имеют некорректный формат;"),
    }
    )
    public ResponseEntity<Object> update(@RequestBody Socks socks) {
        if (socksService.giveSocks(socks).getQuantity() == 0) {
            return ResponseEntity.badRequest().body("На складе нет нужного количества носков, выданы все что есть");
        }
        return ResponseEntity.ok(socks);
    }

    @GetMapping("/")
    @Operation(
            summary = "Выдача записи со склада"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "запись выдана")
    }
    )
    public ResponseEntity<Socks> getAll (@RequestParam ColorSocks colorSocks, SizeSocks sizeSocks, int minCotton, int maxCotton) {
        return ResponseEntity.ok(socksService.getSocks(colorSocks,sizeSocks,minCotton,maxCotton));
    }

    @DeleteMapping("/")
    @Operation(
            summary = "Удаление записи со склада"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "запись удалена")
    }
    )
    public ResponseEntity<Socks> delete(@RequestBody Socks socks) {
        return ResponseEntity.ok(socksService.delete(socks));
    }

}
