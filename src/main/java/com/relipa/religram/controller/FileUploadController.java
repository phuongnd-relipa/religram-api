package com.relipa.religram.controller;

import com.relipa.religram.controller.bean.response.UploadBean;
import com.relipa.religram.exceptionhandler.StorageException;
import com.relipa.religram.exceptionhandler.StorageFileNotFoundException;
import com.relipa.religram.storage.StorageService;
import com.relipa.religram.util.MediaUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("v1/upload")
@Api(tags = {"upload"})
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    /*@GetMapping("/")
    public String listUploadFiles(Model model) throws IOException {
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString()
        ).collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/file/{fileName:.+}")
    @ApiOperation(value = "${upload.serveFile.get.value}", notes = "${upload.serveFile.get.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = Resource.class)})
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@ApiParam(value = "${upload.serveFile.get.param.fileName}", required = true) @PathVariable String fileName) {
        Resource file = storageService.loadAsResource(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; fileName=\"" + file.getFilename() + "\"").body(file);
    }*/

    @PostMapping("/uploadFile")
    @ApiOperation(value = "${upload.file.post.value}", notes = "${upload.file.post.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = UploadBean.class)})
    public ResponseEntity<UploadBean> handleFileUpload(@ApiParam(value = "${upload.file.post.param.file}", required = true) @RequestParam("file") MultipartFile file,
                                                       RedirectAttributes redirectAttributes,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        UploadBean uploadBean = new UploadBean();
        String fileUri = storageService.store(file, userDetails);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");

        uploadBean.setFileName(file.getOriginalFilename());
        uploadBean.setFileUri(fileUri);
        uploadBean.setFileType(MediaUtils.getMediaType(uploadBean.getFileName()));
        return ResponseEntity.ok(uploadBean);
    }

    @PostMapping("/uploadMultipleFiles")
    @ApiOperation(value = "${upload.multipleFile.post.value}", notes = "${upload.multipleFile.post.notes}")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "response.message.200", response = UploadBean.class, responseContainer = "List")})
    public ResponseEntity<List<UploadBean>> handleMultipleFilesUpload(@ApiParam(value = "${upload.multipleFile.post.param.files}", required = true) @RequestParam("files") MultipartFile[] files,
                                                                      RedirectAttributes redirectAttributes,
                                                                      @AuthenticationPrincipal UserDetails userDetails) {
        if (files.length == 0) {
            throw new StorageException("Failed to store empty file!");
        }
        List<UploadBean> uploadBeans = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileUri = storageService.store(file, userDetails);
            UploadBean uploadBean = new UploadBean();
            uploadBean.setFileName(file.getOriginalFilename());
            uploadBean.setFileUri(fileUri);
            uploadBean.setFileType(MediaUtils.getMediaType(uploadBean.getFileName()));
            uploadBeans.add(uploadBean);
        }
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + files.length + " files!");

        return ResponseEntity.ok(uploadBeans);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }
}
