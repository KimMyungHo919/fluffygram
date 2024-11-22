package com.fluffygram.newsfeed.global.tool;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class UploadImage {

    public static void FileAndDataUploadController(Path path) throws IOException {
        // 업로드 디렉토리 생성 (없으면 생성)
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }

    static public String uploadUserImage(Path path, MultipartFile file) {
        String uniqueFilename;

        if (file.isEmpty()) {
            return null;
        }

        String contentType = file.getContentType();
        String originalFileExtension;

        // 타입에 따른 확장자 결정
        if (ObjectUtils.isEmpty(contentType)) {
            // 타입 없으면 null
            throw new RuntimeException("잘못된 확장자 입니다.");
        } else {
            if (contentType.contains("image/jpeg")) {
                originalFileExtension = ".jpg";
            } else if (contentType.contains("image/png")) {
                originalFileExtension = ".png";
            } else {
                throw new RuntimeException("잘못된 이미지 확장자 입니다.");
            }
        }

        try {
            FileAndDataUploadController(path);

            // 고유 파일 이름 생성
            uniqueFilename = UUID.randomUUID() + originalFileExtension;

            // 파일 저장 경로
            Path filePath = path.resolve(uniqueFilename);

            // 파일 저장
            file.transferTo(filePath.toFile());

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());

        }
        return uniqueFilename;
    }
}
