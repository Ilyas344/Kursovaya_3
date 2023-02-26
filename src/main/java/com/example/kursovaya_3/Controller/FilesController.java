package com.example.kursovaya_3.Controller;

import com.example.kursovaya_3.Service.FileService;
import com.example.kursovaya_3.Service.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("api/files")
@Tag(name = "API по работе с файлами")
public class FilesController {
    private final FileService fileService;
    private final SocksService socksService;
    @Value("${name.of.file}")
    private String socksFileName;
    @Value("${name.of.file.history}")
    private String socksFileNameHistory;

    public FilesController(FileService fileService, SocksService socksService) {
        this.fileService = fileService;
        this.socksService = socksService;
    }

    @GetMapping("/export")
    @Operation(
            summary = "Выдача файла с количеством носков."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Удалось выдать файл"),
            @ApiResponse(responseCode = "400", description = "параметры запроса отсутствуют или имеют некорректный формат;"),
    })
    public ResponseEntity<InputStreamResource> downloadFiles() throws FileNotFoundException {
        File file = fileService.getFromFile(socksFileName);
        if (file.exists()) {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"socks.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Загрузка файла с количеством носков"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Удалось загрузить файл файл"),
            @ApiResponse(responseCode = "400", description = "параметры запроса отсутствуют или имеют некорректный формат;"),
    })
    public ResponseEntity<Void> uploadFiles(@RequestParam MultipartFile files) {
        fileService.clearFile(socksFileName);
        File file = fileService.getFromFile(socksFileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            IOUtils.copy(files.getInputStream(), fos);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/export/history")
    @Operation(
            summary = "Выдача файла с историей операцией"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Удалось выдать файл"),
            @ApiResponse(responseCode = "400", description = "параметры запроса отсутствуют или имеют некорректный формат;"),
    })
    public ResponseEntity<InputStreamResource> downloadFilesTxt() throws IOException {
        File file = fileService.getFromFile(socksFileNameHistory);
        if (file.exists()) {

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .contentLength(file.length())
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"socksHistory.json\"")
                    .body(resource);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}
